package com.sagrishin.ptsadm.common.uikit.calendar

import androidx.core.util.toRange
import org.joda.time.DateTime
import org.joda.time.LocalDate
import kotlin.math.min

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


inline fun <reified T> List<T>.fillWithSized(value: T, newSize: Int): List<T> {
    val fixedSizeList = Array(newSize) { value }
    for (i in 0 until min(newSize, size)) fixedSizeList[i] = this[i]
    return listOf(*fixedSizeList)
}


fun IntRange.fillWithSized(value: Int, newSize: Int): List<Int> {
    return (start..endInclusive).toList().fillWithSized(value, newSize)
}
