package org.graham.com.giftreminderfirebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import org.graham.com.giftreminderfirebase.R
import org.graham.com.giftreminderfirebase.models.Gift
import org.graham.com.giftreminderfirebase.models.Person

class AddUserActivity : AppCompatActivity() {

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

            FirebaseDatabase.getInstance().getReference("users").push()
                    .setPriority(person)
        }
    }

}