package com.sagrishin.ptsadm.common.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

abstract class BaseRepository {

    fun <T> callSingle(apiCall: () -> Single<Response<T>>): Single<T> {
        return apiCall().callInBackgroundAndProvideInUi().getResponseBody()
    }

    fun <T> callObservable(apiCall: () -> Observable<Response<T>>): Observable<T> {
        return apiCall().callInBackgroundAndProvideInUi().getResponseBody()
    }

}
