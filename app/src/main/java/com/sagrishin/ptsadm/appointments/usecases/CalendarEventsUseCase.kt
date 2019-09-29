package com.sagrishin.ptsadm.appointments.usecases

import com.sagrishin.ptsadm.appointments.UiAppointment
import com.sagrishin.ptsadm.appointments.UiMonthData
import com.sagrishin.ptsadm.appointments.repositories.AppointmentsRepository
import com.sagrishin.ptsadm.common.api.ApiAppointment
import com.sagrishin.ptsadm.common.api.ApiDay
import com.sagrishin.ptsadm.common.api.toApiModel
import com.sagrishin.ptsadm.common.api.toUiModel
import com.sagrishin.ptsadm.common.firstDayOfMonth
import com.sagrishin.ptsadm.common.lastDayOfMonth
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime

class CalendarEventsUseCase(
    private val appointmentsRepository: AppointmentsRepository,
    private val patientsRepository: PatientsRepository
) {

    fun getCalendarEvents(dayInMonth: DateTime): Single<UiMonthData> {
        return appointmentsRepository.getAppointmentsIn(dayInMonth.firstDayOfMonth, dayInMonth.lastDayOfMonth).map {
            UiMonthData(dayInMonth.monthOfYear, it.map(ApiDay::toUiModel))
        }
    }

    fun saveNewAppointment(uiPatient: UiPatient, dateTime: DateTime): Single<UiPatient> {
        val apiAppointment = ApiAppointment(dateTime = dateTime.toLocalDateTime())
        val newAppointmentSingle = if (uiPatient.isNewPatient()) {
            patientsRepository.savePatient(uiPatient.toApiModel()).flatMap {
                appointmentsRepository.addNewAppointment(it.id!!, apiAppointment)
            }
        } else {
            appointmentsRepository.addNewAppointment(uiPatient.id, apiAppointment)
        }

        return newAppointmentSingle.map { appointment ->
            uiPatient.apply {
                appointments.clear()
                appointments += appointment.toUiModel()
            }
        }
    }

    fun deleteAppointmentBy(appointmentId: Long): Completable {
        return appointmentsRepository.deleteAppointmentBy(appointmentId)
    }

    fun getAppointmentsBy(patientId: Long): Single<List<UiAppointment>> {
        return appointmentsRepository.getAppointmentsBy(patientId).map { it.map { it.toUiModel() } }
    }

}
