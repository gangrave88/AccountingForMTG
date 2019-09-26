package com.example.accountingformtg.BD.Model

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(
    tableName = "CardInfo",
    indices = arrayOf(Index(value = ["idContact"], name = "idx")),
    foreignKeys = arrayOf(ForeignKey(
        entity = ContactInfo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("idContact"),
        onDelete = CASCADE
)))
data class CardInfo(

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    val idContact: Int,
    val name: String,
    val count: Int,
    val oweMe: Boolean,
    val comment: String = "",
    val pic: String = ""
)