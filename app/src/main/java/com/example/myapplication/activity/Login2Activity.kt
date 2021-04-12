package com.example.myapplication.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login2.*

class Login2Activity : AppCompatActivity() {
    lateinit var Firstname23:EditText
    lateinit var Lastname23:EditText
    lateinit var Email23:EditText
    lateinit var Password23:EditText
    //  lateinit var confirmPass:EditText
    lateinit var button: Button
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "CreateAccountActivity"
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        button=findViewById(R.id.buttonforregister)
        button.setOnClickListener {
            mAuth!!
                .createUserWithEmailAndPassword(registeremail.text.toString(), registerpassword.text.toString())
                .addOnCompleteListener(this@Login2Activity) { task ->

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        val userId = mAuth!!.currentUser!!.uid

                        //Verify Email

                        Toast.makeText(this, "email verified..", Toast.LENGTH_SHORT).show()
                        //update user profile information
                        val currentUserDb = mDatabaseReference!!.child(userId)
                        currentUserDb.child("firstName").setValue(registername.text.toString())
                        currentUserDb.child("lastName").setValue(registerlastname.text.toString())
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this@Login2Activity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

}