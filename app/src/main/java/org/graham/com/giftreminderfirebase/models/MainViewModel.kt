package org.graham.com.giftreminderfirebase.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.graham.com.giftreminderfirebase.Constants

class MainViewModel : ViewModel() {
    val TAG: String = "MainViewModel"
    var persons: MutableLiveData<List<Person>> = MutableLiveData()

    fun getUsers(userUid: String): LiveData<List<Person>> {
        FirebaseDatabase.getInstance().getReference("users").child(userUid).child(Constants.CONTACTS)
                .addListenerForSingleValueEvent(object: ValueEventListener {

                    override fun onCancelled(error: DatabaseError?) {
                        Log.e( TAG,"error: " + error?.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        persons.postValue(toUsers(snapshot))
                    }
                })

        return persons
    }

    fun toUsers(snapshot: DataSnapshot) : ArrayList<Person> {
        val users = ArrayList<Person>()
        val children = snapshot.children

        children?.forEach {
            val person: Person = it.getValue(Person::class.java) as Person
            users.add(person)
        }
        return users
    }

}