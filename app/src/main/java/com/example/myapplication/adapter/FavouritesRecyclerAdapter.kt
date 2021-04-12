package com.example.myapplication.adapter

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.activity.DescriptionActivity
import com.example.myapplication.fragment.FavouritesRestaurantsFragment
import com.example.myapplication.model.Favourites
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.recycler_favourite_single_row.view.*


class FavouritesRecyclerAdapter(var postListItems: List<Favourites>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class FavouriteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_favourite_single_row, parent, false)

            return FavouriteViewHolder(view)
        }

        override fun getItemCount(): Int {
            return postListItems.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val Favourites = postListItems[position]
            val context1=holder.itemView.context
            var soundPool: SoundPool? = null
            val soundId = 1
            soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
            soundPool.load(context1, R.raw.uitap, 1)
            var  mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            holder.itemView.txtFavFoodName.text = Favourites.FoodName
            holder.itemView.txtFavFoodRating.text = Favourites.FoodRating
            holder.itemView.txtFavBookPrice.text="Rs."+Favourites.FoodPrice
            holder.itemView.imgFavFoodImage.load(Favourites.FoodImage){  transformations(
                coil.transform.RoundedCornersTransformation(
                    45f,
                    45f,
                    45f,
                    45f
                )
            )}
            holder.itemView.favimgbtn1.setOnClickListener {
                holder.itemView.favimgbtn1.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                val db = Firebase.firestore
                currentUser?.uid?.let { it1 ->
                    db.collection("FavRestaurant")
                        .document(it1)
                        .collection("Favrest")
                        .document("deleteall")//deleteall
                        .collection("Favs")
                        .document(Favourites.FoodName)
                        .delete()
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!")
                            Toast.makeText(
                                context1,
                                "${Favourites.FoodName} removed from favourites",
                                Toast.LENGTH_SHORT
                            ).show()
                            }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                }
            }
            holder.itemView.Content11.setOnClickListener {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
                val intent= Intent(context1, DescriptionActivity::class.java)
                intent.putExtra("ResName", Favourites.FoodName)
                context1.startActivity(intent)
            }



//                val image = R.drawable.ic_baseline_favorite_border_24
//                holder.favimgbtn.setImageResource(image)

        }

    companion object {
        private const val TAG="favoritesAdapter"
    }
    }
