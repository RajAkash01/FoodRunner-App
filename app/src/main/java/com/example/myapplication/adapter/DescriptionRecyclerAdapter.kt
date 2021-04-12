package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.CartActivity
import com.example.myapplication.activity.DescriptionActivity
import com.example.myapplication.activity.experimentActivity
import com.example.myapplication.model.Description
import com.example.myapplication.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_description.view.*
import kotlinx.android.synthetic.main.recycler_description_single_row.view.*
import kotlinx.android.synthetic.main.recycler_description_single_row.view.Content11

class DescriptionRecyclerAdapter( var postListItems:List<Description>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    class DescriptionViewHolder(view: View):RecyclerView.ViewHolder(view) {
val Proceedtocart=view.findViewById<Button>(R.id.ProceedToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_description_single_row,parent,false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val Description=postListItems[position]
        val context = holder.itemView.context
        var soundPool: SoundPool? = null
        val soundId = 1
          val sharedPreferences=context.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE)
       var firebaseStore: FirebaseStorage? = null
        firebaseStore = FirebaseStorage.getInstance()
        val db = Firebase.firestore
        var  mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool.load(context, R.raw.scroll, 1)
        (holder as DescriptionViewHolder)
        holder.itemView.FoodName.text = Description.FoodName
        holder.itemView.FoodPrice.text = "Rs."+Description.Food_Price_for_one
        holder.itemView.No_picker.minValue=0
        holder.itemView.No_picker.maxValue=20
        holder.itemView.No_picker.wrapSelectorWheel=true
        holder.itemView.No_picker.setOnValueChangedListener { picker, odVal, newVal ->
            soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            val editor: SharedPreferences.Editor=sharedPreferences.edit()
            editor.putString("Foodnameforbottomsheet", Description.FoodName).apply()

            if (newVal == 0) {
                val cartitems = hashMapOf(
                    "cartfoodname" to Description.FoodName,
                    "cartitemprice" to Description.Food_Price_for_one,
                    "Quantity" to newVal
                )
                if (currentUser != null) {
                    db.collection("CartItems")
                        .document(currentUser.uid)
                        .collection("Cartitemincoll")
                        .document(Description.FoodName)
                        .delete()
                }
            } else {

            val cartitems = hashMapOf(
                "cartfoodname" to Description.FoodName,
                "cartitemprice" to Description.Food_Price_for_one,
                "Quantity" to newVal
            )
            if (currentUser != null) {
                db.collection("CartItems")
                    .document(currentUser.uid)
                    .collection("Cartitemincoll")
                    .document(Description.FoodName)
                    .set(cartitems)
            }
        }
        }

//        holder.itemView.No_picker.setOnClickListener {
//            val intent=Intent(context,CartActivity::class.java)
//            intent.putExtra("FoodName",Description.FoodName)
//            intent.putExtra("FoodPrice",Description.Price_for_one)
//            context.startActivity(intent)
//        }
    }
}