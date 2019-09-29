package com.sagrishin.ptsadm.common.api

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

const val REASON_MESSAGE_HEADER_NAME = "Reason-Message"


fun <T> Single<Response<T>>.getResponseBody(): Single<T> {
    return flatMap {
        if (it.isSuccessful && (it.body() != null) && (it.errorBody() == null)) {
            Single.create<T> { e -> e.onSuccess(it.body()!!) }
        } else {
            Single.create<T> { e -> e.onError(Exception(it.headers()[REASON_MESSAGE_HEADER_NAME])) }
        }
    }
}


fun <T> Single<Response<T>>.callInBackgroundAndProvideInUi(): Single<Response<T>> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}


fun <T> callSingle(apiCall: () -> Single<Response<T>>): Single<T> {
    return apiCall().callInBackgroundAndProvideInUi().getResponseBody()
}


fun <T> Single<T>.retrySingleCall(timeout: Long = 60, retryCount: Int = 3): Single<T> {
    return timeout(timeout, TimeUnit.SECONDS)
        .retry { count, error -> (error is SocketTimeoutException) && (count <= retryCount) }
}
