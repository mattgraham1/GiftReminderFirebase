package org.graham.com.giftreminderfirebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.graham.com.giftreminderfirebase.models.Gift
import org.graham.com.giftreminderfirebase.models.Person

class AddUserActivity : AppCompatActivity() {

    private val TAG: String = "AddUserActivity"
    private var userUid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val userName = findViewById<EditText>(R.id.edittext_name)
        val reminderDate = findViewById<EditText>(R.id.edittext_reminder_date)
        val giftCost = findViewById<EditText>(R.id.edittext_cost)
        val giftDescription = findViewById<EditText>(R.id.edittext_gift_description)

        userName.onTextChanged { it }
        reminderDate.onTextChanged { it }
        giftCost.onTextChanged { it }
        giftDescription.onTextChanged { it }

        val submitButton = findViewById<Button>(R.id.button_submit)
        submitButton.setOnClickListener { view ->

            //TODO: Update date of purchase
            val gift = Gift(giftDescription.text.toString(), giftCost.text.toString(), "08/30/75")
            val person = Person(userName.text.toString(), reminderDate.text.toString(), gift)

            if(!userUid.isNullOrEmpty()) {
                FirebaseDatabase.getInstance().getReference("users").child(userUid)
                        .child(Constants.CONTACTS).push().setValue(person)
                launchMainActivity()
            } else {
                Log.e(TAG, "Error user UID is empty or null.")
            }
        }

        userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun launchMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    fun EditText.onTextChanged(cb: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (cb.toString().isNotEmpty())
                    error = null
            }
        })
    }
}