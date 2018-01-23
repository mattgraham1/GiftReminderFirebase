package org.graham.com.giftreminderfirebase.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.graham.com.giftreminderfirebase.R
import org.graham.com.giftreminderfirebase.models.Person

class MainAdapter() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val TAG = "MainAdapter"
    private var personList: List<Person> = emptyList()

    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        Log.d(TAG, "click...")
    }

    constructor(persons: List<Person>) : this() {
        this.personList = persons
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var holder: View = layoutInflater.inflate(R.layout.list_layout, parent, false)
        holder.setOnClickListener(mOnClickListener)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.title.text = personList[position].name
        holder!!.giftName.text = personList[position].gift.name
        holder!!.giftCost.text = personList[position].gift.cost
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    public fun setAdapterItems(persons: List<Person>) {
        this.personList = persons
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<CardView>(R.id.card)
        var title = view.findViewById<TextView>(R.id.cardview_title) as TextView
        var giftName = view.findViewById<TextView>(R.id.textView_giftname) as TextView
        var giftCost = view.findViewById<TextView>(R.id.textView_gift_cost) as TextView
    }
}