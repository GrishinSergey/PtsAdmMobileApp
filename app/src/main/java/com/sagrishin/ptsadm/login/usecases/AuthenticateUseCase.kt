package com.sagrishin.ptsadm.login.usecases

import com.sagrishin.ptsadm.common.api.toApiModel
import com.sagrishin.ptsadm.login.UiUser
import com.sagrishin.ptsadm.login.repositories.AuthenticateRepository
import io.reactivex.Completable
import io.reactivex.Single

class AuthenticateUseCase(
    private val repository: AuthenticateRepository
) {

    fun updateToken(): Completable {
        return repository.updateToken()
    }

    fun login(enteredData: UiUser): Completable {
        return repository.authorise(enteredData.toApiModel())
    }

    fun register(enteredData: UiUser): Single<UiUser> {
        return Single.just(UiUser("", "", ""))
    }

}
