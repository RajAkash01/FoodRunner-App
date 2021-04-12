package com.example.myapplication.activity


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.R
import com.example.myapplication.fragment.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class DrawerlayoutActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorlayout:CoordinatorLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationview:NavigationView
//    lateinit var logout:TextView
    var previousMenuItem:MenuItem? =null
    private lateinit var mAuth: FirebaseAuth
    lateinit var Logoutbtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawerlayout)
        mAuth = FirebaseAuth.getInstance()
        drawerLayout=findViewById(R.id.drawerlayout)
        coordinatorlayout=findViewById(R.id.coordinatorlayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.framelayout)
     navigationview=findViewById(R.id.navigationview)


        setUpToolbar()
        DashFragment()
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@DrawerlayoutActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationview.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
                it.isCheckable=true
                it.isChecked=true
                previousMenuItem=it
            }
            when(it.itemId){
                R.id.Home ->{supportFragmentManager.beginTransaction().replace(
                    R.id.framelayout,
                    HomeFragment()
                ).commit()
                drawerLayout.closeDrawers()
                    supportActionBar?.title="All Restaurants"

                }
               R.id.My_Profile ->
//               {intent=Intent(this@DrawerlayoutActivity,experimentActivity::class.java)
//               startActivity(intent)}
                {supportFragmentManager.beginTransaction().replace(
                    R.id.framelayout,
                    MyProfilesFragment()
                ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="My ProfileActivity"

                }
                R.id.Favourties_Restaurants ->{supportFragmentManager.beginTransaction().replace(
                    R.id.framelayout,
                    FavouritesRestaurantsFragment()
                ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourties Restaurants"

                }
                R.id.FAQs -> {supportFragmentManager.beginTransaction().replace(
                    R.id.framelayout,
                    FAQsFragment()
                ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="FAQs"
                }
                R.id.Order_History -> {supportFragmentManager.beginTransaction().replace(
                    R.id.framelayout,
                    OrderHistoryFragment()
                ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Order History"
                }
                R.id.Resgister->{
                    val intent=Intent(this,HomePageExperimentActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Order History"
                }
                R.id.Logout-> {
                    // Logout


                    val dialog=AlertDialog.Builder(this)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage(("Are sure you want to log out?"))
                    dialog.setPositiveButton(Html.fromHtml("<font color='#E43914'>Yes</font>")) { text, listner ->
                 //       Firebase.auth.signOut()
                        mAuth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                        //facebook logout
                        if (AccessToken.getCurrentAccessToken() != null) {
                            GraphRequest(
                                AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
                                GraphRequest.Callback {
                                    AccessToken.setCurrentAccessToken(null)
                                    LoginManager.getInstance().logOut()
                                    finish()
                                }
                            ).executeAsync()
                        }
                    }
                    dialog.setNegativeButton(Html.fromHtml("<font color='#1427E4'>No</font>")) { text, listner ->
                        // TODO nothing
                    }
                    dialog.create()
                    dialog.show()




                }
            }
            return@setNavigationItemSelectedListener true
        }

    }
fun setUpToolbar(){
    setSupportActionBar(toolbar)
    supportActionBar?.title ="toolbar"
    supportActionBar?.setDisplayShowHomeEnabled(true);
    supportActionBar?.setDisplayHomeAsUpEnabled(false);
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        if(id== android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun DashFragment(){
        supportFragmentManager.beginTransaction().replace(
            R.id.framelayout,
            HomeFragment()
        ).commit()
        supportActionBar?.title="All Restaurants"
        drawerLayout.closeDrawers()
        navigationview.setCheckedItem(R.id.Home)
    }
   override fun onBackPressed(){
       val frags=supportFragmentManager.findFragmentById(R.id.framelayout)
     when(frags){
         !is HomeFragment -> DashFragment()
         else-> super.onBackPressed()
     }
    }
}