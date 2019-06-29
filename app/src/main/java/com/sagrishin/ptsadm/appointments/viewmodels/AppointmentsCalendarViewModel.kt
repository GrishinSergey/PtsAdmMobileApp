package com.sagrishin.ptsadm.appointments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.sagrishin.ptsadm.appointments.UiMonthData
import com.sagrishin.ptsadm.appointments.usecases.CalendarEventsUseCase
import com.sagrishin.ptsadm.common.firstDayOfMonth
import com.sagrishin.ptsadm.common.lastDayOfMonth
import com.sagrishin.ptsadm.common.livedata.let
import com.sagrishin.ptsadm.common.uikit.calendar.UiDayWithEvents
import com.sagrishin.ptsadm.common.uikit.calendar.mapLocalDays
import com.sagrishin.ptsadm.patients.UiPatient
import org.joda.time.DateTime

class AppointmentsCalendarViewModel(
    private val calendarEventsUseCase: CalendarEventsUseCase
): ViewModel() {

    lateinit var dayInMonth: DateTime
    val dayEvents = MediatorLiveData<List<UiPatient>>()
    val monthData: MediatorLiveData<UiMonthData> by lazy {
        MediatorLiveData<UiMonthData>().let {
            UiMonthData(
                dayInMonth.monthOfYear,
                (dayInMonth.firstDayOfMonth..dayInMonth.lastDayOfMonth).mapLocalDays {
                    UiDayWithEvents(it.toDateTimeAtStartOfDay())
                }
            )
        } as MediatorLiveData<UiMonthData>
    }

    override fun onCleared() {
        calendarEventsUseCase.clearSubscriptions()
    }

    fun loadMonthEvents(dayInMonth: DateTime) {
        monthData.addSource(calendarEventsUseCase.getCalendarEvents(dayInMonth)) {
            monthData.value = it?.let { it } ?: UiMonthData(dayInMonth.monthOfYear, emptyList())
        }
    }

    fun loadEventsForDay(day: DateTime) {
        dayEvents.addSource(findDayWithEventsBy(day)) {
            dayEvents.value = it.events.toMutableList()
        }
    }

    fun saveNewAppointment(uiPatient: UiPatient, dateTime: DateTime) {
        monthData.addSource(calendarEventsUseCase.saveNewAppointment(uiPatient, dateTime)) { patientWithAppointment ->
            patientWithAppointment?.let {
                val appointmentDay = dateTime.withTimeAtStartOfDay()
                monthData.value!!.perDayEvents.find { it.day == appointmentDay }!!.let {
                    it.events += patientWithAppointment
                    it.countEvents = it.events.size
                }
                monthData.value = monthData.value
            } ?: let {
                /// need to notify ui about error
            }
        }
    }

    fun deleteAppointmentBy(appointmentId: Long, appointmentDay: DateTime) {
        monthData.addSource(calendarEventsUseCase.deleteAppointmentBy(appointmentId)) { operationResult ->
            if (operationResult) {
                val day = appointmentDay.withTimeAtStartOfDay()
                monthData.value!!.perDayEvents.find { it.day == day }!!.let {
                    it.events.removeIf { it.appointments[0].id == appointmentId }
                    it.countEvents = it.events.size
                }
                monthData.value = monthData.value
            } else {
                /// need to notify ui about error
            }
        }
    }

    private fun findDayWithEventsBy(day: DateTime): LiveData<UiDayWithEvents> {
        return monthData.let { inMonthDay ->
            inMonthDay.perDayEvents.find { day == it.day }?.let { it } ?: UiDayWithEvents(day)
        }
    }

}
