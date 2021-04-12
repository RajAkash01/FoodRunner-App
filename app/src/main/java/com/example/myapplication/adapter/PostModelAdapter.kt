package com.example.myapplication.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.activity.DescriptionActivity
import com.example.myapplication.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*
import kotlinx.android.synthetic.main.recycler_hometesting_single_row.view.*
import kotlinx.android.synthetic.main.recycler_hometesting_single_row.view.Content11
import kotlinx.android.synthetic.main.recycler_hometesting_single_row.view.leftimage

class PostModelAdapter( var postListItems:List<PostModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class DescViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ResName1: TextView =itemView.findViewById (R.id.ResName1)
        val txtfoodrating: TextView =itemView.findViewById(R.id.ResPriceForOne)
        val txtprice: TextView =itemView.findViewById(R.id.ResRating)
        val Content11: RelativeLayout =itemView.findViewById(R.id.Content11)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_hometesting_single_row,parent,false)
        return DescViewHolder(view)
    }
    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postModel = postListItems[position]
        val context=holder.itemView.context
        var soundPool: SoundPool? = null
        val soundId = 1
        var  mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val sharedPreferences=context.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE)
        val Profile=holder.itemView.findViewById<ImageView>(R.id.leftimage)
        holder.itemView.ResName1.text = postModel.ResName
        //to start animation
        val AnimationFadein= AnimationUtils.loadAnimation(context,R.anim.fadein)
        holder.itemView.ResName1.startAnimation(AnimationFadein)

       soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundPool.load(context, R.raw.uitap, 1)

        holder.itemView.ResPriceForOne.text = postModel.Price_for_one+"/Perperson"
        holder.itemView.ResRating.text = postModel.Rating
        holder.itemView.leftimage.load(postModel.Images){
            transformations(coil.transform.RoundedCornersTransformation( 45f,  45f,  45f, 45f))
        }
        holder.itemView.Content11.setOnClickListener {
            soundPool.play(soundId,1F, 1F, 0, 0, 1F)
            val intent=Intent(context,DescriptionActivity::class.java)
            val name=sharedPreferences.edit()
            name.apply{
                putString("ResName",postModel.ResName)
            }.apply()
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())
        }
        holder.itemView.favimgbtn1.setOnClickListener {
            val image = R.drawable.ic_baseline_favorite_24
               holder.itemView.favimgbtn1.setImageResource(image)

            val editor=sharedPreferences.edit()
            editor.apply{
                putString("FoodName",postModel.ResName)
                putString("FoodPrice",postModel.Price_for_one)
                putString("FoodRating",postModel.Rating)
                putString("FoodImage",postModel.Images)
            }.apply()
            val  FavRestaurant=hashMapOf("FoodName" to postModel.ResName
                , "FoodPrice" to postModel.Price_for_one
                ,"FoodRating" to postModel.Rating
                ,"FoodImage" to postModel.Images
            )
            val db = Firebase.firestore
            //             to write data in cloud firestore:
            currentUser?.uid?.let {
                db.collection("FavRestaurant")
                    .document(it)
                    .collection("Favrest")
                    .document("deleteall")//deleteall
                    .collection("Favs")
                    .document(postModel.ResName)
                    .set(FavRestaurant)
                    .addOnCompleteListener {
                        Toast.makeText(context, "success to saved in favorites", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }


    }
    companion object {
        private const val TAG="PostModelAdapter"
    }


}