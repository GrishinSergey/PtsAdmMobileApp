package com.sagrishin.ptsadm.patients.usecases

import com.sagrishin.ptsadm.common.api.ApiPatient
import com.sagrishin.ptsadm.common.api.toUiModel
import com.sagrishin.ptsadm.patients.UiPatient
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.Completable
import io.reactivex.Single

class PatientsUseCase(private val patientsRepository: PatientsRepository) {

    /**
     * For tests and showing demos logic with selecting users from user contacts was commented.
     */
//    fun getAllPatients(): LiveData<List<UiPatient>> {
//        val mediatorLiveData = mutableLiveDataOf<List<UiPatient>>()
//
//        val patientsFromPhoneBook = patientsRepository.getLocalPatientsFromPhoneBook()
//        compositeDisposable += patientsRepository.getAll()
//            .zipWith<List<ApiPatient>, List<ApiPatient>>(patientsFromPhoneBook, BiFunction { t1, t2 ->
//                (t1 + t2).distinctBy { it.phoneNumber }.sortedBy { (it.name + it.surname).trim() }
//            })
//            .map { it.map(ApiPatient::toUiModel) }
//            .subscribe(mediatorLiveData::setValue)
//
//        return mediatorLiveData
//    }

    fun getAllPatients(): Single<List<UiPatient>> {
        return patientsRepository.getAll().map { it.map(ApiPatient::toUiModel) }
    }

    fun getRemotePatients(): Single<List<UiPatient>> {
        return patientsRepository.getAll()
            .map { it.sortedBy { (it.name + it.surname).trim() }.map(ApiPatient::toUiModel) }
    }

    fun deletePatientBy(id: Long): Completable {
        return patientsRepository.deleteBy(id)
    }

}
