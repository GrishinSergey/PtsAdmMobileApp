package com.sagrishin.ptsadm.appointments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sagrishin.ptsadm.appointments.UiAppointment
import com.sagrishin.ptsadm.appointments.UiMonthData
import com.sagrishin.ptsadm.appointments.usecases.CalendarEventsUseCase
import com.sagrishin.ptsadm.common.firstDayOfMonth
import com.sagrishin.ptsadm.common.lastDayOfMonth
import com.sagrishin.ptsadm.common.livedata.ActionLiveData
import com.sagrishin.ptsadm.common.livedata.let
import com.sagrishin.ptsadm.common.uikit.calendar.UiDayWithEvents
import com.sagrishin.ptsadm.common.uikit.calendar.mapLocalDates
import com.sagrishin.ptsadm.patients.UiPatient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.joda.time.DateTime

class AppointmentsCalendarViewModel(
    private val calendarEventsUseCase: CalendarEventsUseCase
): ViewModel() {

    lateinit var dayInMonth: DateTime
    val dayEvents = MediatorLiveData<List<UiPatient>>()
    val monthData: MutableLiveData<UiMonthData> by lazy { getInitialMonthData() }

    val errorLiveData: LiveData<String>
        get() = _errorLiveData
    private val _errorLiveData = ActionLiveData<String>()

    val patientAppointmentsLiveData = MediatorLiveData<List<UiAppointment>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadMonthEvents(dayInMonth: DateTime) {
        compositeDisposable += calendarEventsUseCase.getCalendarEvents(dayInMonth).doOnSuccess {
            monthData.value = it?.let { it } ?: UiMonthData(dayInMonth.monthOfYear, emptyList())
        }.doOnError {
            _errorLiveData.value = it.message
            /// need to notify ui about error
        }.subscribe()
    }

    fun loadEventsForDay(day: DateTime) {
        dayEvents.addSource(findDayWithEventsBy(day)) { dayEvents.value = it.events.toMutableList() }
    }

    fun loadAppointmentsBy(patientId: Long) {
        compositeDisposable += calendarEventsUseCase.getAppointmentsBy(patientId).doOnSuccess {
            patientAppointmentsLiveData.value = it.sortedByDescending { it.dateTime }
        }.doOnError {
            _errorLiveData.value = it.message
            /// need to notify ui about error
        }.subscribe()
    }

    fun saveNewAppointment(uiPatient: UiPatient, dateTime: DateTime) {
        compositeDisposable += calendarEventsUseCase.saveNewAppointment(uiPatient, dateTime).doOnSuccess { newPatient ->
            val appointmentDay = dateTime.withTimeAtStartOfDay()
            monthData.value!!.perDayEvents.find { it.day == appointmentDay }!!.let {
                it.events += newPatient
                it.countEvents = it.events.size
            }
            monthData.value = monthData.value
        }.doOnError {
            _errorLiveData.value = it.message
            /// need to notify ui about error
        }.subscribe()
    }

    fun deleteAppointmentBy(appointmentId: Long, appointmentDay: DateTime) {
        compositeDisposable += calendarEventsUseCase.deleteAppointmentBy(appointmentId).doOnComplete {
            val day = appointmentDay.withTimeAtStartOfDay()
            monthData.value!!.perDayEvents.find { it.day == day }!!.let {
                it.events.removeIf { it.appointments[0].id == appointmentId }
                it.countEvents = it.events.size
            }
            monthData.value = monthData.value
        }.doOnError {
            _errorLiveData.value = it.message
            /// need to notify ui about error
        }.subscribe()
    }


    private fun getInitialMonthData(): MutableLiveData<UiMonthData> {
        return MutableLiveData<UiMonthData>().let {
            UiMonthData(
                monthNumber = dayInMonth.monthOfYear,
                perDayEvents = (dayInMonth.firstDayOfMonth..dayInMonth.lastDayOfMonth).mapLocalDates {
                    UiDayWithEvents(it.toDateTimeAtStartOfDay())
                }
            )
        }
    }

    private fun findDayWithEventsBy(day: DateTime): LiveData<UiDayWithEvents> {
        return monthData.let { it.perDayEvents.find { day == it.day }?.let { it } ?: UiDayWithEvents(day) }
    }

}
