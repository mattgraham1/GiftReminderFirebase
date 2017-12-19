package org.graham.com.giftreminderfirebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import org.graham.com.giftreminderfirebase.models.Gift
import org.graham.com.giftreminderfirebase.models.Person

class AddUserActivity : AppCompatActivity() {

    var userUid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val userName = findViewById(R.id.edittext_name) as EditText
        val reminderDate = findViewById(R.id.edittext_reminder_date) as EditText
        val giftCost = findViewById(R.id.edittext_cost) as EditText
        val giftDescription = findViewById(R.id.edittext_gift_description) as EditText

        val submitButton = findViewById<Button>(R.id.button_submit)
        submitButton.setOnClickListener { view ->
            // send to firebase.
            val gift = Gift(giftDescription.text.toString(), giftCost.text.toString(), "08/30/75")
            val person = Person(userName.text.toString(), reminderDate.text.toString(), gift)

            if(!userUid.isNullOrEmpty()) {
                FirebaseDatabase.getInstance().getReference("users").child(userUid).child("contacts")
                        .push().setValue(person)
            } else {
                Log.e("Matt", "Error user UID is empty or null.")
            }
        }

        if(intent.hasExtra(Constants.USER_UID)) {
            userUid = intent.getStringExtra(Constants.USER_UID)
            Log.e("Matt", "userUid: " + userUid)
        }
    }
}