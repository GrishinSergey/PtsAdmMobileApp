package com.sagrishin.ptsadm.di

import com.sagrishin.ptsadm.patients.repositories.PatientsApiService
import com.sagrishin.ptsadm.patients.repositories.PatientsRepository
import com.sagrishin.ptsadm.patients.repositories.impl.PatientsRepositoryImpl
import com.sagrishin.ptsadm.patients.usecases.PatientsUseCase
import com.sagrishin.ptsadm.patients.viewmodels.PatientsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import retrofit2.Retrofit

val patientsModule = module {

    factory {
        val retrofit: Retrofit by inject()
        retrofit.create(PatientsApiService::class.java)
    }

    factoryBy<PatientsRepository, PatientsRepositoryImpl>()

    factory { PatientsUseCase(get()) }

    viewModel { PatientsViewModel(get()) }

}
