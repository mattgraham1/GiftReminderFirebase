package org.graham.com.giftreminderfirebase

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import org.graham.com.giftreminderfirebase.models.User


class LoginActivity : AppCompatActivity() {
    private val TAG: String = "LoginActivity"
    var firebaseAuth = FirebaseAuth.getInstance()!!

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonSignup: Button? = null
    private var buttonLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editText_email)
        editTextPassword = findViewById(R.id.editText_password)

        buttonSignup = findViewById(R.id.button_signup)
        buttonSignup?.setOnClickListener { view ->
            Log.d(TAG, "Signup clicked....")
            buttonLogin?.isEnabled = false
            signup(editTextEmail?.text.toString(), editTextPassword?.text.toString())
        }

        buttonLogin = findViewById<Button>(R.id.button_login)
        buttonLogin?.setOnClickListener { view ->
            Log.d(TAG, "login button clicked...")
            buttonSignup?.isEnabled = false
            login(editTextEmail?.text.toString(), editTextPassword?.text.toString())
        }

        if(firebaseAuth.currentUser != null) {
            launchMainActivity(firebaseAuth.currentUser!!.uid)
            finish()
        }

        val buttonDebug = findViewById<Button>(R.id.button_debug) as Button
        buttonDebug.setOnClickListener {
            launchMainActivity("")
        }
    }

    private fun signup(email: String, password: String) {
        if(email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this, "Email or password is blank.", Toast.LENGTH_LONG).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            buttonLogin?.isEnabled = true

            checkTaskException(task)

            if (task.isSuccessful) {
                val currentUser = firebaseAuth.currentUser
                writeUserToFirebase(currentUser!!)
                launchMainActivity(currentUser?.uid)
            } else {
                Toast.makeText(this, "Failed to log in!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        if(email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this, "Email or password is blank.", Toast.LENGTH_LONG).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            buttonSignup?.isEnabled = true

            checkTaskException(task)

            if (task.isSuccessful) {
                val currentUser = firebaseAuth.currentUser
                launchMainActivity(currentUser?.uid)
            } else {
                Toast.makeText(this, "Failed to log in!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun writeUserToFirebase(currentUser: FirebaseUser) {
        Log.d(TAG, "display name: " + currentUser.displayName)
        val user = User(currentUser.email!!, currentUser.uid)
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.uid)
                .setValue(user)
    }

    private fun checkTaskException(task: Task<AuthResult>) {
        if (!task.isSuccessful) {
            try {
                throw task.exception!!
            } catch (e: FirebaseAuthWeakPasswordException) {
                editTextPassword!!.error = getString(R.string.error_weak_password)
                editTextPassword!!.requestFocus()
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                editTextEmail!!.error = getString(R.string.error_invalid_email)
                editTextEmail!!.requestFocus()
            } catch (e: FirebaseAuthUserCollisionException) {
                editTextEmail!!.error = getString(R.string.error_user_exists)
                editTextEmail!!.requestFocus()
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
    }

    private fun launchMainActivity(userUid: String?) {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}
