package com.example.streetsale

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso


class BidListAdapter(private val mList: List<aBid>) : RecyclerView.Adapter<BidListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bids_list_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageURI(ItemsViewModel.img)

        // sets the text to the textview from our itemHolder class
//        Picasso
//            .get()
//            .load(itemsViewModel.img)
//            .into(holder.imageView);
        //var descView: String = " TITLE: "+itemsViewModel.name+"\n"+" DESCRIPTION: "+itemsViewModel.description+"\n"+" ADDRESS: "+itemsViewModel.address+" "
//        holder.textView.text = descView
//        holder.viewsale.setOnClickListener {
//            //go to open sales activity
//            var intent: Intent = Intent(holder.itemView.context, SaleActivity::class.java)
//            intent.putExtra("saleId", itemsViewModel.saleId)
//        }
        var userId = FirebaseAuth.getInstance().currentUser!!.uid

        var txt = " "+itemsViewModel.userName+": $"+itemsViewModel.price
        holder.textView.text = txt

        if (itemsViewModel.bidAdmin.equals(userId)){
            holder.viewSale.visibility = View.VISIBLE
            holder.viewSale.setOnClickListener {
//                itemsViewModel.soldTo=true
//                itemsViewModel.boughtId = itemsViewModel.userId
//                itemsViewModel.price = Integer.parseInt(itemsViewModel.price.toString())
                //remove item from list of items reload sales, move to users sold to items
                FirebaseFirestore.getInstance().collection("sales").document(itemsViewModel.saleId).collection("items").document(itemsViewModel.idItem).get().addOnSuccessListener {
                        var itemobj=Saleitem()
                         itemobj = it.toObject(Saleitem::class.java)!!
                        itemobj.sold=true
                        itemobj.boughtById = itemsViewModel.userId
                        itemobj.price = itemsViewModel.price.toString()
                        itemobj.boughtByName= itemsViewModel.userName


                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                .collection("soldTo")
                                .document(itemsViewModel.idItem).set(
                                    itemobj
                                ).addOnSuccessListener {
                                    FirebaseFirestore.getInstance().collection("sales")
                                        .document(itemsViewModel.saleId).collection("items")
                                        .document(itemsViewModel.idItem)
                                        .update(
                                            "sold", true,
                                            "boughtById", itemsViewModel.userId,
                                            "boughtByName", itemsViewModel.userName,
                                            "price", itemsViewModel.price.toString()

                                            //SetOptions.merge()
                                        ).addOnSuccessListener {
                                            var intnt= Intent(holder.itemView.context, SaleActivity::class.java)
                                            intnt.putExtra("saleId",itemsViewModel.saleId)
                                            intnt.putExtra("saleIdAdmin",itemsViewModel.bidAdmin)



                                            holder.itemView.context.startActivity(intnt)
                                        }

                                }

                    }

            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
      //  val imageView: ImageView = itemView.findViewById(R.id.bidPricePlaced)
        val textView: TextView = itemView.findViewById(R.id.bidPricePlaced)
        val viewSale: Button = itemView.findViewById(R.id.acceptBidbtn)
    }
}