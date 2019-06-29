package com.sagrishin.ptsadm.common.api

import com.sagrishin.ptsadm.appointments.UiAppointment
import com.sagrishin.ptsadm.login.UiUser
import com.sagrishin.ptsadm.common.uikit.calendar.UiDayWithEvents
import com.sagrishin.ptsadm.patients.UiPatient

fun ApiPatient.toUiModel(): UiPatient {
    return UiPatient(
        id = id?.let { it } ?: -1,
        name = name,
        surname = surname,
        phoneNumber = phoneNumber,
        appointments = appointments.map { it.toUiModel() }.toMutableList()
    )
}


fun ApiAppointment.toUiModel(): UiAppointment {
    return UiAppointment(
        id = id?.let { it } ?: -1,
        description = description,
        dateTime = dateTime.toDateTime()
    )
}


fun ApiDay.toUiModel(): UiDayWithEvents {
    return UiDayWithEvents(
        day = date,
        events = patients.map { it.toUiModel() }.toMutableList()
    )
}


fun ApiUser.toUiModel(): UiUser? {
    return UiUser(
        login = login,
        password = password
    )
}


/* */


fun UiUser.toApiModel(): ApiUser {
    return ApiUser(
        login = login,
        password = password
    )
}


fun UiPatient.toApiModel(): ApiPatient {
    return ApiPatient(
        name = name,
        surname = surname,
        phoneNumber = phoneNumber
    )
}
