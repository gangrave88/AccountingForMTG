package com.example.accountingformtg.BD

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.accountingformtg.BD.Model.CardInfo
import com.example.accountingformtg.BD.Model.ContactInfo

@Database(entities = arrayOf(ContactInfo::class, CardInfo::class), version = 6)
abstract class ContactDB : RoomDatabase() {

    abstract fun ContactDAO(): ContactDAO

    companion object {

        private var INSTANCE : ContactDB? = null

        fun getInstance(context: Context): ContactDB? {

            if (INSTANCE == null){
                synchronized(ContactDB::class){
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, ContactDB::class.java, "contact.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE

        }

        fun destroyInstance(){
            INSTANCE = null
        }

    }

}