package com.example.streetsale

import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class ItemViewAdapter(private val mList: List<Saleitem>) : RecyclerView.Adapter<ItemViewAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sale_item_view, parent, false)

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
        var descView: String = " TITLE: "+itemsViewModel.name+ "\n"+" DESC: "+itemsViewModel.description+"\n"+" PRICE: "+itemsViewModel.price+"\n"
        holder.textView.text = itemsViewModel.name
        var userId = FirebaseAuth.getInstance().currentUser!!.uid
        if(itemsViewModel.adminId.equals(userId)){
            holder.delsale.visibility = View.VISIBLE
            holder.delsale.setOnClickListener {
                //go to open sales activity
                var intent: Intent = Intent(holder.itemView.context, DeleteActivity::class.java)
                intent.putExtra("itemId", itemsViewModel.itemId)
                intent.putExtra("saleId", itemsViewModel.saleId)
                intent.putExtra("type","item")
                holder.itemView.context.startActivity(intent)

            }

        }else{
            holder.delsale.visibility = View.GONE

        }
        holder.moreBtn.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("More Item Info")

// Set up the input
           // val input = EditText(holder.itemView.context)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//            input.setHint("PLACE A BID")
//            input.inputType = InputType.TYPE_CLASS_NUMBER
//            builder.setView(input)

            builder.setMessage(descView)

// Set up the buttons
            builder.setPositiveButton("DONE", DialogInterface.OnClickListener { dialog, which ->

            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()

        }

//        holder.bidBtn.setOnClickListener {
//            //dialog to add bid and load bid list
//            var newBid=0

//            var bidMap= hashMapOf<String, Any>(
//                "price" to newBid
//            )
//
//
//
//
//
//        }
//        holder.bidlist.layoutManager = LinearLayoutManager(holder.itemView.context)
//
//        itemsViewModel.priceBids.sortByDescending { it.price }
//        val bidAdapter = BidListAdapter(itemsViewModel.priceBids)
//
//        // Setting the Adapter with the recyclerview
//        holder.bidlist.adapter = bidAdapter

        holder.viewbidBtn.setOnClickListener {
            var intent1: Intent = Intent(holder.itemView.context, ItemBidsActivity::class.java)
            intent1.putExtra("itemId", itemsViewModel.itemId)
            intent1.putExtra("saleId", itemsViewModel.saleId)
            intent1.putExtra("adminId", itemsViewModel.adminId)

            holder.itemView.context.startActivity(intent1)

        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemposter)
        val textView: TextView = itemView.findViewById(R.id.itemdescview)
        val delsale: Button = itemView.findViewById(R.id.admindelItembtn)
       // val bidBtn: Button = itemView.findViewById(R.id.placeBidbtn)
        val viewbidBtn: Button = itemView.findViewById(R.id.viewOtherBidsbtn)
        val moreBtn: Button=itemView.findViewById(R.id.viewiteminfo)

    }
}