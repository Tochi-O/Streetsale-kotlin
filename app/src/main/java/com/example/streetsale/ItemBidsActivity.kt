package com.example.streetsale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ItemBidsActivity : AppCompatActivity() {

    lateinit var  newBidtxt: EditText
    lateinit var  newBidbtn: Button

    var priceBids: ArrayList<aBid> = ArrayList()
    var itemI=Saleitem()
    var userId:FirebaseAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_bids)

         newBidtxt= findViewById(R.id.nBidTxt)
        newBidbtn= findViewById(R.id.nbid)

        val bidlist: RecyclerView = findViewById(R.id.recyclerBidsspersale)

        bidlist.layoutManager = LinearLayoutManager(this)

        var bidAdapter = BidListAdapter(priceBids)

        // Setting the Adapter with the recyclerview
        bidlist.adapter = bidAdapter
        var idSale=intent.getStringExtra("saleId")
        var idItem= intent.getStringExtra("itemId")
        Log.d("item bids", "onCreate: ${idItem}")
        Log.d("item bids", "onCreate: ${idSale}")

        FirebaseFirestore.getInstance().collection("sales").document(idSale!!).collection("items").document(idItem!!).get().addOnSuccessListener { doc0->

            itemI= doc0.toObject(Saleitem::class.java)!!

            itemI.itemId=doc0.id
        }

                newBidbtn.setOnClickListener {
                    // Here you get get input text from the Edittext
                    var newBid = Integer.parseInt(newBidtxt.text.toString())
                    var nBid = aBid()
                    nBid.price = newBid
                    nBid.userId = userId.currentUser!!.uid
                    nBid.userName="nuttt"
                    nBid.bidAdmin=itemI.adminId

                    nBid.idItem = itemI.itemId
                    nBid.saleId=itemI.saleId
                    //nBid.itemObj=itemsViewModel
                    FirebaseFirestore.getInstance().collection("users").document(userId.currentUser!!.uid).get()
                        .addOnSuccessListener {dc1->

                            nBid.userName = dc1.data?.get("name") as String

                            FirebaseFirestore.getInstance().collection("sales").document(itemI.saleId).
                            collection("items").document(itemI.itemId).collection("bids").add(nBid).addOnSuccessListener {dc->
                                      priceBids.add(nBid)
                                      priceBids.sortByDescending { it.price }

                                      bidlist.layoutManager = LinearLayoutManager(this)

                                      bidAdapter = BidListAdapter(priceBids)
//
//                        // Setting the Adapter with the recyclerview
                                     bidlist.adapter = bidAdapter
                                bidAdapter.notifyDataSetChanged()

                            }
                        }
                }

        //get all bids

                FirebaseFirestore.getInstance().collection("sales").document(idSale!!).collection("items").document(idItem!!).collection("bids").get().addOnSuccessListener { docs1 ->

                    Log.d("item bids size run? ", "onCreate: ${priceBids.size}")

                    for ( dsnap1: DocumentSnapshot in docs1){
                        var bid= aBid()
                         bid= dsnap1.toObject(aBid::class.java)!!
                        //dsnap1.data?.get("price")
                        //if (bid != null) {
                            priceBids.add(bid)
                       // }
                        Log.d("item bids size", "onCreate: ${priceBids.size}")
                        bidlist.layoutManager = LinearLayoutManager(this)

                         bidAdapter = BidListAdapter(priceBids)

                        // Setting the Adapter with the recyclerview
                        bidlist.adapter = bidAdapter
                        bidAdapter.notifyDataSetChanged()

                    }



                }


        var actionBar = getSupportActionBar()

        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }


}

        // this event will enable the back
        // function to the button on press
        override fun onContextItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    return true
                }
            }
            return super.onContextItemSelected(item)
        }
}