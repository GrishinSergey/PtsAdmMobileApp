package com.sagrishin.ptsadm.common.api

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.lang.Exception

const val REASON_MESSAGE_HEADER_NAME = "Reason-Message"

fun <T> Observable<Response<T>>.getResponseBody(): Observable<T> {
    return flatMap {
        if (it.isSuccessful && (it.body() != null) && (it.errorBody() == null)) {
            Observable.create<T> { e -> e.onNext(it.body()!!) }
        } else {
            Observable.create<T> { e -> e.onError(Exception(it.headers()[REASON_MESSAGE_HEADER_NAME])) }
        }
    }
}

fun <T> Single<Response<T>>.getResponseBody(): Single<T> {
    return flatMap {
        if (it.isSuccessful && (it.body() != null) && (it.errorBody() == null)) {
            Single.create<T> { e -> e.onSuccess(it.body()!!) }
        } else {
            Single.create<T> { e -> e.onError(Exception(it.headers()[REASON_MESSAGE_HEADER_NAME])) }
        }
    }
}

fun <T> Observable<Response<T>>.callInBackgroundAndProvideInUi(): Observable<Response<T>> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<Response<T>>.callInBackgroundAndProvideInUi(): Single<Response<T>> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
