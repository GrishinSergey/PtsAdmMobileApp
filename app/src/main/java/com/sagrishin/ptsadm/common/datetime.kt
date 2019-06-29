package com.sagrishin.ptsadm.common

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

val currentDayDateTime: DateTime by lazy {
    DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+03:00")))
}

fun DateTime.plusOneMonth(): DateTime = plusMonths(1).withTimeAtStartOfDay()

fun DateTime.minusOneMonth(): DateTime = minusMonths(1).withTimeAtStartOfDay()

fun DateTime.isCurrentMonth(): Boolean = monthOfYear == currentDayDateTime.monthOfYear


val DateTime.firstDayOfMonth: LocalDate get() = dayOfMonth().withMinimumValue().toLocalDate()

val DateTime.lastDayOfMonth: LocalDate get() = dayOfMonth().withMaximumValue().toLocalDate()

val DateTime.firstDayOfWeek: LocalDate get() = withDayOfWeek(1).toLocalDate()

val DateTime.lastDayOfWeek: LocalDate get() = withDayOfWeek(7).toLocalDate()
