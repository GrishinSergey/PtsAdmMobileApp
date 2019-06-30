package com.sagrishin.ptsadm.patients.usecases

import androidx.lifecycle.LiveData
import com.sagrishin.ptsadm.common.api.ApiPatient
import com.sagrishin.ptsadm.common.api.toUiModel
import com.sagrishin.ptsadm.common.livedata.mutableLiveDataOf
import com.sagrishin.ptsadm.common.usecases.BaseUseCase
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.plusAssign

class PatientsUseCase(
    private val patientsRepository: PatientsRepository
) : BaseUseCase() {

    fun getAllPatients(): LiveData<List<UiPatient>> {
        val mediatorLiveData = mutableLiveDataOf<List<UiPatient>>()

        val patientsFromPhoneBook = patientsRepository.getLocalPatientsFromPhoneBook()
        compositeDisposable += patientsRepository.getAll()
            .zipWith<List<ApiPatient>, List<ApiPatient>>(patientsFromPhoneBook, BiFunction { t1, t2 ->
                (t1 + t2).distinctBy { it.phoneNumber }.sortedBy { (it.name + it.surname).trim() }
            })
            .map { it.map { it.toUiModel() } }
            .subscribe(mediatorLiveData::setValue)

        return mediatorLiveData
    }

    fun getRemotePatients(): LiveData<List<UiPatient>> {
        val mediatorLiveData = mutableLiveDataOf<List<UiPatient>>()

        compositeDisposable += patientsRepository.getAll()
            .map { it.sortedBy { (it.name + it.surname).trim() }.map { it.toUiModel() } }
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
