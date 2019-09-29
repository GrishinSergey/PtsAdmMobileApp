package com.sagrishin.ptsadm.common

import android.content.Context
import android.content.Intent

const val SHOW_LOGIN_PARAM_KEY = "showLoginPage"

fun Context.showLoginPage() {
    startActivity(Intent().apply {
        action = Intent.ACTION_MAIN
        putExtra(SHOW_LOGIN_PARAM_KEY, true)
    })
}