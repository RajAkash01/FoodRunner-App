package com.example.myapplication.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.Explode
import android.transition.Fade
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val SPLASHTIMEOUT:Long = 500
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            // set an exit transition
            exitTransition = Explode()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashsceen)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Handler().postDelayed({
            startActivity(intent)
            finish()
            if(user != null){
                val dashboardIntent = Intent(this,DrawerlayoutActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(dashboardIntent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                finish()
            }else{
                val signInIntent = Intent(this, LoginActivity::class.java)
                startActivity(signInIntent)
                finish()
                Toast.makeText(this, "signinplzz", Toast.LENGTH_SHORT).show()
            }
        },SPLASHTIMEOUT)


    }
}