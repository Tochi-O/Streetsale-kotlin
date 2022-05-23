package com.example.streetsale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class EditSaleActivity : AppCompatActivity() {

    lateinit var editTitle: EditText
    lateinit var editDesc: EditText
    lateinit var editAddr: EditText
    lateinit var editBtn: Button
    lateinit var idSale: String

    lateinit var newSale: Sale
    lateinit var aSale: Sale

    lateinit var cUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sale)

        cUser= FirebaseAuth.getInstance().currentUser!!
        //initialize the editext
        editTitle = findViewById(R.id.editsalename)
        editDesc = findViewById(R.id.editsaledescription)
        editAddr = findViewById(R.id.editsaleaddress)
        editBtn = findViewById(R.id.editsalebutton)


        idSale = intent.getStringExtra("saleId").toString()

        //get info of sale
        FirebaseFirestore.getInstance().collection("sales").document(idSale).get().addOnSuccessListener { doc ->
            aSale = Sale()
            aSale = doc.toObject(Sale::class.java)!!
            aSale.saleId = idSale



            editTitle.setText(aSale.name)
            editDesc.setText(aSale.description)
            editAddr.setText(aSale.address)


        }
        //fill the edit text
        //get new info in the edit text
        //make an updated sale object
        //so to sale page




        editBtn.setOnClickListener {
            newSale= Sale()
            newSale.name = editTitle.text.toString()
            newSale.description = editDesc.text.toString()
            newSale.address = editAddr.text.toString()
           // newSale.img=""
            newSale.admin=cUser.uid
            newSale.saleId=idSale
            newSale.searchFields = newSale.name+" "+newSale.description+" "+newSale.address
            val updates = hashMapOf<String, Any>(
                "name" to newSale.name,
                "description" to newSale.description,
                "address" to newSale.address,
                "searchFields" to newSale.searchFields
            )
            FirebaseFirestore.getInstance().collection("sales").document(idSale).update(
                updates).addOnSuccessListener {
                var intent= Intent(this, SaleActivity::class.java)
                intent.putExtra("saleId", idSale)
                intent.putExtra("saleIdAdmin", cUser.uid)
                startActivity(intent)
                finish()
            }

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