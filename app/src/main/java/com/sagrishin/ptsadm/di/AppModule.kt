package com.sagrishin.ptsadm.di

import android.content.Context
import android.content.SharedPreferences
import com.sagrishin.ptsadm.common.api.interceptors.TokenInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        androidContext().getSharedPreferences(androidContext().packageName, Context.MODE_PRIVATE)
    }

}
