package com.sagrishin.ptsadm

import android.app.Application
import android.util.Log
import com.sagrishin.ptsadm.di.*
import com.snakydesign.watchtower.WatchTower
import com.snakydesign.watchtower.interceptor.WebWatchTowerObserver
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
            ).androidContext(applicationContext)
        }

        WatchTower.start(WebWatchTowerObserver(port = 8085))

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

    }

}
