package org.graham.com.giftreminderfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.graham.com.giftreminderfirebase.models.Person
import java.util.*

class MainAdapter(val personList: ArrayList<Person>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        Log.d("Matt", "click...")
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        var holder: View = layoutInflater.inflate(R.layout.list_layout, parent, false)
        holder.setOnClickListener(mOnClickListener)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.title.text = personList[position].name
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<CardView>(R.id.card)
        var title = view.findViewById<TextView>(R.id.cardview_title) as TextView
    }
}