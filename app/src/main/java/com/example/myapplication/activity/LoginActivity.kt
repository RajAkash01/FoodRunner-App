package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.fragment.MyProfilesFragment
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_experiment.*
import kotlinx.android.synthetic.main.activity_forgot_password_page.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login2.*
import java.util.*


class LoginActivity : AppCompatActivity() {


    lateinit var btnclick: Button
    lateinit var forgotpassword: TextView
    lateinit var donthaveaccount: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var button: SignInButton
    lateinit var email:EditText
    lateinit var pass:EditText
    private lateinit var mAuth: FirebaseAuth
    companion object {
        const val RC_SIGN_IN = 120
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    //Facebook Callback manager
    var callbackManager: CallbackManager? = null
    val TAG="LoginActivity"
    lateinit var fbbtn:LoginButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            getString(R.string.SharedPrefrences_file_name),
            Context.MODE_PRIVATE
        )
        mAuth = FirebaseAuth.getInstance()
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_login)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        button = findViewById(R.id.googleinbutton)
        button.setOnClickListener {
            signIn()

        }
            if (isLoggedIn) {
                val intent = Intent(this@LoginActivity, DrawerlayoutActivity::class.java)
                startActivity(intent)
                finish()
            }


            btnclick = findViewById(R.id.btn)


            forgotpassword = findViewById(R.id.forgotpassword)
            donthaveaccount = findViewById(R.id.donthaveaccount)

            btnclick.setOnClickListener {
                email=findViewById(R.id.nameforlogin)
                pass=findViewById(R.id.passwordforlogin)
               val emailaddress=email.text.toString()
               val password1=pass.text.toString()

                if (TextUtils.isEmpty(emailaddress)&&!TextUtils.isEmpty(password1)){
                    Toast.makeText(this, "Email Address is empty", Toast.LENGTH_SHORT).show()
                }else if (TextUtils.isEmpty(password1)&&!TextUtils.isEmpty(emailaddress)){
                    Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
                }else if (TextUtils.isEmpty(emailaddress)&&TextUtils.isEmpty(password1)){
                    Toast.makeText(this, "Email and password is empty", Toast.LENGTH_SHORT).show()
                }
                else {
                   mAuth.signInWithEmailAndPassword(emailaddress, password1)
                        .addOnCompleteListener(this) { task ->


                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signInWithEmail:success", "success")
                                val intent = Intent(this, DrawerlayoutActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // ...
                            }

                            // ...

                        }
                }
            }
            forgotpassword.setOnClickListener {
                val intent = Intent(
                    this@LoginActivity,
                    ForgotPasswordPageActivity::class.java
                )
                startActivity(intent)

            }

            donthaveaccount.setOnClickListener {
                val intent = Intent(
                    this@LoginActivity,
                    RegistrationPageActivity::class.java
                )
                startActivity(intent)
            }
        callbackManager = CallbackManager.Factory.create();
        fbbtn=findViewById(R.id.facebookSignInButton)
        fbbtn.setOnClickListener {
            fbbtn.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY)
            fbsignin()
            
        }
        }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val intent = Intent(this, DrawerlayoutActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }


fun SavedPreferences() {
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        }

    private fun fbsignin() {
        // Callback registration
        facebookSignInButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                handleFacebookAccessToken(loginResult.accessToken);
                startActivity(Intent(this@LoginActivity, DrawerlayoutActivity::class.java))
                finish()
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "process canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@LoginActivity, "error occured", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG,  "handleFacebookAccessToken:" + token)

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth!!.currentUser
                    startActivity(Intent(this@LoginActivity, DrawerlayoutActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException())
                    Toast.makeText(this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    }


