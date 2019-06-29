package com.sagrishin.ptsadm.common.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class ErrorBusLiveData<T: Any, E: Throwable> : MediatorLiveData<T>() {
    
    private lateinit var errorLiveData: LiveData<E>

    fun setError(t: E) {
        errorLiveData.value = t
    }

    fun onComplete(source: LiveData<T>, onChanged: Observer<T>): ErrorBusLiveData<T, E> {
        super.addSource(source, onChanged)
        return this
    }
    
    fun onError(onChanged: Observer<E>): ErrorBusLiveData<T, E> {
        super.addSource(errorLiveData, onChanged)
        return this
    }
    
}
