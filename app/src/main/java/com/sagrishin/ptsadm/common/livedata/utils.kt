package com.sagrishin.ptsadm.common.livedata

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap

fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> = MutableLiveData(value!!)

fun <T> T.toLiveData(): LiveData<T> = MutableLiveData(this)

fun <T> liveDataOf(t: T): LiveData<T> = MutableLiveData(t)

fun <T> liveDataOf(): LiveData<T> = MutableLiveData()

fun <T> mutableLiveDataOf(t: T): MutableLiveData<T> = MutableLiveData(t)

fun <T> mutableLiveDataOf(): MutableLiveData<T> = MutableLiveData()

fun <T, F> LiveData<T>.flatMap(mapper: (T) -> LiveData<F>): LiveData<F> {
    return switchMap(this, Function(mapper))
}

fun <T, F> LiveData<T>.let(mapper: (T) -> F): LiveData<F> {
    return Transformations.map(this, mapper)
}

fun <T, F> MutableLiveData<T>.let(mapper: (T) -> F): MutableLiveData<F> {
    return Transformations.map(this, mapper) as MutableLiveData<F>
}

fun <T> LiveData<List<T>>.filter(predicate: (T) -> Boolean): LiveData<List<T>> {
    return Transformations.map(this) { it.filter(predicate) }
}

fun <T, F> LiveData<List<T>>.map(mapper: (T) -> F): LiveData<List<F>> {
    return Transformations.map(this) { it.map(mapper) }
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, l: (T) -> Unit) {
    observe(owner, Observer(l))
}
