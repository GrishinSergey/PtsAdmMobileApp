package com.sagrishin.ptsadm.common.api.interceptors

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor(
    private val sharedManager: SharedPreferences
) : Interceptor {

    companion object {
        const val TOKEN_KEY = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedManager.getString(TOKEN_KEY, "") ?: ""
        val request = chain.request()
            .newBuilder()
            .addHeader(TOKEN_KEY, "Bearer $token")
            .build()
        return chain.proceed(request)
    }

}
