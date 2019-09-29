package com.sagrishin.ptsadm.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sagrishin.ptsadm.common.livedata.ActionLiveData
import com.sagrishin.ptsadm.login.UiUser
import com.sagrishin.ptsadm.login.usecases.AuthenticateUseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

class AuthViewModel(private val authenticateUseCase: AuthenticateUseCase) : ViewModel() {

    val tokenUpdateLiveData: LiveData<Unit> get() = _tokenUpdateLiveData
    val errorLiveData: LiveData<Throwable> get() = _errorLiveData
    private val _tokenUpdateLiveData: ActionLiveData<Unit> = ActionLiveData()
    private val _errorLiveData: ActionLiveData<Throwable> = ActionLiveData()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun updateToken() {
        compositeDisposable += authenticateUseCase.updateToken().handleTokenActions()
    }

    fun loginUser(enteredData: UiUser) {
        compositeDisposable += authenticateUseCase.login(enteredData).handleTokenActions()
    }


    private fun Completable.handleTokenActions(): Disposable {
        return doOnComplete {
            _tokenUpdateLiveData.value = Unit
        }.doOnError {
            _errorLiveData.value = it
        }.subscribe()
    }

}
