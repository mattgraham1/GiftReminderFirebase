package org.graham.com.giftreminderfirebase

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import kotlinx.android.synthetic.main.activity_main.*
import org.graham.com.giftreminderfirebase.adapters.MainAdapter
import org.graham.com.giftreminderfirebase.models.MainViewModel
import org.graham.com.giftreminderfirebase.models.Person


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private var userUid: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: MainAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = MainViewModel()

        userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.main_swipe_refresh)
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.secondaryColor)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Log.e(TAG, "swipe refresh called.")
            mainViewModel.getUsers(userUid).observe(this, Observer { users ->
                users?.let {
                    recyclerViewAdapter.setAdapterItems(it)
                    swipeRefreshLayout.isRefreshing = false
                }
            })
        })

        recyclerView = findViewById(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        recyclerViewAdapter = MainAdapter(ArrayList<Person>())
        recyclerView.adapter = recyclerViewAdapter

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getUsers(userUid).observe(this, Observer { users ->
            users?.let {
                recyclerViewAdapter.setAdapterItems(it)
            }
        })

        fab.setOnClickListener { view ->
            val addUserIntent = Intent(this, AddUserActivity::class.java)
            startActivity(addUserIntent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
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
            startActivity(Intent(this, LoginActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
