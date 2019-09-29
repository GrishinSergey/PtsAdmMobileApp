package com.sagrishin.ptsadm.patients.repositories.impl

import android.content.Context
import com.sagrishin.ptsadm.common.api.ApiPatient
import com.sagrishin.ptsadm.common.api.callSingle
import com.sagrishin.ptsadm.patients.repositories.PatientsApiService
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.mirrajabi.rxcontacts.RxContacts

class PatientsRepositoryImpl(
    private val patientsApiService: PatientsApiService,
    private val context: Context
) : PatientsRepository {

    override fun getAll(): Single<List<ApiPatient>> {
        return callSingle { patientsApiService.getAllByCurrentDoctor() }
    }

    override fun savePatient(apiPatient: ApiPatient): Single<ApiPatient> {
        return callSingle { patientsApiService.save(apiPatient) }
    }

    override fun deleteBy(id: Long): Completable {
        return callSingle { patientsApiService.deleteBy(id) }.flatMapCompletable {
            if (it) Completable.complete() else Completable.error(Throwable(""))
        }
    }

    override fun getLocalPatientsFromPhoneBook(): Single<List<ApiPatient>> {
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
