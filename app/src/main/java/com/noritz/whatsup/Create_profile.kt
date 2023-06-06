package com.noritz.whatsup

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class Create_profile : AppCompatActivity() {

    //var bind:ActivitySetupProfileBinding = null
    private lateinit var auth : FirebaseAuth
    private var fDB : FirebaseDatabase? = null
    private var fStorage : FirebaseStorage? = null
    var img : Uri? = null
    private lateinit var imageView : ImageView
    private lateinit var nameButton : Button
    private lateinit var nameEditText: EditText

    //dialog

    @SuppressLint("SuspiciousIndentation") // a voir
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile)
        initialisation()
        imageView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45) // why 45
        }
        nameButton.setOnClickListener {
            val username : String = nameEditText.text.toString()
            if(username.isEmpty()){
                nameEditText.setError("Please type your username")
            }
            if(img != null){
             val storageRef = fStorage!!.reference.child("Profile").child(auth!!.uid!!) //a voir
                storageRef.putFile(img!!).addOnCompleteListener { task ->
                    var usedImg : String? = null
                    if(task.isSuccessful) {
                        storageRef.downloadUrl.addOnCompleteListener { uri ->
                           usedImg = uri.toString()
                        }
                    }

                    else {
                        usedImg = "No Image"
                    }

                    val user = User(
                        auth.uid,
                        nameEditText.text.toString(),
                        auth.currentUser!!.phoneNumber,
                        usedImg
                    )
                    fDB!!.reference.child("Users").child(auth.uid.toString()).setValue(user).addOnCompleteListener {
                        val intent = Intent(this@Create_profile, Menu::class.java)
                        startActivity(intent)
                        finish()
                    } // a voir
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if(data.data != null){
                val uri = data.data //filepath
                val fStorage = FirebaseStorage.getInstance()
                val time = Date().time
                val storageRef = fStorage.reference.child("Profile").child(time.toString()+"")
                storageRef.putFile(uri!!).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        storageRef.downloadUrl.addOnCompleteListener { uri ->
                            val filePath = uri.toString()
                            val obj = HashMap<String, Any>()
                            obj["image"] = filePath
                            fDB!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!) // don't know why he did that
                                .updateChildren(obj).addOnSuccessListener {  }
                        }
                    }
                }
                imageView.setImageURI(data.data) // if doesn't work use findviewbyid
                img = data.data
            }
        }

    }

    private fun initialisation()    {
        auth = FirebaseAuth.getInstance()
        fDB = FirebaseDatabase.getInstance()
        fStorage = FirebaseStorage.getInstance()
        imageView = findViewById<ImageView?>(R.id.profilePic)
        nameButton = findViewById(R.id.nameButton)
        nameEditText = findViewById(R.id.nameEditText)
    }

}