package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R


class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val actionbar=supportActionBar
        actionbar?.title="Home"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
       onBackPressed()
        return true
    }

}