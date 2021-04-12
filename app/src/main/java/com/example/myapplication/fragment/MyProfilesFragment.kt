package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class MyProfilesFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var name:TextView
    lateinit var email:TextView
    lateinit var id:TextView
    lateinit var profile:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
          name=view.findViewById(R.id.display_name12)
        email=view.findViewById(R.id.display_email12)
        id=view.findViewById(R.id.display_id12)
        profile=view.findViewById(R.id.display_profile12)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
//        Glide.with(this).load(currentUser?.photoUrl).into(display_image)
        name.text= currentUser?.displayName
        email.text=currentUser?.email
        id.text=currentUser?.uid
        Glide.with(this).load(currentUser?.photoUrl).apply(
            RequestOptions.bitmapTransform(
                RoundedCornersTransformation(
                    45,
                    0,
                    RoundedCornersTransformation.CornerType.ALL
                )
            )
        ).into(profile)
//      profile.load(currentUser?.photoUrl){
//          crossfade(true)
//          crossfade(1000)
//
//      }
        if (currentUser!=null){
//            txtview.visibility=View.GONE
//            gifimg.visibility=View. GONE
//            button.visibility=View. GONE
//            display_name.visibility=View.VISIBLE
//            display_email.visibility=View.VISIBLE
//            display_image.visibility=View.VISIBLE
        }else{
//            txtview.visibility=View.VISIBLE
//            gifimg.visibility=View. VISIBLE
//            button.visibility=View. VISIBLE
//            display_name.visibility=View. GONE
//            display_email.visibility=View.GONE
//            display_image.visibility=View.GONE
        }
        return view
    }

}