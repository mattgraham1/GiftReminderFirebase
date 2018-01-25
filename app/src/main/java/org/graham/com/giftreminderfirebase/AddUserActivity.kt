package org.graham.com.giftreminderfirebase

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
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
import java.text.SimpleDateFormat
import java.util.*

class AddUserActivity : AppCompatActivity() {

    private val TAG: String = "AddUserActivity"
    private var userUid: String = ""

    private lateinit var userName: EditText
    private lateinit var userNameTextInput: TextInputLayout
    private lateinit var giftCost: EditText
    private lateinit var giftCostTextInput: TextInputLayout
    private lateinit var giftDescription: EditText
    private lateinit var giftDescriptionTextInput: TextInputLayout
    private lateinit var reminderDate: EditText
    private lateinit var reminderDateTextInput: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        userName = findViewById<EditText>(R.id.edittext_name)
        userNameTextInput = findViewById<TextInputLayout>(R.id.textInputLayout_user_name)
        userName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    userNameTextInput.error = null
                }
            }
        })
        reminderDate = findViewById<EditText>(R.id.edittext_reminder_date)
        reminderDateTextInput = findViewById<TextInputLayout>(R.id.textInputLayout_reminder_date)
        reminderDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    reminderDateTextInput.error = null
                }
            }
        })
        giftCost = findViewById<EditText>(R.id.edittext_cost)
        giftCostTextInput = findViewById<TextInputLayout>(R.id.textInputLayout_gift_cost)
        giftCost.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    giftCostTextInput.error = null
                }
            }
        })
        giftDescription = findViewById<EditText>(R.id.edittext_gift_description)
        giftDescriptionTextInput = findViewById<TextInputLayout>(R.id.textInputLayout_gift_description)
        giftDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    giftDescriptionTextInput.error = null
                }
            }
        })


        val submitButton = findViewById<Button>(R.id.button_submit)
        submitButton.setOnClickListener { view ->
            // Check if user entered proper fields
            if (userName.text.isBlank()) {
                userNameTextInput.error = "Error, please enter name."
                return@setOnClickListener
            } else if (reminderDate.text.isBlank()) {
                reminderDateTextInput.error = "Error, please enter reminder date."
                return@setOnClickListener
            } else if (giftDescription.text.isBlank()) {
                giftDescriptionTextInput.error = "Error, please enter description."
                return@setOnClickListener
            } else if (giftCost.text.isBlank()) {
                giftCostTextInput.error = "Error, please enter gift cost."
                return@setOnClickListener
            }

            val dateOfPurchanse: String = SimpleDateFormat(Constants.PURCHASE_DATE_FORMAT, Locale.US).format(Date())
            val gift = Gift(giftDescription.text.toString(), giftCost.text.toString(), dateOfPurchanse)
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
}