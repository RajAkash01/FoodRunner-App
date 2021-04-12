package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.activity.DescriptionActivity
import com.example.myapplication.database.FoodDatabase
import com.example.myapplication.database.FoodEntity
import com.example.myapplication.model.Food
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context,val itemlist:ArrayList<Food>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtfoodname:TextView=view.findViewById (R.id.bookname)
        val txtfoodrating:TextView=view.findViewById(R.id.ratings)
        val image:ImageView=view.findViewById(R.id.leftimage)
        val txtprice:TextView=view.findViewById(R.id.price)
        val Content11: RelativeLayout =view.findViewById(R.id.Content11)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
     return itemlist.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
                  val food=itemlist[position]
          holder.txtfoodname.text=food.foodName
          holder.txtfoodrating.text=food.foodRating
//        holder.image.setImageResource(book.bookImage)
          holder.txtprice.text=food.foodPrice
        Picasso.get().load(food.foodImage).error(R.drawable.default_book_cover).into(holder.image)
        holder.Content11.setOnClickListener{
            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("id",food.foodId)
            context.startActivity(intent)
           // Toast.makeText(context,"Clicked on ${holder.textview}",Toast.LENGTH_LONG).show()
        }
    }

}