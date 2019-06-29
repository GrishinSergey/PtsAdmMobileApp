package com.sagrishin.ptsadm.common.uikit.calendar

import com.sagrishin.ptsadm.patients.UiPatient
import org.joda.time.DateTime

data class UiDayWithEvents(
    val day: DateTime,
    val events: MutableList<UiPatient> = mutableListOf(),
    var countEvents: Int = events.size
)
