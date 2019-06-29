package com.sagrishin.ptsadm.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.fragment.app.Fragment

fun Fragment.isOnline(): Boolean {
    val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return  activeNetwork?.isConnected == true
}