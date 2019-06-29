package com.sagrishin.ptsadm.common.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

open class CompositeLiveData<T : Any> : MediatorLiveData<T>() {

    lateinit var tempValue: T
    private var currentCounter = 0
    private var allCounter = 0

    override fun setValue(value: T) {
        tempValue = value
    }

    override fun <S : Any> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        currentCounter++
        allCounter++
        super.addSource(source) {
            onChanged.onChanged(it)
            synchronized(this) {
                currentCounter--
                if (currentCounter == 0) {
                    onComplete()
                    currentCounter = allCounter
                }
            }
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        super.removeObservers(owner)
        currentCounter--
        allCounter--
    }

    fun onComplete() {
        super.setValue(tempValue)
    }

}
