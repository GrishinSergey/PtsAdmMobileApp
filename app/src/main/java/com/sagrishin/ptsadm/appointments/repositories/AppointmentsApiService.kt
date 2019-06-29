package com.sagrishin.ptsadm.appointments.repositories

import com.sagrishin.ptsadm.common.api.ApiAppointment
import com.sagrishin.ptsadm.common.api.ApiDay
import com.sagrishin.ptsadm.common.api.ApiPatient
import io.reactivex.Single
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.LocalDate
import retrofit2.Response
import retrofit2.http.*

interface AppointmentsApiService {

    @GET("/appointments/getAppointmentsIn/{start}/{end}")
    fun getAppointmentsIn(
        @Path("start") start: LocalDate,
        @Path("end") end: LocalDate
    ): Single<Response<List<ApiDay>>>

    @POST("/appointments/addNewAppointment/{patientId}")
    fun addNewAppointment(
        @Path("patientId") patientId: Long,
        @Body apiAppointment: ApiAppointment
    ): Single<Response<ApiAppointment>>


    @DELETE("/appointments/deleteAppointmentBy/{id}")
    fun deleteAppointmentBy(
        @Path("id") appointmentId: Long
    ): Single<Response<Boolean>>

}
