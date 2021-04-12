package com.example.myapplication.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.DashboardRecyclerAdapter
import com.example.myapplication.adapter.PostModelAdapter
import com.example.myapplication.model.Food
import com.example.myapplication.model.PostModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_recyclerview.*

class RecyclerViewActivity : AppCompatActivity() {
    private var postList:List<PostModel> = ArrayList()
    private var postListAdapter:PostModelAdapter = PostModelAdapter(postList)
    private val firebaseFirestore:FirebaseFirestore= FirebaseFirestore.getInstance()
    lateinit var recyclerviewactivity: RecyclerView
    lateinit var progresslayout:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        val PostModellist = arrayListOf<PostModel>()
        recyclerviewactivity=findViewById(R.id.recyclerTest)


        progresslayout=findViewById(R.id.progress_layout1)
        progresslayout.visibility=View.VISIBLE

         getPostList().addOnCompleteListener {
             if (it.isSuccessful){
                 postList=it.result!!.toObjects(PostModel::class.java)
                 postListAdapter.postListItems=postList
                 postListAdapter.notifyDataSetChanged()
                 progresslayout.visibility=View.GONE
             }else{
                 Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
             }
         }
         recyclerTest.layoutManager=LinearLayoutManager(this)
        recyclerTest.adapter=postListAdapter

//        getPostList().addOnCompleteListener{
//            layoutManager = LinearLayoutManager(this)
//            recyclerAdapter = PostModelAdapter(
//                this,
//                PostModellist
//            )
//            recyclerviewactivity.adapter = recyclerAdapter
//            recyclerviewactivity.layoutManager = layoutManager
//            recyclerAdapter.notifyDataSetChanged()
//        }

    }
    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore
        .collection("Restaurant_Name1")
            .get()

    }

}