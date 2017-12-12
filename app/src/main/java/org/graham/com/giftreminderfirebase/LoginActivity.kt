package org.graham.com.giftreminderfirebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.button_login)
        val edittextEmail = findViewById<EditText>(R.id.editText_email)
        val edittextPassword = findViewById<EditText>(R.id.editText_password)
        loginButton.setOnClickListener { view ->
            Log.d("Matt", "login button clicked...")
            login(edittextEmail.text.toString(), edittextPassword.text.toString())
        }
    }

    private fun login(email: String, password: String) {
        if (firebaseAuth == null) {
            Log.d("Matt", "firebaseAuth is null....")
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser;
                Toast.makeText(this, "Successfully logged in, user: " + user, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to log in!", Toast.LENGTH_LONG).show()
            }
        }
    }

}
