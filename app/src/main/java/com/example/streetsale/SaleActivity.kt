package com.example.streetsale

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class SaleActivity : AppCompatActivity() {

    lateinit var titleNDescView: TextView
    lateinit var addrView: TextView
    lateinit var goToAddr: Button
    lateinit var editSaleBtn: Button
    lateinit var viewSoldBtn: Button

    lateinit var addItemBtn: Button
    lateinit var recycItems: RecyclerView
    lateinit var salePoster: ImageView
    lateinit var idSale: String
    lateinit var idSaleAdmin: String
    var desctxt: String=""
    lateinit var itemsL: ArrayList<Saleitem>
    lateinit var aSale: Sale
    lateinit var moreBtn :Button
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var userId: String? = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)


        idSale = intent.getStringExtra("saleId").toString()
        idSaleAdmin = intent.getStringExtra("saleIdAdmin").toString()
        Log.d("saleactivity 2", "onCreate: sale id $idSale")
        Log.d("saleactivity 2", "onCreate: sale id $idSaleAdmin")



        addItemBtn = findViewById(R.id.adminaddbtn)
        salePoster = findViewById(R.id.imageviewsaleposter2)
        editSaleBtn = findViewById(R.id.admineditbtn)
        viewSoldBtn = findViewById(R.id.viewsoldbtn)
        titleNDescView = findViewById(R.id.saledescview2)
        addrView = findViewById(R.id.addrView)
        goToAddr = findViewById(R.id.goToAddrbtn)
        moreBtn = findViewById(R.id.viewinfo)


        recycItems = findViewById(R.id.recycleritemspersale)


        if(idSaleAdmin.equals(userId)){
            editSaleBtn.visibility = View.VISIBLE
            addItemBtn.visibility = View.VISIBLE
            viewSoldBtn.visibility=View.VISIBLE
        }



        firestore.collection("sales").document(idSale).get().addOnSuccessListener {doc->
            aSale = Sale()
            aSale = doc.toObject(Sale::class.java)!!
            aSale.saleId = idSale

            //show the textview for the sale and image
            Picasso
                .get()
                .load(aSale.img)
                .into(salePoster);
            val descView: String = " TITLE: "+aSale.name+"\n"+" DESC: "+aSale.description+"\n"
            titleNDescView.text = aSale.name
            val addrStr =" ADDRESS: "+aSale.address
           // addrView.text = addrStr
            desctxt= descView+addrStr

            //the button to the address
            //open the recycler view adapter for items list

            firestore.collection("sales").document(idSale).collection("items").get().addOnSuccessListener { docs->
                itemsL= ArrayList()

                for ( dsnap: DocumentSnapshot in docs){
                    var eachItem = Saleitem()
                    eachItem = dsnap.toObject(Saleitem::class.java)!!
                    eachItem.itemId =dsnap.id
                    eachItem.saleId = idSale
                    eachItem.adminId = userId.toString()
                    eachItem.priceBids=ArrayList()
                    eachItem.addr = aSale.address



                    if(!eachItem.sold) {
                        itemsL.add(eachItem)
                    }
                }
                recycItems.layoutManager = LinearLayoutManager(this)

                val itAdapter = ItemViewAdapter(itemsL)

                // Setting the Adapter with the recyclerview
                recycItems.adapter = itAdapter
            }


        }


        moreBtn.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("More Item Info")

// Set up the input
            // val input = EditText(holder.itemView.context)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//            input.setHint("PLACE A BID")
//            input.inputType = InputType.TYPE_CLASS_NUMBER
//            builder.setView(input)

            builder.setMessage(desctxt)

// Set up the buttons
            builder.setPositiveButton("DONE", DialogInterface.OnClickListener { dialog, which ->

            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()


        }


        goToAddr.setOnClickListener {
            var intentMaps: Intent =  Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=${aSale.address}"));
            startActivity(intentMaps)
        }

        editSaleBtn.setOnClickListener {
            var intentN= Intent(this, EditSaleActivity::class.java)
            intentN.putExtra("saleId",idSale)
            startActivity(intentN)
        }

        addItemBtn.setOnClickListener {
            var intentN1= Intent(this, AddItemActivity::class.java)
            Log.d("saleactivity 23", "onCreate: sale id $idSale")
            intentN1.putExtra("saleId",idSale)
            intentN1.putExtra("saleaddr",aSale.address)

            startActivity(intentN1)
        }

        viewSoldBtn.setOnClickListener {
            var intentN2= Intent(this, SoldActivity::class.java)
            intentN2.putExtra("saleId",idSale)
            intentN2.putExtra("type","seller")

            startActivity(intentN2)
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