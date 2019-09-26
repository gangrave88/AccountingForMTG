package com.example.accountingformtg

import android.app.Application
import com.example.accountingformtg.BD.ContactDB

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        App.contactDB = ContactDB.getInstance(this)

    }

    companion object {
        var contactDB: ContactDB? = null
    }
}