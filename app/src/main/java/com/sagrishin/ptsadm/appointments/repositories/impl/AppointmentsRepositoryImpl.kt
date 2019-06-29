package com.sagrishin.ptsadm.appointments.repositories.impl

import com.sagrishin.ptsadm.appointments.repositories.AppointmentsApiService
import com.sagrishin.ptsadm.appointments.repositories.AppointmentsRepository
import com.sagrishin.ptsadm.common.api.*
import com.sagrishin.ptsadm.patients.UiPatient
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import retrofit2.Response

class AppointmentsRepositoryImpl(
    private val apiService: AppointmentsApiService
): BaseRepository(), AppointmentsRepository {

    override fun getAppointmentsIn(start: LocalDate, end: LocalDate): Single<List<ApiDay>> {
        return callSingle { apiService.getAppointmentsIn(start, end) }
    }

    override fun addNewAppointment(patientId: Long, apiAppointment: ApiAppointment): Single<ApiAppointment> {
        return callSingle { apiService.addNewAppointment(patientId, apiAppointment) }
    }

    override fun deleteAppointmentBy(appointmentId: Long): Single<Boolean> {
        return callSingle { apiService.deleteAppointmentBy(appointmentId) }
    }

}
