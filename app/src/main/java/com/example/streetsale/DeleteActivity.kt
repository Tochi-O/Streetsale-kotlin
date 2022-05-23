package com.example.streetsale

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class DeleteActivity : AppCompatActivity() {

    var itemId:String=""
    var saleId: String=""
    var cUserId: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)



        var classDel= intent.getStringExtra("class").toString();
        saleId= intent.getStringExtra("saleId").toString();
        itemId= intent.getStringExtra("itemId").toString();


        if (classDel.equals("sale")){
            confirm("sale")

        }else if (classDel.equals("item")){

            confirm("item")

        }


    }


    fun confirm( type: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to delete this $type")

// Set up the input
//            val input = EditText(holder.itemView.context)
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//            input.setHint("PLACE A BID")
//            input.inputType = InputType.TYPE_CLASS_NUMBER
//            builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
           // newBid = Integer.parseInt(input.text.toString())
            if(type.equals("item")){
                //delete item from firestore
                FirebaseFirestore.getInstance().collection("sales").document(saleId).collection("items").document(itemId).delete().addOnSuccessListener {

                    var newIntent = Intent(this, SaleActivity::class.java)
                    newIntent.putExtra("saleIdAdmin", cUserId!!.uid)
                    newIntent.putExtra("saleId", saleId)
                    startActivity(newIntent)
                }

            }else if(type.equals("sale")){
                //delete sale from firestore
                FirebaseFirestore.getInstance().collection("sales").document(saleId).delete().addOnSuccessListener {
                    var newIntent = Intent(this,MainActivity::class.java)
                    startActivity(newIntent)
                }
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}