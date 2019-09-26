package com.example.accountingformtg.BD.Request

import android.arch.persistence.room.Relation
import com.example.accountingformtg.BD.Model.CardInfo

class MainListItem {

    var id: Int = 0
    var name: String = ""
    var phone: String = ""
    var money: Float = 0f
    var visible: Boolean = false

    @Relation(parentColumn = "id", entityColumn = "idContact", entity = CardInfo::class)
    var cardList: List<CardInfo> = emptyList()

}
