package com.example.chatappprj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        emailField = findViewById(R.id.email_field)
        passField = findViewById(R.id.pass_field)
        btnLogin = findViewById(R.id.login_btn)
        btnRegister = findViewById(R.id.register_btn)

        btnRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = emailField.text.toString()
            val pass = passField.text.toString()

            login(email,pass)
        }


    }

    private fun login(email:String, pass:String){
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Login, "User does not exists", Toast.LENGTH_SHORT).show()
                }
            }
    }
}