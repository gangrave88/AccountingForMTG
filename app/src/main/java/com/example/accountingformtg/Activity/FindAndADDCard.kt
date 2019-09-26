package com.example.accountingformtg.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.example.accountingformtg.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_find_add_card.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FindAndADDCard:AppCompatActivity() {

    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_add_card)

        disposable.add(
            RxTextView.textChanges(search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { text -> text.length > 3 }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                        text -> run{
                    val list = MtgCardApiClient.getCardsByExactName(text.toString(),10,1).body()
                    if (list != null) {
                        val listCard = arrayListOf<String>()
                        for (card:MtgCard in list){
                            listCard.add(card.name)
                        }

                        search.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listCard))
                    }
                }
            }
        )
    }
}