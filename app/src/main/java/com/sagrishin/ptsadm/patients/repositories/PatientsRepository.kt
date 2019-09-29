package com.sagrishin.ptsadm.patients.repositories

import com.sagrishin.ptsadm.common.api.ApiPatient
import io.reactivex.Completable
import io.reactivex.Single

interface PatientsRepository {

    fun getAll(): Single<List<ApiPatient>>

    fun savePatient(apiPatient: ApiPatient): Single<ApiPatient>

    fun deleteBy(id: Long): Completable

    fun getLocalPatientsFromPhoneBook(): Single<List<ApiPatient>>
}
