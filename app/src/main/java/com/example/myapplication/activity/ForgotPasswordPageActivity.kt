package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password_page.*


class ForgotPasswordPageActivity : AppCompatActivity(){
    lateinit var forgotsendbutton: Button
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_page)
        val actionbar = supportActionBar
        actionbar!!.title = "forgot page"
        actionbar.setDisplayHomeAsUpEnabled(true)
        mAuth = FirebaseAuth.getInstance()
        forgotsendbutton=findViewById(R.id.forgotsendbtn)
        forgotsendbutton.setOnClickListener {
                val email = forgotemail?.text.toString().trim()
                    mAuth!!
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val message = "Email sent."
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "No user found with this email.", Toast.LENGTH_SHORT).show()
                            }
                        }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    }




