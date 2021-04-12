package com.example.myapplication.fragment

import android.animation.Animator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.CartActivity
import com.example.myapplication.adapter.PostModelAdapter
import com.example.myapplication.model.PostModel
import com.example.myapplication.util.ConnectionManager
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
//import io.codetail.widget.RevealFrameLayout
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*


class HomeFragment : Fragment() {
    private var postList:List<PostModel> = ArrayList()
    private var postListAdapter: PostModelAdapter = PostModelAdapter(postList)
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var recyclerviewactivity: RecyclerView
    lateinit var progresslayout:RelativeLayout
lateinit var Floatingbtn:FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)
        recyclerviewactivity=view.findViewById(R.id.recyclerTest)


        if (ConnectionManager().CheckConnectivity(activity as Context)) {
            progresslayout=view.findViewById(R.id.progress_layout1)
            progresslayout.visibility=View.VISIBLE

            getPostList().addOnCompleteListener {
                if (it.isSuccessful){
                    postList=it.result!!.toObjects(PostModel::class.java)
                    postListAdapter.postListItems=postList
                    postListAdapter.notifyDataSetChanged()
                    progresslayout.visibility=View.GONE
                }else{
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                }
            }
            recyclerviewactivity.layoutManager=LinearLayoutManager(context)
            recyclerviewactivity.adapter=postListAdapter


//   to make divider between items   //recyclerdashboard.addItemDecoration(
//                                   DividerItemDecoration(
//                                       recyclerdashboard.context,
//                                       (layoutManager as LinearLayoutManager).orientation
//                                   )
//                              )





        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("internet connection not found")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val intentsettings = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intentsettings)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit ") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        Floatingbtn=view.findViewById(R.id.floatingbtn)
        Floatingbtn.setOnClickListener {
//            val layout: RevealFrameLayout = view.findViewById(R.id.layout) as RevealFrameLayout
//            val target: View = view.findViewById(R.id.floatingbtn)
//
//            val animator: Animator =
//            animator.setDuration(500)
//            animator.start()
            val Animation=AnimationUtils.loadAnimation(context, R.anim.bounce)
            Floatingbtn.startAnimation(Animation)
            Handler().postDelayed({
                Floatingbtn.clearAnimation()
                val intent = Intent(context, CartActivity::class.java)
                startActivity(intent)
            }, 620)
        }



        return view

    }




    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Restaurant_Name1")
            .get()

    }

    fun getPostListDescn(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Restaurant_Name1")
            .orderBy("ResName", Query.Direction.DESCENDING)
            .get()
    }
    fun getPostListAscen(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Restaurant_Name1")
            .orderBy("ResName", Query.Direction.ASCENDING)
            .get()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        val search= menu.findItem(R.id.action_search1)
        val searchView=search?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint="Search!"
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newtext = newText.toString()

                //                val newtext = searchtext[0].toUpperCase() + searchtext.substring(1)
                fun getsearchPostList(): Task<QuerySnapshot> {
                    return firebaseFirestore

                        .collection("Restaurant_Name1")
                        .orderBy("ResName").startAt("$newtext").endAt("$newtext\uf8ff")
                        .get()


                }
//                hideKeyboard()
                getsearchPostList().addOnCompleteListener {
                    if (it.isSuccessful) {
                        postList = it.result!!.toObjects(PostModel::class.java)
                        postListAdapter.postListItems = postList
                        postListAdapter.notifyDataSetChanged()
                        progresslayout.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerviewactivity.layoutManager = LinearLayoutManager(context)
                recyclerviewactivity.adapter = postListAdapter
                return true
            }

        })
    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
//    fun Activity.hideKeyboard() {
//        hideKeyboard(currentFocus ?: View(this))
//    }   for activity to hide keyboard

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_by_high -> {
                getPostListDescn().addOnCompleteListener {
                    if (it.isSuccessful) {
                        postList = it.result!!.toObjects(PostModel::class.java)
                        postListAdapter.postListItems = postList
                        postListAdapter.notifyDataSetChanged()
                        progresslayout.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerviewactivity.layoutManager = LinearLayoutManager(context)
                recyclerviewactivity.adapter = postListAdapter
            }
            R.id.sort_by_low -> {

                getPostListAscen().addOnCompleteListener {
                    if (it.isSuccessful) {
                        postList = it.result!!.toObjects(PostModel::class.java)
                        postListAdapter.postListItems = postList
                        postListAdapter.notifyDataSetChanged()
                        progresslayout.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerviewactivity.layoutManager = LinearLayoutManager(context)
                recyclerviewactivity.adapter = postListAdapter
            }

        }
        return super.onOptionsItemSelected(item)
    }
    


}
