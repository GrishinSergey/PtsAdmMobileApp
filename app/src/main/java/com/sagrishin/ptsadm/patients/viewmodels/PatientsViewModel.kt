package com.sagrishin.ptsadm.patients.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sagrishin.ptsadm.common.livedata.let
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.usecases.PatientsUseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class PatientsViewModel(private val patientsUseCase: PatientsUseCase) : ViewModel() {

    val shownPatientsLiveData: LiveData<List<UiPatient>>
        get() = _shownPatientsLiveData
    private val _shownPatientsLiveData = MediatorLiveData<List<UiPatient>>()
    private val allPatientsLiveData = MutableLiveData<List<UiPatient>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadRemotePatients() {
        compositeDisposable += patientsUseCase.getRemotePatients().doOnSuccess().doOnError {

        }.subscribe()
    }

    fun loadAllPatients() {
        compositeDisposable += patientsUseCase.getAllPatients().doOnSuccess().doOnError {

        }.subscribe()
    }

    fun filterPatientsBy(filterText: String) {
        _shownPatientsLiveData.addSource(allPatientsLiveData.let { it.filter { it.isPatientMatchTo(filterText) } }) {
            _shownPatientsLiveData.value = it
        }
    }

    fun deletePatient(patient: UiPatient) {
        compositeDisposable += patientsUseCase.deletePatientBy(patient.id).doOnComplete {
            allPatientsLiveData.value!!.toMutableList().apply {
                removeIf { it.id == patient.id }
                allPatientsLiveData.value = this
                _shownPatientsLiveData.value = this
            }
        }.doOnError {

        }.subscribe()
    }

    fun resetShownPatients() {
        _shownPatientsLiveData.value = allPatientsLiveData.value
    }

    private fun Single<List<UiPatient>>.doOnSuccess(): Single<List<UiPatient>> {
        return doOnSuccess {
            allPatientsLiveData.value = it
            _shownPatientsLiveData.value = it
        }
    }

    private fun UiPatient.isPatientMatchTo(text: String): Boolean {
        return name.contains(text, true) || surname.contains(text, true) || phoneNumber.contains(text, true)
    }

}
