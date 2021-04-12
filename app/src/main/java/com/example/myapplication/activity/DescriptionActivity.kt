package com.example.myapplication.activity

//import com.example.myapplication.adapter.DescriptionRecyclerAdapter
//import com.example.myapplication.adapter.RestaurantDetailRecyclerAdapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CartRecyclerAdapter
import com.example.myapplication.adapter.DescriptionRecyclerAdapter
import com.example.myapplication.model.Cartitem
import com.example.myapplication.model.Description
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ncorti.slidetoact.SlideToActView
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_description.view.*
import kotlinx.android.synthetic.main.cart_bottom_sheets.*
import kotlinx.android.synthetic.main.recycler_description_single_row.*
import kotlinx.android.synthetic.main.recycler_description_single_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class DescriptionActivity : AppCompatActivity() {
    private var descriptionList:List<Description> = ArrayList()
    private var DescriptionRecyclerAdapter: DescriptionRecyclerAdapter = DescriptionRecyclerAdapter(
        descriptionList
    )
    private var CartList:List<Cartitem> = ArrayList()
    private var CartRecyclerAdapter: CartRecyclerAdapter = CartRecyclerAdapter(CartList)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var recyclerviewactivity: RecyclerView
    lateinit var Toolbar:androidx.appcompat.widget.Toolbar
    lateinit var Proceedtocartbtn:Button
    lateinit var Progresslayout:RelativeLayout
    lateinit var bottomSheetBehavior:BottomSheetBehavior<RelativeLayout>
    private var ResName:String=""
    private var proceedtocartbtn=0
    var  mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    val TAG ="Description activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        recyclerviewactivity=findViewById(R.id.recyclerDescription)
        val sharedPreferences=getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val getvalue=sharedPreferences.getInt("newVal", 0)
        val getvalue2=sharedPreferences.getInt("odVal", 0)
        var name=sharedPreferences.getString("ResName", "")
        ResName=name.toString()
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheets)
        bottomSheetBehavior.setPeekHeight(300)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)

        Proceedtocartbtn=findViewById(R.id.ProceedToCart)
        Proceedtocartbtn.setOnClickListener {
            getCartList1().addOnCompleteListener {
                if (it.isSuccessful) {
                    CartList = it.result!!.toObjects(Cartitem::class.java)
                    CartRecyclerAdapter.postListItems = CartList
                    CartRecyclerAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }

                val i = CartRecyclerAdapter.itemCount
                if (i == 1) {
                    if (sharedPreferences.getString("Foodnameforbottomsheet", null)!=null) {
                        val newfoodname =
                            sharedPreferences.getString("Foodnameforbottomsheet", null)

                        val db = Firebase.firestore
                        val docRef = newfoodname?.let { it1 ->
                            db.collection("CartItems")
                                .document("${currentUser?.uid}")
                                .collection("Cartitemincoll")
                                .document(it1)
                        }
                        docRef?.get()
                            ?.addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                                    CartFoodNameforbotomsheets.text =
                                        document.getString("cartfoodname")
                                    val quant = document.get("Quantity")
                                    CartQuantityforbottomsheets.text = quant.toString()
                                } else {
                                    Log.d(TAG, "No such document")
                                }
                            }
                            ?.addOnFailureListener { exception ->
                                Log.d(TAG, "get failed with ", exception)
                            }

                        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheets)
                        bottomSheetBehavior.setPeekHeight(300)
                        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            Proceedtocartbtn.visibility = View.VISIBLE
                        } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                            Proceedtocartbtn.visibility = View.VISIBLE
                        } else {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            Proceedtocartbtn.visibility = View.INVISIBLE
                        }
                    }else{
                        val i=Intent(this,CartActivity::class.java)
                        startActivity(i)
                    }
                } else {
                    val intent = Intent(this, CartActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

            }
            }



//        Proceedtocartbtn.visibility=View.INVISIBLE
        Progresslayout=findViewById(R.id.progress_layout2)
        Progresslayout.visibility=View.VISIBLE
        getPostList1().addOnCompleteListener {
            if (it.isSuccessful){

                descriptionList=it.result!!.toObjects(Description::class.java)
                DescriptionRecyclerAdapter.postListItems=descriptionList
                DescriptionRecyclerAdapter.notifyDataSetChanged()
                Progresslayout.visibility=View.GONE
            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerDescription.layoutManager= LinearLayoutManager(this)
        recyclerDescription.adapter=DescriptionRecyclerAdapter

       supportActionBar?.setTitle("Description")
       supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val editor=sharedPreferences.edit()
        editor.apply {
            putString("description name", ResName)
        }.apply()
//        var slideToActView=findViewById<SlideToActView>(R.id.slider)
//
//        slideToActView.setOnSlideCompleteListener(object : SlideToActView.OnSlideCompleteListener() {
//            override fun onSlideComplete(view: SlideToActView) {
//
//            }
//        })
        val slide = findViewById<SlideToActView>(R.id.slider)

        slide.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {

            }
        }

        slide.onSlideToActAnimationEventListener = object :
            SlideToActView.OnSlideToActAnimationEventListener {
            override fun onSlideCompleteAnimationStarted(view: SlideToActView, threshold: Float) {

            }

            override fun onSlideCompleteAnimationEnded(view: SlideToActView) {
                val i=Intent(this@DescriptionActivity,GooglePaymentAPIActivity::class.java)
                startActivity(i)
            }

            override fun onSlideResetAnimationStarted(view: SlideToActView) {

            }

            override fun onSlideResetAnimationEnded(view: SlideToActView) {

            }
        }
    }



    fun getPostList1():Task<QuerySnapshot> {
       return firebaseFirestore
           .collection("Restaurant_Name1")
           .document(ResName)
           .collection("FoodName")
           .get()

    }
    fun getCartList1(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("CartItems")
            .document("${currentUser?.uid}")
            .collection("Cartitemincoll")
            .get()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val savedthing=outState.putString("ResNamesaved", ResName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val sharedPreferences=getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val name=sharedPreferences.getString("description name", null)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        getPostList1().addOnCompleteListener {
            if (it.isSuccessful){

                descriptionList=it.result!!.toObjects(Description::class.java)
                DescriptionRecyclerAdapter.postListItems=descriptionList
                DescriptionRecyclerAdapter.notifyDataSetChanged()
                Progresslayout.visibility=View.GONE
            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerDescription.layoutManager= LinearLayoutManager(this)
        recyclerDescription.adapter=DescriptionRecyclerAdapter
    }
     }


