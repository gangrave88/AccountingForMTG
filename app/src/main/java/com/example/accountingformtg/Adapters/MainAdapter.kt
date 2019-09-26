package com.example.accountingformtg.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.accountingformtg.BD.Model.ContactInfo
import com.example.accountingformtg.BD.Request.MainListItem
import com.example.accountingformtg.R
import kotlinx.android.synthetic.main.main_list_item.view.*

class MainAdapter(private val items: List<MainListItem>, private val context: Context): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.main_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemList = items[position]

        holder.bind(itemList)

        holder.itemView.setOnClickListener {
            val currentState = itemList.visible
            itemList.visible = !currentState
            notifyItemChanged(position)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bind(mainListItem: MainListItem){
            itemView.name.text = mainListItem.name
            itemView.summ.text = mainListItem.money.toString()
            itemView.phone.text = mainListItem.phone

            when(mainListItem.visible){
                true -> itemView.detail.visibility = View.VISIBLE
                false -> itemView.detail.visibility = View.GONE
            }

            var oweMe = 0
            var iShould = 0
            var oweMeList = ""
            var iShouldList = ""

            mainListItem.cardList.forEach {
                when(it.oweMe){
                    true -> {
                        oweMe += it.count
                        oweMeList += it.name + " " + it.count.toString()
                    }
                    false -> {
                        iShould += it.count
                        iShouldList += it.name + " " + it.count.toString()
                    }
                }
            }

            itemView.owe_me.text = oweMe.toString()
            itemView.i_should.text = iShould.toString()
            itemView.owe_me_list.text = Editable.Factory.getInstance().newEditable(oweMeList)
            itemView.i_should_list.text = Editable.Factory.getInstance().newEditable(iShouldList)
        }
    }

}
