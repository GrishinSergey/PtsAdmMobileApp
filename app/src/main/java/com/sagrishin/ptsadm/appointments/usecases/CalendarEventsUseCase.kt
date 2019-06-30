package com.sagrishin.ptsadm.appointments.usecases

import androidx.lifecycle.LiveData
import com.sagrishin.ptsadm.appointments.UiAppointment
import com.sagrishin.ptsadm.appointments.UiMonthData
import com.sagrishin.ptsadm.appointments.repositories.AppointmentsRepository
import com.sagrishin.ptsadm.common.api.*
import com.sagrishin.ptsadm.common.firstDayOfMonth
import com.sagrishin.ptsadm.common.lastDayOfMonth
import com.sagrishin.ptsadm.common.livedata.mutableLiveDataOf
import com.sagrishin.ptsadm.common.usecases.BaseUseCase
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.rxkotlin.plusAssign
import org.joda.time.DateTime

class CalendarEventsUseCase(
    private val appointmentsRepository: AppointmentsRepository,
    private val patientsRepository: PatientsRepository
): BaseUseCase() {

    fun getCalendarEvents(dayInMonth: DateTime): LiveData<UiMonthData?> {
        val result = mutableLiveDataOf<UiMonthData?>()

        val start = dayInMonth.firstDayOfMonth
        val end = dayInMonth.lastDayOfMonth
        compositeDisposable += appointmentsRepository.getAppointmentsIn(start, end)
            .map { UiMonthData(dayInMonth.monthOfYear, it.map(ApiDay::toUiModel)) }
            .subscribe(result::setValue)

        return result
    }

    fun saveNewAppointment(uiPatient: UiPatient, dateTime: DateTime): LiveData<UiPatient?> {
        val result = mutableLiveDataOf<UiPatient?>()

        val apiAppointment = ApiAppointment(dateTime = dateTime.toLocalDateTime())
        val newAppointmentSingle = if (uiPatient.isNewPatient()) {
            patientsRepository.savePatient(uiPatient.toApiModel()).flatMap {
                appointmentsRepository.addNewAppointment(it.id!!, apiAppointment)
            }
        } else {
            appointmentsRepository.addNewAppointment(uiPatient.id, apiAppointment)
        }

        compositeDisposable += newAppointmentSingle.doOnError {
            result.value = null
        }.doOnSuccess { appointment ->
            uiPatient.appointments.let {
                it.clear()
                it += appointment.toUiModel()
            }
            result.value = uiPatient
        }.subscribe()

        return result
    }

    fun deleteAppointmentBy(appointmentId: Long): LiveData<Boolean> {
        val result = mutableLiveDataOf<Boolean>()

        compositeDisposable += appointmentsRepository.deleteAppointmentBy(appointmentId).doOnError {
            result.value = false
        }.doOnSuccess {
            result.value = it
        }.subscribe()

        return result
    }

    fun getAppointmentsBy(patientId: Long): LiveData<List<UiAppointment>> {
        val result = mutableLiveDataOf<List<UiAppointment>>()

        compositeDisposable += appointmentsRepository.getAppointmentsBy(patientId).doOnError {
            result.value = emptyList()
        }.doOnSuccess {
            result.value = it.map { it.toUiModel() }
        }.subscribe()

        return result
    }

}
