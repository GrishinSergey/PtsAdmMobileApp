package com.sagrishin.ptsadm.login

data class UiUser(
    val login: String,
    val password: String,
    val confirmPassword: String = password
)
