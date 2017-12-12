package org.graham.com.giftreminderfirebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class LoginActivity : AppCompatActivity() {

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        setSupportActionBar(toolbar)

        val loginButton = findViewById<Button>(R.id.button_login)
        val edittextEmail = findViewById<EditText>(R.id.editText_email)
        val edittextPassword = findViewById<EditText>(R.id.editText_password)
        loginButton.setOnClickListener { view ->
            login(edittextEmail.text.toString(), edittextPassword.text.toString())
//            createUser(edittextEmail.text.toString(), edittextPassword.text.toString())
        }
    }

    private fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser;
                Log.d("Matt", "Successfully logged in, user: " + user!!.uid)
                var mainIntent: Intent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra("userUid", user.uid)
                startActivity(mainIntent)
            } else {
                Log.e("Matt", "result: " + task.result)
            }
        }
    }

    private fun createUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                Log.d("Matt", "Successfully logged in, user: " + user!!.uid)
                finish()
            } else {
                Log.e("Matt", "result: " + task.result)
            }
        }
    }

}
