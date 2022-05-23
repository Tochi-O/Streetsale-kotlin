package com.example.streetsale

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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

class AddItemActivity : AppCompatActivity() {

    lateinit var addItemName: EditText
    lateinit var addItemDesc: EditText
    lateinit var addItemPrice: EditText
    lateinit var addPosterBtn : Button
    lateinit var addItemBtn: Button
    lateinit var seeAddedPoster: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var newItem: Saleitem
    lateinit var cUser: FirebaseUser
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var frstorage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var saleId: String
    lateinit var saleAddr: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        addItemName = findViewById(R.id.additemname)
        addItemDesc= findViewById(R.id.additemdescription)
        addItemPrice= findViewById(R.id.additemprice)
        addPosterBtn= findViewById(R.id.additemposter)
        addItemBtn = findViewById(R.id.additembutton)
        seeAddedPoster = findViewById(R.id.addposterview)

        saleId = intent.getStringExtra("saleId").toString()
        saleAddr = intent.getStringExtra("saleaddr").toString()


        Log.d("add item activity", "onCreate: sale id $saleId")








        addPosterBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        addItemBtn.setOnClickListener {
            cUser = FirebaseAuth.getInstance().currentUser!!
            newItem = Saleitem()
            newItem.name = addItemName.text.toString()
            newItem.description =addItemDesc.text.toString()
            newItem.price = addItemPrice.text.toString()
            //newItem.searchField = newSale.name+" "+newSale.description+" "+newSale.address
            newItem.adminId = cUser.uid
            newItem.saleId = saleId
            newItem.addr=saleAddr

            uploadImage(newItem)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            seeAddedPoster.visibility = View.VISIBLE
            seeAddedPoster.setImageURI(imageUri)
        }
    }

    private fun addUploadRecordToDb(newItem2: Saleitem){
        Log.d("saleid", "addUploadRecordToDb: 3 $saleId")

        firestore.collection("sales").document(saleId).collection("items").add(newItem2).addOnSuccessListener {

           // var saleId = it.id

            Log.d("saleid", "addUploadRecordToDb: 3 $saleId")
            var intent =Intent(this, SaleActivity::class.java)
            intent.putExtra("saleId",saleId)
            intent.putExtra("saleIdAdmin", cUser.uid)
            startActivity(intent)
            finish()

            //add image to firebase storage

            //add image link to firestore document
            //go to main page
        }
    }

    private fun uploadImage(newItem2: Saleitem){

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
                    newItem2.img = imgstring
                    addUploadRecordToDb(newItem2)
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