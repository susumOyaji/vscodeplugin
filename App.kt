package com.example.androidphnex

import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import timber.log.Timber

class App : Application() {
    fun onCreate() {
        super.onCreate()
        // Log
        Timber.plant(DebugTree())
    }
}