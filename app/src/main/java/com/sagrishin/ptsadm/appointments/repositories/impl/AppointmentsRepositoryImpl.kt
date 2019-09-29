package com.sagrishin.ptsadm.appointments.repositories.impl

import com.sagrishin.ptsadm.appointments.repositories.AppointmentsApiService
import com.sagrishin.ptsadm.appointments.repositories.AppointmentsRepository
import com.sagrishin.ptsadm.common.api.ApiAppointment
import com.sagrishin.ptsadm.common.api.ApiDay
import com.sagrishin.ptsadm.common.api.callSingle
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

class AppointmentsRepositoryImpl(
    private val apiService: AppointmentsApiService
): AppointmentsRepository {

    override fun getAppointmentsIn(start: LocalDate, end: LocalDate): Single<List<ApiDay>> {
        return callSingle { apiService.getAppointmentsIn(start, end) }
    }

    override fun addNewAppointment(patientId: Long, apiAppointment: ApiAppointment): Single<ApiAppointment> {
        return callSingle { apiService.addNewAppointment(patientId, apiAppointment) }
    }

    override fun deleteAppointmentBy(appointmentId: Long): Completable {
        return callSingle { apiService.deleteAppointmentBy(appointmentId) }.flatMapCompletable {
            if (it) Completable.complete() else Completable.error(Throwable())
        }
    }

    override fun getAppointmentsBy(patientId: Long): Single<List<ApiAppointment>> {
        return callSingle { apiService.getAppointmentsBy(patientId) }
    }

}
