package com.example.accountingformtg.BD.Model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class ContactInfo(

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    val name: String,
    val phone: String = "",
    val money: Float = 0f
)