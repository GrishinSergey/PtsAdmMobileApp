package com.sagrishin.ptsadm.patients.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sagrishin.ptsadm.common.livedata.let
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.usecases.PatientsUseCase

class PatientsViewModel(
    private val patientsUseCase: PatientsUseCase
) : ViewModel() {

    val shownPatientsLiveData = MediatorLiveData<List<UiPatient>>()
    private val allPatientsLiveData = MutableLiveData<List<UiPatient>>()

    override fun onCleared() {
        patientsUseCase.clearSubscriptions()
    }

    fun loadPatients() {
        shownPatientsLiveData.addSource(patientsUseCase.getPatients()) {
            allPatientsLiveData.value = it
            shownPatientsLiveData.value = it
        }
    }

    fun filterPatientsBy(filterText: String) {
        shownPatientsLiveData.addSource(allPatientsLiveData.let { it.filter { it.isPatientMatchTo(filterText) } }) {
            shownPatientsLiveData.value = it
        }
    }

    fun deletePatient(patient: UiPatient) {
        shownPatientsLiveData.addSource(patientsUseCase.deletePatientBy(patient.id)) {
            allPatientsLiveData.value!!.toMutableList().apply {
                removeIf { it.id == patient.id }
                allPatientsLiveData.value = this
                shownPatientsLiveData.value = this
            }
        }
    }

    fun resetShownPatients() {
        shownPatientsLiveData.value = allPatientsLiveData.value
    }

    private fun UiPatient.isPatientMatchTo(text: String): Boolean {
        return name.contains(text, true) || surname.contains(text, true) || phoneNumber.contains(text, true)
    }

}
