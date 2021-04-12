package com.example.myapplication.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CartRecyclerAdapter
import com.example.myapplication.model.Cartitem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_description.*


class CartActivity : AppCompatActivity() {
    private var CartList:List<Cartitem> = ArrayList()
    private var CartRecyclerAdapter: CartRecyclerAdapter = CartRecyclerAdapter(CartList)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//    lateinit var recyclerviewactivity: RecyclerView
    var  mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        title="Your Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        recyclerviewactivity=findViewById(R.id.recyclerDescription)
        CartRecyclerAdapter.notifyDataSetChanged()
        getPostList1().addOnCompleteListener {
            if (it.isSuccessful){
                CartList=it.result!!.toObjects(Cartitem::class.java)
                CartRecyclerAdapter.postListItems=CartList
                CartRecyclerAdapter.notifyDataSetChanged()
                cartlayout.visibility=View.GONE
                val i=CartRecyclerAdapter.itemCount
                if (i==0){
                    cartlayout.visibility=View.VISIBLE
                }
            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
        CartRecycler.layoutManager= LinearLayoutManager(this)
        CartRecycler.adapter=CartRecyclerAdapter
        val sharedPreferences=getSharedPreferences(
            "sharedPrefs",
            android.content.Context.MODE_PRIVATE
        )
var sum=sharedPreferences.getInt("sum", 0)
        var itemprice=sharedPreferences.getInt("cartprice", 0)
        var subtotal=0
        subtotal=subtotal+itemprice





    total.text="Rs."+sum.toString()

 checkout.setOnClickListener {
     val i=Intent(this, GooglePaymentAPIActivity::class.java)
     startActivity(i)
 }

    }
    
    fun getPostList1(): Task<QuerySnapshot> {
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
}
