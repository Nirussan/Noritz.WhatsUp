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
import com.noritz.whatsup.databinding.CreateProfileBinding
import java.util.Date

class Create_profile : AppCompatActivity() {

    var bind: CreateProfileBinding? = null
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
        bind = CreateProfileBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        initialisation()
        bind!!.profilePic.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45) // why 45
        }
        bind!!.nameButton.setOnClickListener {
            val username : String = nameEditText.text.toString()
            if(username.isEmpty()){
                bind!!.nameEditText.setError("Please type your username")
            }
            var usedImg : String? = null
            if(img != null){
                            usedImg = img.toString()
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
                        val intent = Intent(this@Create_profile, ChatsList::class.java)
                        startActivity(intent)
                        finish()
                    } // a voir
                }
            }

        //envoyerMainActivity()



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

    private fun envoyerMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
    }

}