package com.example.myapplication.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration_page.*


class RegistrationPageActivity : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)
        val actionbar = supportActionBar
        actionbar!!.title = "Register Yourself"
        actionbar.setDisplayHomeAsUpEnabled(true)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        button=findViewById(R.id.buttontoregister)
        button.setOnClickListener {

               mAuth!!
                   .createUserWithEmailAndPassword(EmailforRegistration.text.toString(), PasswordforRegistration.text.toString())
                   .addOnCompleteListener(this@RegistrationPageActivity) { task ->

                       if (task.isSuccessful) {
                           // Sign in success, update UI with the signed-in user's information
                           Log.d(TAG, "createUserWithEmail:success")

                           val userId = mAuth!!.currentUser!!.uid

                           //Verify Email
                           verifyEmail()
                           Toast.makeText(this, "email verified..", Toast.LENGTH_SHORT).show()
                           //update user profile information
                           val currentUserDb = mDatabaseReference!!.child(userId)
                           currentUserDb.child("firstName").setValue(FirstNameForRegistration.text.toString())
                           currentUserDb.child("lastName").setValue(LastNameforRegistration.text.toString())
                           Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                            updateUserInfoAndUI()
                       } else {
                           // If sign in fails, display a message to the user.
                           Log.w(TAG, "createUserWithEmail:failure", task.exception)
                           Toast.makeText(this@RegistrationPageActivity, "Authentication failed.",
                               Toast.LENGTH_SHORT).show()
                       }
                   }

                   }
                }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(this@RegistrationPageActivity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this@RegistrationPageActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateUserInfoAndUI() {

        //start next activity
        val intent = Intent(this@RegistrationPageActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
