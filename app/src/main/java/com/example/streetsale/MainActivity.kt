package com.example.streetsale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    var saledata = ArrayList<Sale>()
    lateinit var boughtItemsBtn: Button
    lateinit var addSaleBtn: Button
    lateinit var logoutBtn: Button
    lateinit var searchBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {


            logoutBtn = findViewById(R.id.logoutbtn)
            addSaleBtn = findViewById(R.id.addsale)
            boughtItemsBtn = findViewById(R.id.boughtitemsbtn)
            searchBtn= findViewById(R.id.searchbtn1)

            searchBtn.setOnClickListener {
                startActivity(Intent(this, SearchActivity::class.java))

            }


            logoutBtn.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }

            addSaleBtn.setOnClickListener {
                startActivity(Intent(this, AddSaleActivity::class.java))

            }

            var cUserId = FirebaseAuth.getInstance().currentUser!!.uid

            boughtItemsBtn.setOnClickListener {
                var intentt = Intent(this, SoldActivity::class.java)
                intentt.putExtra("type", "user")
                intentt.putExtra("userId", cUserId)
                startActivity(intentt)

            }
            // getting the recyclerview by its id
            val recyclerview = findViewById<RecyclerView>(R.id.allsalesrecyclerView)

            // this creates a vertical layout Manager
            recyclerview.layoutManager = LinearLayoutManager(this)
// This will pass the ArrayList to our Adapter
            val adapter = SaleViewAdapter(saledata)

            // Setting the Adapter with the recyclerview
            recyclerview.adapter = adapter

            //get it all from Firestore
            FirebaseFirestore.getInstance().collection("sales")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        var saledta = document.toObject(Sale::class.java)
                        saledta.saleId = document.id
                        saledata.add(saledta)
                        Log.d("saleid", "addUploadRecordToDb 1 : ${saledta.saleId}")

                        adapter.notifyDataSetChanged()
                        Log.d("add all sales", "Read document with ID ${document.id}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("add all sales", "Error getting documents $exception")
                }

            // ArrayList of class ItemsViewModel

            // This loop will create 20 Views containing
            // the image with the count of view
            //        for (i in 1..20) {
            //            data.add(ItemsViewModel(R.drawable.ic_baseline_folder_24, "Item " + i))
            //        }


            // calling the action bar
            var actionBar = getSupportActionBar()

            // showing the back button in action bar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
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