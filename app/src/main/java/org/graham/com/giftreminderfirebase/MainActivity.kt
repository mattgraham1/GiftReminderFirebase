package org.graham.com.giftreminderfirebase

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.graham.com.giftreminderfirebase.adapters.MainAdapter
import org.graham.com.giftreminderfirebase.models.Person


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private var userUid: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        recyclerView = findViewById(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fab.setOnClickListener { view ->
            val addUserIntent = Intent(this, AddUserActivity::class.java)
            startActivity(addUserIntent)
        }

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.main_swipe_refresh)
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.secondaryColor)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Log.e(TAG, "swipe refresh called.")
            initRecyclerView(recyclerView)
        })

        initRecyclerView(recyclerView)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (!userUid.isNullOrEmpty()) {
            initRecyclerView(recyclerView)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(Constants.USER_UID, userUid)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_login) {
            startActivity(Intent(this, LoginActivity::class.java))
            return true
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {

        if(!userUid.isNullOrEmpty() && !userUid.contains("null", true)) {
            swipeRefreshLayout.isRefreshing = true

            val persons = ArrayList<Person>()

            FirebaseDatabase.getInstance().getReference("users").child(userUid).child(Constants.CONTACTS)
                    .addListenerForSingleValueEvent(object: ValueEventListener {

                        override fun onCancelled(error: DatabaseError?) {
                           Log.e( TAG,"error: " + error!!.message)
                        }

                        override fun onDataChange(snapshot: DataSnapshot?) {
                            val children = snapshot!!.children

                            children.forEach {
                                val person: Person = it.getValue(Person::class.java) as Person
                                persons.add(person)
                            }

                            val adapter = MainAdapter(persons)
                            recyclerView.adapter = adapter
                            recyclerView.adapter.notifyDataSetChanged()
                        }
                    })
            swipeRefreshLayout.isRefreshing = false
        } else {
            Log.e(TAG, "Error user UID is empty or null.")
        }
    }
}
