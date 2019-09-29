package com.sagrishin.ptsadm.common.api.interceptors

import android.content.Context
import com.sagrishin.ptsadm.common.showLoginPage
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor(private val context: Context): Interceptor {

    enum class NetworkErrorCode(val code: Int = -1) {
        FORBIDDEN(403),
        NOT_AUTHORIZED(401),
        INTERNAL_ERROR(500)
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        when (response.code()) {
            NetworkErrorCode.FORBIDDEN.code -> {
                context.showLoginPage()
            }
            NetworkErrorCode.NOT_AUTHORIZED.code -> {
                context.showLoginPage()
            }
            NetworkErrorCode.INTERNAL_ERROR.code -> {
                /// stubbed. this should be handled on ui as error execution
            }
        }
        return response
    }

}
