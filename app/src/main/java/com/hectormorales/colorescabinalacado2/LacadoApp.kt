package com.hectormorales.colorescabinalacado2

import android.app.Application
import com.hectormorales.colorescabinalacado2.di.appModule
import com.hectormorales.colorescabinalacado2.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class LacadoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LacadoApp)
            modules(appModule, viewModelModule)
        }
    }
}