package com.sagrishin.ptsadm.patients

import com.sagrishin.ptsadm.appointments.UiAppointment

data class UiPatient(
    val id: Long = -1,
    val name: String = "",
    val surname: String = "",
    val phoneNumber: String = "",
    val appointments: MutableList<UiAppointment> = mutableListOf()
) {

    fun isNewPatient(): Boolean = id == -1L

}
