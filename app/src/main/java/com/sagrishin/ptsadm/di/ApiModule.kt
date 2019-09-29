package com.sagrishin.ptsadm.di

import com.google.gson.GsonBuilder
import com.sagrishin.ptsadm.common.api.interceptors.ErrorInterceptor
import com.sagrishin.ptsadm.common.api.interceptors.TokenInterceptor
import com.sagrishin.ptsadm.common.api.serializers.DateTimeAdapter
import com.sagrishin.ptsadm.common.api.serializers.LocalDateTimeAdapter
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val apiModule = module {

    single {
        Retrofit.Builder().apply {
            baseUrl("https://ptsadmapp.herokuapp.com/")
            client(get())
            addConverterFactory(ScalarsConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create(get()))
        }.build()
    }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(TokenInterceptor(get()))
            addInterceptor(ErrorInterceptor(get()))
        }.build()
    }

    single {
        GsonBuilder().apply {
            registerTypeAdapter(DateTime::class.java, DateTimeAdapter())
            registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        }.create()
    }

}
