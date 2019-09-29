package com.sagrishin.ptsadm.common.uikit.calendar

import org.joda.time.DateTime
import org.joda.time.LocalDate

internal inline fun <R> ClosedRange<DateTime>.mapDateTimes(
    step: DateTime.() -> DateTime = { this.plusDays(1) },
    action: (DateTime) -> R
): List<R> {
    val res = mutableListOf<R>()
    var day = start
    while (day <= endInclusive) {
        res += action(day)
        day = day.step()
    }

    return res
}

internal inline fun <R> ClosedRange<LocalDate>.mapLocalDates(
    step: LocalDate.() -> LocalDate = { this.plusDays(1) },
    action: (LocalDate) -> R
): List<R> {
    val res = mutableListOf<R>()
    var day = start
    while (day <= endInclusive) {
        res += action(day)
        day = day.step()
    }

    return res
}
