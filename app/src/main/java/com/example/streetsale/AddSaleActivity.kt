package com.example.streetsale

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.HashMap

class AddSaleActivity : AppCompatActivity() {


    lateinit var addSaleName:  EditText
    lateinit var addSaleDesc: EditText
    lateinit var addSaleAddress: EditText
    lateinit var addPosterBtn :Button

    lateinit var addSaleBtn: Button
    lateinit var seeAddedPoster: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var newSale: Sale
    lateinit var cUser: FirebaseUser
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
     var frstorage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sale)

        addSaleName = findViewById(R.id.addsalename)
        addSaleDesc = findViewById(R.id.addsaledescription)
        addSaleAddress = findViewById(R.id.addsaleaddress)
        addPosterBtn = findViewById(R.id.addsaleposter)
        addSaleBtn = findViewById(R.id.addsalebutton)
        seeAddedPoster = findViewById(R.id.addposterview)
//











        addPosterBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        addSaleBtn.setOnClickListener {
            cUser = FirebaseAuth.getInstance().currentUser!!
            newSale = Sale()
            var name = addSaleName.text.toString()
            newSale.name = name
            var dsc = addSaleDesc.text.toString()
            newSale.description = dsc
            var addr = addSaleAddress.text.toString()
            newSale.address = addr
            newSale.searchFields = newSale.name + " " + newSale.description + " " + newSale.address
            newSale.admin = cUser.uid.toString()
            val newUser = hashMapOf<String, Any>(
                "name" to name,
                "description" to dsc,
                "address" to addr,
                "searchFields" to newSale.name + " " + newSale.description + " " + newSale.address,
                "admin" to cUser.uid.toString(),
                "img" to ""
            )

            uploadImage(newSale)

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
                    finish()
                    return true
                }
            }
            return super.onContextItemSelected(item)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            seeAddedPoster.visibility = View.VISIBLE
            seeAddedPoster.setImageURI(imageUri)
        }
    }

    private fun addUploadRecordToDb(newSale1: Sale){
        FirebaseFirestore.getInstance().collection("sales").add(newSale1).addOnSuccessListener {

            var saleId = it.id

            //add image to firebase storage

            //add image link to firestore document
            //go to main page
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun uploadImage(newSale2: Sale){

        if(imageUri != null){
            val ref = frstorage.reference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(imageUri!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val imgstring = downloadUri.toString()
                    newSale2.img = imgstring
                    addUploadRecordToDb(newSale2)
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

}