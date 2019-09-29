package com.sagrishin.ptsadm.login.repositories

import com.sagrishin.ptsadm.common.api.ApiUser
import io.reactivex.Completable
import io.reactivex.Single

interface AuthenticateRepository {

    fun updateToken(): Completable

    fun register(enteredData: ApiUser): Single<ApiUser>

    fun authorise(enteredData: ApiUser): Completable

}
