package com.sagrishin.ptsadm.di

import com.sagrishin.ptsadm.appointments.repositories.AppointmentsApiService
import com.sagrishin.ptsadm.appointments.repositories.impl.AppointmentsRepositoryImpl
import com.sagrishin.ptsadm.appointments.repositories.AppointmentsRepository
import com.sagrishin.ptsadm.appointments.usecases.CalendarEventsUseCase
import com.sagrishin.ptsadm.appointments.viewmodels.AppointmentsCalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import retrofit2.Retrofit

val appointmentsModule = module {

    factory {
        val retrofit: Retrofit by inject()
        retrofit.create(AppointmentsApiService::class.java)
    }

    factoryBy<AppointmentsRepository, AppointmentsRepositoryImpl>()

    factory { CalendarEventsUseCase(get(), get()) }

    viewModel { AppointmentsCalendarViewModel(get()) }

}
