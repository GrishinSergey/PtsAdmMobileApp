package com.sagrishin.ptsadm.appointments

import com.sagrishin.ptsadm.common.uikit.calendar.UiDayWithEvents
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

data class UiAppointment(
    val id: Long = -1,
    val description: String = "",
    val dateTime: DateTime = DateTime.now()
)


data class UiMonthData(
    val monthNumber: Int,
    val perDayEvents: List<UiDayWithEvents>
)
