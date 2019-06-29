package com.sagrishin.ptsadm.patients.repositories

import com.sagrishin.ptsadm.common.api.ApiPatient
import io.reactivex.Single
import io.reactivex.disposables.Disposable

interface PatientsRepository {

    fun getAll(): Single<List<ApiPatient>>

    fun savePatient(apiPatient: ApiPatient): Single<ApiPatient>

    fun deleteBy(id: Long): Single<Boolean>

}
