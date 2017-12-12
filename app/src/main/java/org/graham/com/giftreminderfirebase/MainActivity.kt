package org.graham.com.giftreminderfirebase

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.graham.com.giftreminderfirebase.adapters.MainAdapter
import org.graham.com.giftreminderfirebase.models.Person


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        var recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        initRecyclerView(recyclerView)

        fab.setOnClickListener { view ->
            startActivity(Intent(this, AddUserActivity::class.java))
            Snackbar.make(view, "Show Adding User", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_login) {
            startActivity(Intent(this, LoginActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        val persons = ArrayList<Person>()

        FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onCancelled(error: DatabaseError?) {
                println("error: " + error!!.message)
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val children = snapshot!!.children

                children.forEach {
                    val person: Person = it.getValue(Person::class.java) as Person
                    persons.add(person)
                }

                val adapter = MainAdapter(persons)
                recyclerView.adapter = adapter
            }
        })
    }
}
