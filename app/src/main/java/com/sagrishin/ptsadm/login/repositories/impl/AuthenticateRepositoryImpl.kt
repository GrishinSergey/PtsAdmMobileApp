package com.sagrishin.ptsadm.login.repositories.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.sagrishin.ptsadm.common.api.ApiUser
import com.sagrishin.ptsadm.common.api.callSingle
import com.sagrishin.ptsadm.common.api.interceptors.TokenInterceptor
import com.sagrishin.ptsadm.common.api.retrySingleCall
import com.sagrishin.ptsadm.login.repositories.AuthenticateApiService
import com.sagrishin.ptsadm.login.repositories.AuthenticateRepository
import io.reactivex.Completable
import io.reactivex.Completable.fromSingle
import io.reactivex.Single

class AuthenticateRepositoryImpl(
    private val apiService: AuthenticateApiService,
    private val sharedPreferences: SharedPreferences
) : AuthenticateRepository {

    override fun updateToken(): Completable {
        return fromSingle(callSingle { apiService.updateToken().retrySingleCall() }.updateTokenInPrefs())
    }

    override fun register(enteredData: ApiUser): Single<ApiUser> {
        return callSingle { apiService.register(enteredData).retrySingleCall() }
    }

    override fun authorise(enteredData: ApiUser): Completable {
        return fromSingle(callSingle { apiService.authorise(enteredData).retrySingleCall() }.updateTokenInPrefs())
    }


    private fun Single<String>.updateTokenInPrefs(): Single<String> {
        return doOnSuccess { newToken -> sharedPreferences.edit { putString(TokenInterceptor.TOKEN_KEY, newToken) } }
    }

}
