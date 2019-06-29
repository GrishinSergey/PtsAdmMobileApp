package com.sagrishin.ptsadm.login.repositories.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.sagrishin.ptsadm.common.api.ApiUser
import com.sagrishin.ptsadm.common.api.BaseRepository
import com.sagrishin.ptsadm.common.api.interceptors.TokenInterceptor
import com.sagrishin.ptsadm.login.repositories.AuthenticateApiService
import com.sagrishin.ptsadm.login.repositories.AuthenticateRepository
import io.reactivex.Single
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class AuthenticateRepositoryImpl(
    private val apiService: AuthenticateApiService,
    private val sharedPreferences: SharedPreferences
) : BaseRepository(), AuthenticateRepository {

    override fun updateToken(): Single<String> {
        return callSingle {
            apiService.updateToken()
                .timeout(60, TimeUnit.SECONDS)
                .retry { count, error -> (error is SocketTimeoutException) && (count <= 3) }
        }.doOnSuccess { newToken ->
            sharedPreferences.edit { putString(TokenInterceptor.TOKEN_KEY, newToken) }
        }
    }

    override fun register(enteredData: ApiUser): Single<ApiUser> {
        return callSingle { apiService.register(enteredData)
            .timeout(60, TimeUnit.SECONDS)
            .retry { count, error -> (error is SocketTimeoutException) && (count <= 3) }
        }
    }

    override fun authorise(enteredData: ApiUser): Single<String> {
        return callSingle {
            apiService.authorise(enteredData)
                .timeout(60, TimeUnit.SECONDS)
                .retry { count, error -> (error is SocketTimeoutException) && (count <= 3) }
        }.doOnSuccess { newToken ->
            sharedPreferences.edit { putString(TokenInterceptor.TOKEN_KEY, newToken) }
        }
    }

}
