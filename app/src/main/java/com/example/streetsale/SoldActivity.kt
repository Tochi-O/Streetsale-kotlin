package com.example.streetsale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SoldActivity : AppCompatActivity() {
     var cUserId:String=""
     var saleId: String=""
     var userSoldItems: ArrayList<Saleitem> = ArrayList()
     var soldItems: ArrayList<Saleitem> =ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold)
        //this.onBackPressed()
        //  onBackPressed()
        val recyclerview = findViewById<RecyclerView>(R.id.soldrecyclerView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        //view items in sales sold
        // This will pass the ArrayList to our Adapter
        //view items in sales sold
        // This will pass the ArrayList to our Adapter
        val adapter = SoldListAdapter(soldItems)


        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        var whoSold = intent.getStringExtra("type").toString()

        if (whoSold.equals("user")) {
            cUserId = intent.getStringExtra("userId").toString()
            userSoldItems = ArrayList()

            val adapter = SoldListUserAdapter(userSoldItems)


            // Setting the Adapter with the recyclerview
            recyclerview.adapter = adapter


            //get sold items from the database
            //view items user has bought
            FirebaseFirestore.getInstance().collection("users").document(cUserId)
                .collection("soldTo").get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        var saledta = document.toObject(Saleitem::class.java)
                        saledta.itemId = document.id
                        userSoldItems.add(saledta)
                        Log.d("add all sales user", "Read document with ID ${document.id}")
                    }
                    Log.d("add all sales", "Read document with ID ${soldItems.size}")

                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w("add all sales", "Error getting documents $exception")
                }


        } else if (whoSold.equals("seller")) {
            saleId = intent.getStringExtra("saleId").toString()

            soldItems = ArrayList()
            val adapter = SoldListAdapter(soldItems)


            // Setting the Adapter with the recyclerview
            recyclerview.adapter = adapter
            //for every item in firestore if sold is true add to list
            //get it all from Firestore
            FirebaseFirestore.getInstance().collection("sales").document(saleId).collection("items")
                .whereEqualTo("sold", true).get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        var saledta = document.toObject(Saleitem::class.java)
                        saledta.itemId = document.id
                        soldItems.add(saledta)
                        Log.d("add all sales seller", "Read document with ID ${document.id}")
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w("add all sales", "Error getting documents $exception")
                }

            // calling the action bar

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