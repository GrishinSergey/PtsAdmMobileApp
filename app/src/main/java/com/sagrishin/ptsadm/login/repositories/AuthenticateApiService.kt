package com.sagrishin.ptsadm.login.repositories

import com.sagrishin.ptsadm.common.api.ApiUser
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticateApiService {

    @POST("/authorization/updateToken")
    fun updateToken(): Single<Response<String>>

    @POST("/authorization/register")
    fun register(@Body enteredData: ApiUser): Single<Response<ApiUser>>

    @POST("/authorization/authorize")
    fun authorise(@Body enteredData: ApiUser): Single<Response<String>>

}
