package com.example.myapplication.adapter

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.CartActivity
import com.example.myapplication.model.Cartitem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.recycler_cart_single_row.view.*
import kotlinx.android.synthetic.main.recycler_description_single_row.view.*
import kotlinx.android.synthetic.main.recycler_description_single_row.view.FoodName
import kotlinx.android.synthetic.main.recycler_description_single_row.view.FoodPrice


class CartRecyclerAdapter(var postListItems: List<Cartitem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
        class CartViewHolder(view: View): RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_cart_single_row,
                parent,
                false
            )
            return CartViewHolder(view)
        }

        override fun getItemCount(): Int {
            return postListItems.size
        }
//interface CallbackInterface{
//    fun passResultCallback(message: String)
//}



    var  sum=0
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val Cartitem=postListItems[position]
            val Context=holder.itemView.context
            var  mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            holder.itemView.FoodName.text = Cartitem.cartfoodname
            holder.itemView.FoodPrice.text = "Rs."+Cartitem.cartitemprice
            holder.itemView.TxtForTotalFoods.text= Cartitem.Quantity.toString()
            for ( i in 1 until  postListItems.size){
                sum += Cartitem.cartitemprice.toInt() * Cartitem.Quantity
            }
//            callbackInterface.passResultCallback(sum)
            val sharedPreferences=Context.getSharedPreferences(
                "sharedPrefs",
                android.content.Context.MODE_PRIVATE
            )
            val editor:SharedPreferences.Editor=sharedPreferences.edit()
            editor.putInt("sum", sum)
                editor.putInt("cartprice", Cartitem.cartitemprice.toInt()).apply()
        Toast.makeText(
            Context,
            "total:$sum lastnumber:${postListItems.size} numbers:${Cartitem.cartitemprice.toInt()}",
            Toast.LENGTH_SHORT
        ).show()
            holder.itemView.clear_foodname.setOnClickListener {
                val db = Firebase.firestore
                db.collection("CartItems")
                    .document("${currentUser?.uid}")
                    .collection("Cartitemincoll")
                    .document("${Cartitem.cartfoodname}")
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(Context, "Successfully deleted", Toast.LENGTH_SHORT).show()
                        val i=Intent(Context, CartActivity::class.java)
                        Context.startActivity(i)
                        (holder.itemView.context as Activity).finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(Context, "Failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }