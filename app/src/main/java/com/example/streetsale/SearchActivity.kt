package com.example.streetsale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    lateinit var searchEdittxt: EditText
    lateinit var searchBtn: Button
    lateinit var searchRecycview: RecyclerView

    var saledata = ArrayList<Sale>()
    var saledataSearch = ArrayList<Sale>()
    var saledatafields = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEdittxt= findViewById(R.id.searchedittxt)
        searchBtn= findViewById(R.id.searchbtn)
        searchRecycview=findViewById(R.id.searchitemspersale)

        searchRecycview.layoutManager = LinearLayoutManager(this)
        val adapter = SaleViewAdapter(saledataSearch)

        // Setting the Adapter with the recyclerview
        searchRecycview.adapter = adapter

        //enter search fields
        //if the search field is contained in any of the search fields
        //add to list
        //post to adapter, show in adapter
        FirebaseFirestore.getInstance().collection("sales")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { document ->
                    var saledta = document.toObject(Sale::class.java)
                    saledta.saleId = document.id
                    saledata.add(saledta)
                    Log.d("add all sales", "Read document with ID ${document.id}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("add all sales", "Error getting documents $exception")

        }

        saledatafields = searchEdittxt.text.toString().split("") as ArrayList<String>


        searchBtn.setOnClickListener {
            for(sale: Sale in saledata){
                for (str: String in saledatafields){
                    if(sale.searchFields.contains(str)){
                       if (!saledataSearch.contains(sale)){
                         saledataSearch.add(sale)
                       }

                    }
                }
            }
            adapter.notifyDataSetChanged()
        }



        // calling the action bar
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
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}

