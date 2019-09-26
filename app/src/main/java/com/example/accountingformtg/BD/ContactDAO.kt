package com.example.accountingformtg.BD

import android.arch.persistence.room.*
import com.example.accountingformtg.BD.Model.ContactInfo
import com.example.accountingformtg.BD.Request.MainListItem
import io.reactivex.Flowable

@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContact(contactInfo: ContactInfo)

    @Update
    fun updateContact(contactInfo: ContactInfo)

    @Delete
    fun deleteContact(contactInfo: ContactInfo)

    @Query("SELECT id, name, phone, money, 0 as visible FROM ContactInfo")
    fun getContacts(): Flowable<List<MainListItem>>

}