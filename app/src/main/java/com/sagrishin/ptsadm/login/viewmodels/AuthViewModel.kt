package com.sagrishin.ptsadm.login.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.sagrishin.ptsadm.login.UiUser
import com.sagrishin.ptsadm.login.usecases.AuthenticateUseCase

class AuthViewModel(
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {

    val tokenUpdateLiveData: MediatorLiveData<String?> = MediatorLiveData()

    fun updateToken() {
        tokenUpdateLiveData.addSource(authenticateUseCase.updateToken(), tokenUpdateLiveData::setValue)
    }

    fun loginUser(enteredData: UiUser) {
        tokenUpdateLiveData.addSource(authenticateUseCase.login(enteredData), tokenUpdateLiveData::setValue)
    }

    override fun onCleared() {
        authenticateUseCase.clearSubscriptions()
    }

}
