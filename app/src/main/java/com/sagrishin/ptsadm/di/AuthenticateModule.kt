package com.sagrishin.ptsadm.di

import com.sagrishin.ptsadm.login.repositories.AuthenticateApiService
import com.sagrishin.ptsadm.login.repositories.impl.AuthenticateRepositoryImpl
import com.sagrishin.ptsadm.login.repositories.AuthenticateRepository
import com.sagrishin.ptsadm.login.usecases.AuthenticateUseCase
import com.sagrishin.ptsadm.login.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import retrofit2.Retrofit

val authModule = module {

    factory {
        val retrofit: Retrofit by inject()
        retrofit.create(AuthenticateApiService::class.java)
    }

    factoryBy<AuthenticateRepository, AuthenticateRepositoryImpl>()

    factory { AuthenticateUseCase(get()) }

    viewModel { AuthViewModel(get()) }

}
