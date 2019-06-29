package com.sagrishin.ptsadm.common.usecases

import io.reactivex.disposables.CompositeDisposable

abstract class BaseUseCase {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun clearSubscriptions() {
        compositeDisposable.clear()
    }

}