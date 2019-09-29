package com.sagrishin.ptsadm

import android.app.Application
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.sagrishin.ptsadm.di.*
import io.fabric.sdk.android.Fabric
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.IOException

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        startKoin {
            modules(
                appModule,
                apiModule,
                appointmentsModule,
                authModule,
                patientsModule
            )
            androidContext(applicationContext)
        }

        RxJavaPlugins.setErrorHandler {
            val e = if (it is UndeliverableException) it.cause!! else it
            if (e is IOException) {
                // fine, irrelevant network problem or API that throws on cancellation
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e)
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e)
            }

            Log.e("RxJava", e.message, e)
        }

        Fabric.with(this, Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build())
    }

}
