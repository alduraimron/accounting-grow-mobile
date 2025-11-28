package com.alduraimron.accountinggrow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AccountingGrowApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AccountingGrowApp
            private set
    }
}