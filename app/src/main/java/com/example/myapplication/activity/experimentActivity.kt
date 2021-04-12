package com.example.myapplication.activity

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_experiment.*
import java.lang.StringBuilder


class experimentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experiment)
val foodname=editTextTextPersonName.text.toString()
        val foodprice=editTextTextPersonName2.text.toString()
        val sharedPreferences=getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.apply{

        }
val foodnam=sharedPreferences.getString("foodname", 0.toString())
        button2.setOnClickListener {
            Toast.makeText(this, "$foodnam", Toast.LENGTH_SHORT).show()
        }
    }
        }

