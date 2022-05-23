package com.example.streetsale

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso



    class SoldListAdapter(private val mList: ArrayList<Saleitem>) : RecyclerView.Adapter<com.example.streetsale.SoldListAdapter.ViewHolder>() {

        // create new views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflates the card_view_design view
            // that is used to hold list item
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sold_item, parent, false)

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
            var descView: String =
                " TITLE: "+itemsViewModel.name+"\n"+" DESCRIPTION: "+itemsViewModel.description+"\n"+" ADDRESS: "+itemsViewModel.addr+" "+"\n"+" BUYER: "+itemsViewModel.boughtByName+" "+"\n"+" PRICE: "+itemsViewModel.price+" "
            holder.textView.text = descView
            /*holder.viewsale.setOnClickListener {
                //go to open sales activity
               var intent: Intent = Intent(holder.itemView.context, SaleActivity::class.java)
               intent.putExtra("saleId", itemsViewModel.saleId)
            }*/

        }

        // return the number of the items in the list
        override fun getItemCount(): Int {
            return mList.size
        }

        // Holds the views for adding it to image and text
        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
            val imageView: ImageView = itemView.findViewById(R.id.solditemposter)
            val textView: TextView = itemView.findViewById(R.id.solditemdescview)
            //val viewsale: Button = itemView.findViewById(R.id.opensalebtn)
        }
    }

class SoldListUserAdapter(private val mList: List<Saleitem>) : RecyclerView.Adapter<com.example.streetsale.SoldListUserAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_sold_item, parent, false)

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
        var descView: String = " TITLE: "+itemsViewModel.name+"\n"+" DESCRIPTION: "+itemsViewModel.description+"\n"+" ADDRESS: "+itemsViewModel.addr+" "+"\n"+" PRICE: "+itemsViewModel.price+" "
        holder.textView.text = descView
        holder.viewsale.setOnClickListener {
            //go to open sales activity
//            var intent: Intent = Intent(holder.itemView.context, SaleActivity::class.java)
//            intent.putExtra("saleId", itemsViewModel.saleId)
            var intentMaps: Intent =  Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=${itemsViewModel.addr}"));
            holder.itemView.context.startActivity(intentMaps)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.solduitemposter)
        val textView: TextView = itemView.findViewById(R.id.solduitemdescview)
        val viewsale: Button = itemView.findViewById(R.id.soldaddrbtn)
    }
}

