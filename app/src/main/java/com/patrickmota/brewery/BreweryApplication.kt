package com.patrickmota.brewery

import android.app.Application
import com.patrickmota.brewery.di.KoinModules.networkModule
import com.patrickmota.brewery.di.KoinModules.repositoryModule
import com.patrickmota.brewery.di.KoinModules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BreweryApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BreweryApplication)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }
}