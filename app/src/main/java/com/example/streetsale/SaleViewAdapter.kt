package com.example.streetsale

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class SaleViewAdapter(private val mList: List<Sale>) : RecyclerView.Adapter<SaleViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sale_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageURI(ItemsViewModel.img)

        // sets the text to the textview from our itemHolder class
        Picasso
            .get()
            .load(itemsViewModel.img)
            .into(holder.imageView);
        var descView: String = " TITLE: "+itemsViewModel.name+"\n"+" DESCRIPTION: "+itemsViewModel.description+"\n"+" ADDRESS: "+itemsViewModel.address+" "
        holder.textView.text = descView
        holder.viewsale.setOnClickListener {
            //go to open sales activity
            var intent1: Intent = Intent(holder.itemView.context, SaleActivity::class.java)
            Log.d("saleid", "addUploadRecordToDb 2 : run first  ${itemsViewModel.saleId}")

            intent1.putExtra("saleId", itemsViewModel.saleId)
            intent1.putExtra("saleIdAdmin",itemsViewModel.admin)
            Log.d("saleid", "addUploadRecordToDb 2 : ${itemsViewModel.saleId}")
            holder.itemView.context.startActivity(intent1)
            Log.d("saleid", "addUploadRecordToDb 2 : run last ${itemsViewModel.saleId}")

        }



        var cUserId = FirebaseAuth.getInstance().currentUser?.uid
        if(cUserId.equals(itemsViewModel.admin)){
            holder.delSale.visibility=View.VISIBLE
            holder.delSale.setOnClickListener {
                var intent: Intent = Intent(holder.itemView.context, DeleteActivity::class.java)
                intent.putExtra("itemId", " ")
                intent.putExtra("saleId", itemsViewModel.saleId)
                intent.putExtra("type","sale")
                Log.d("saleid", "addUploadRecordToDb 2 : ${itemsViewModel.saleId}")
                holder.itemView.context.startActivity(intent)

            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageviewsaleposter)
        val textView: TextView = itemView.findViewById(R.id.saledescview)
        val viewsale: Button = itemView.findViewById(R.id.opensalebtn)
        val delSale: Button = itemView.findViewById(R.id.admindelbtn)

    }
}