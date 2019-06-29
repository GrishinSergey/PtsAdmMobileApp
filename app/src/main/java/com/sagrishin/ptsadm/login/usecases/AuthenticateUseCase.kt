package com.sagrishin.ptsadm.login.usecases

import androidx.lifecycle.LiveData
import com.sagrishin.ptsadm.login.UiUser
import com.sagrishin.ptsadm.common.api.toApiModel
import com.sagrishin.ptsadm.common.livedata.liveDataOf
import com.sagrishin.ptsadm.common.livedata.mutableLiveDataOf
import com.sagrishin.ptsadm.common.usecases.BaseUseCase
import com.sagrishin.ptsadm.login.repositories.AuthenticateRepository
import io.reactivex.rxkotlin.plusAssign

class AuthenticateUseCase(
    private val repository: AuthenticateRepository
): BaseUseCase() {

    fun updateToken(): LiveData<String?> {
        val liveDataResult = mutableLiveDataOf<String?>()
        compositeDisposable += repository.updateToken().doOnSuccess {
            liveDataResult.value = it
        }.doOnError {
            liveDataResult.value = null
        }.subscribe()
        return liveDataResult
    }

    fun login(enteredData: UiUser): LiveData<String?> {
        val liveDataResult = mutableLiveDataOf<String?>()
        compositeDisposable += repository.authorise(enteredData.toApiModel()).doOnSuccess { newToken ->
            liveDataResult.value = newToken
        }.doOnError {
            liveDataResult.value = null
        }.subscribe()
        return liveDataResult
    }

    fun register(enteredData: UiUser): LiveData<UiUser?> {
        return liveDataOf()
    }

}
