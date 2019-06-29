package com.sagrishin.ptsadm.patients.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sagrishin.ptsadm.common.api.toUiModel
import com.sagrishin.ptsadm.common.livedata.liveDataOf
import com.sagrishin.ptsadm.common.livedata.mutableLiveDataOf
import com.sagrishin.ptsadm.common.usecases.BaseUseCase
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.rxkotlin.plusAssign

class PatientsUseCase(
    private val patientsRepository: PatientsRepository
) : BaseUseCase() {

    fun getPatients(): LiveData<List<UiPatient>> {
        val mediatorLiveData = mutableLiveDataOf<List<UiPatient>>()

        compositeDisposable += patientsRepository.getAll()
            .map { it.map { it.toUiModel() } }
            .subscribe(mediatorLiveData::setValue)

        return mediatorLiveData
    }

    fun deletePatientBy(id: Long): LiveData<Boolean> {
        val liveData = mutableLiveDataOf<Boolean>()

        compositeDisposable += patientsRepository.deleteBy(id)
            .subscribe { result -> liveData.value = result }

        return liveData
    }

}
