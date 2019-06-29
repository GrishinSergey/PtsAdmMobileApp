package com.sagrishin.ptsadm.patients.repositories.impl

import android.content.Context
import com.sagrishin.ptsadm.common.api.ApiPatient
import com.sagrishin.ptsadm.common.api.BaseRepository
import com.sagrishin.ptsadm.patients.repositories.PatientsApiService
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import ir.mirrajabi.rxcontacts.RxContacts

class PatientsRepositoryImpl(
    private val patientsApiService: PatientsApiService,
    private val context: Context
) : BaseRepository(), PatientsRepository {

    override fun getAll(): Single<List<ApiPatient>> {
        return callSingle { patientsApiService.getAllByCurrentDoctor() }
            .zipWith(getLocalPatientInPhoneBook(), BiFunction { t1, t2 ->
                (t1 + t2).distinctBy { it.phoneNumber }.sortedBy { (it.name + it.surname).trim() }
            })
    }

    override fun savePatient(apiPatient: ApiPatient): Single<ApiPatient> {
        return callSingle { patientsApiService.save(apiPatient) }
    }

    override fun deleteBy(id: Long): Single<Boolean> {
        return callSingle { patientsApiService.deleteBy(id) }
    }

    private fun getLocalPatientInPhoneBook(): Single<List<ApiPatient>> {
        return RxContacts.fetch(context)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .filter { it.phoneNumbers.isNotEmpty() }
            .map {
                ApiPatient(
                    name = it.displayName.split(" ").firstOrNull() ?: " ",
                    surname = it.displayName.split(" ").secondOrNull() ?: " ",
                    phoneNumber = it.phoneNumbers.firstOrNull() ?: "no number",
                    appointments = emptyList()
                )
            }.toList()
    }

    private fun <T> List<T>.secondOrNull(): T? = if (size < 2) null else this[1]

}
