package com.example.myapplication.fragment

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.FavouritesRecyclerAdapter
import com.example.myapplication.model.Favourites
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class FavouritesRestaurantsFragment : Fragment() {
    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    private var Favourites: List<Favourites> = ArrayList()
    private var FavouritesAdapter: FavouritesRecyclerAdapter = FavouritesRecyclerAdapter(Favourites)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
   var  mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourties, container, false)
        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progress_layout4)
        val progressbarforfav:RelativeLayout=view.findViewById(R.id.progress_barforfav)
        progressbarforfav.visibility=View.VISIBLE
        getPostList().addOnCompleteListener {
            if (it.isSuccessful) {
                Favourites = it.result!!.toObjects(com.example.myapplication.model.Favourites::class.java)
                FavouritesAdapter.postListItems = Favourites
                FavouritesAdapter.notifyDataSetChanged()
                progressbarforfav.visibility = View.GONE
                progressLayout.visibility=View.GONE
                val i=FavouritesAdapter.itemCount
                if (i==0){
                    progressLayout.visibility=View.VISIBLE
                }
            }
            else {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerFavourite.layoutManager = LinearLayoutManager(context)
        recyclerFavourite.adapter = FavouritesAdapter

      setHasOptionsMenu(true)
        return view
    }

    private fun delete() {
        val db = Firebase.firestore
       currentUser?.uid?.let {
            db.collection("FavRestaurant")
                .document(it)
                .collection("Favrest")
                .document("deleteall")
                .delete()
//                .addOnSuccessListener {
//                    Toast.makeText(context, "success to delete all", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener{
//                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
//                }
        }

    }

    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("FavRestaurant")
            .document("${currentUser?.uid}")
            .collection("Favrest")
            .document("deleteall")//deleteall
            .collection("Favs")
            .get()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favourties, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clear_all -> {
                Toast.makeText(context as Activity, "clicked", Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.framelayout,
                    FavouritesRestaurantsFragment()
                )?.commit()
                val db = Firebase.firestore
                var  mAuth = FirebaseAuth.getInstance()
                val currentUser = mAuth.currentUser
                if (currentUser != null) {
                    db.collection("FavRestaurant")
                        .document(currentUser.uid)
                        .collection("Favrest")
                        .document("deleteall")
                        .delete()
                }

//                val a = FavouritesAdapter.itemCount
//                if (a == 0) {
//                    Toast.makeText(context, "your favourites page is empty", Toast.LENGTH_SHORT)
//                        .show()
//                } else if (a>0){
//                    delete()
//                    activity?.supportFragmentManager?.beginTransaction()?.replace(
//                        R.id.framelayout,
//                        FavouritesRestaurantsFragment()
//                    )?.commit()
//                }

            }
        }
        return true
    }

}