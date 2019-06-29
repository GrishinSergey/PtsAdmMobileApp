package com.sagrishin.ptsadm.common.api

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

data class ApiPatient(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("appointments")
    val appointments: List<ApiAppointment> = emptyList()
)


data class ApiAppointment(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("dateTime")
    val dateTime: LocalDateTime,
    @SerializedName("patientId")
    val patientId: Long? = null
)


data class ApiUser(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String
)


data class ApiDay(
    @SerializedName("date")
    val date: DateTime,
    @SerializedName("patients")
    val patients: List<ApiPatient>
)
