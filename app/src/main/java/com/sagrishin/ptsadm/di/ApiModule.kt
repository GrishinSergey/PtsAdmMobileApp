package com.sagrishin.ptsadm.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.sagrishin.ptsadm.common.api.interceptors.TokenInterceptor
import com.sagrishin.ptsadm.common.api.serializers.DateTimeAdapter
import com.sagrishin.ptsadm.common.api.serializers.LocalDateTimeAdapter
import com.snakydesign.watchtower.interceptor.WatchTowerInterceptor
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.ISODateTimeFormat
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val apiModule = module {

    single {
        Retrofit.Builder().baseUrl("https://ptsadmapp.herokuapp.com/")
            .client(get())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(get()))
            .addInterceptor(WatchTowerInterceptor())
            .build()
    }

    single {
        GsonBuilder().apply {
            registerTypeAdapter(DateTime::class.java, DateTimeAdapter())
            registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        }.create()
    }

}
