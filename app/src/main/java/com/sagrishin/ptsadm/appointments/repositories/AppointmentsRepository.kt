package com.sagrishin.ptsadm.appointments.repositories

import com.sagrishin.ptsadm.common.api.ApiAppointment
import com.sagrishin.ptsadm.common.api.ApiDay
import com.sagrishin.ptsadm.patients.UiPatient
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.LocalDate

interface AppointmentsRepository {

    fun getAppointmentsIn(start: LocalDate, end: LocalDate): Single<List<ApiDay>>

    fun addNewAppointment(patientId: Long, apiAppointment: ApiAppointment): Single<ApiAppointment>

    fun deleteAppointmentBy(appointmentId: Long): Single<Boolean>

    fun getAppointmentsBy(patientId: Long): Single<List<ApiAppointment>>

}
