package com.sagrishin.ptsadm.login.repositories

import com.sagrishin.ptsadm.common.api.ApiUser
import io.reactivex.Single

interface AuthenticateRepository {

    fun updateToken(): Single<String>

    fun register(enteredData: ApiUser): Single<ApiUser>

    fun authorise(enteredData: ApiUser): Single<String>

}
