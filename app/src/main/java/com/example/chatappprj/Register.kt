package com.example.chatappprj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
//    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        nameField = findViewById(R.id.name_field)
        emailField = findViewById(R.id.email_field)
        passField = findViewById(R.id.pass_field)
        btnRegister = findViewById(R.id.register_btn)

        btnRegister.setOnClickListener{
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val pass = passField.text.toString()

            register(name,email,pass)
        }
    }
    private fun register(name:String, email:String, pass:String){
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@Register, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Register, "Error with registration", Toast.LENGTH_SHORT).show()
                }
            }
    }
}