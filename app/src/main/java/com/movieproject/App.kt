package com.movieproject

import android.app.Application
import com.movieproject.di.AppComponent
import com.movieproject.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.create()
    }
}