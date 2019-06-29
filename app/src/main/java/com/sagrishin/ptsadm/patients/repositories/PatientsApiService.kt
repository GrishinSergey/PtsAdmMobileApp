package com.sagrishin.ptsadm.patients.repositories

import com.sagrishin.ptsadm.common.api.ApiPatient
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface PatientsApiService {

    @GET("/patients/getAllByCurrentDoctor")
    fun getAllByCurrentDoctor(): Single<Response<List<ApiPatient>>>

    @POST("/patients/save")
    fun save(@Body apiPatient: ApiPatient): Single<Response<ApiPatient>>

    @DELETE("/patients/deletePatientBy/{id}")
    fun deleteBy(@Path("id") id: Long): Single<Response<Boolean>>

}
