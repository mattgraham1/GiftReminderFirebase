package org.graham.com.giftreminderfirebase.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.graham.com.giftreminderfirebase.Constants
import org.graham.com.giftreminderfirebase.R
import org.graham.com.giftreminderfirebase.models.Person

class MainAdapter() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val TAG = "MainAdapter"
    private var personList: List<Person> = emptyList()

    constructor(persons: List<Person>) : this() {
        this.personList = persons
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val holder: View = layoutInflater.inflate(R.layout.list_layout, parent, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.title.text = personList[position].name
        holder!!.giftName.text = personList[position].gift.name
        holder!!.giftCost.text = personList[position].gift.cost

        holder.card.setOnLongClickListener({ v: View ->
            Log.e(TAG, "card long click...")
            val userUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            true
        })

        holder.delete.setOnClickListener({v: View ->
            val userUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            if (!userUid.isEmpty() && !personList.get(position).key.isEmpty()) {
                FirebaseDatabase.getInstance().getReference(Constants.USERS).child(userUid).child(Constants.CONTACTS)
                        .child(personList.get(position).key).setValue(null)
            }
            else {
                Log.e(TAG, "Failed to remove item from firebase database.")
            }
        })
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    fun setAdapterItems(persons: List<Person>) {
        this.personList = persons
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card = view.findViewById<CardView>(R.id.card) as CardView
        var title = view.findViewById<TextView>(R.id.cardview_title) as TextView
        var giftName = view.findViewById<TextView>(R.id.textView_giftname) as TextView
        var giftCost = view.findViewById<TextView>(R.id.textView_gift_cost) as TextView
        var delete = view.findViewById<ImageButton>(R.id.imageButton_delete) as ImageButton
    }
}