package com.noritz.whatsup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.noritz.whatsup.databinding.ActivityMainBinding
import com.noritz.whatsup.databinding.MyChatsBinding
import java.util.Calendar
import java.util.Date

class MyChats : AppCompatActivity() {

    var binding :MyChatsBinding? = null
    var adapter :MessageAdapter? = null
    var messages :ArrayList<Message>? = null
    var senderRoom :String? = null
    var receiverRoom :String? = null
    var database :FirebaseDatabase? = null
    var storage :FirebaseStorage? = null
    var dialog :ProgressDialog? = null
    var senderUid :String? = null
    var receiverUid :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyChatsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolBar)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        dialog = ProgressDialog(this@MyChats)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        messages = ArrayList()
        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("image")
        binding!!.username.text = name
        Glide.with(this@MyChats).load(profile).placeholder(R.drawable.image)
            .into(binding!!.profilePic)
        binding!!.backSpace.setOnClickListener { finish() }
        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().uid
        database!!.reference.child("State").child(receiverUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        if (status == "offline") {
                            binding!!.state.visibility = View.GONE
                        } else {
                            binding!!.state.setText(status)
                            binding!!.state.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        adapter = MessageAdapter(this@MyChats, messages, senderRoom!!, receiverRoom!!)
        binding!!.recyler.layoutManager = LinearLayoutManager(this@MyChats)
        binding!!.recyler.adapter = adapter
        database!!.reference.child("Chats").child(senderRoom!!).child("message").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messages!!.clear()
                for(s in snapshot.children){
                    val message :Message? = s.getValue(Message::class.java)
                    message!!.messageId = s.key
                    messages!!.add(message)
                }
                adapter!!.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        binding!!.send.setOnClickListener {
            val messageTxt :String = binding!!.messageBox.text.toString()
            val date = Date()
            val message = Message(messageTxt,senderUid, date.time)
            binding!!.messageBox.setText("")
            val randomKey = database!!.reference.push().key
            val lastMsgObj = HashMap<String,Any>()
            lastMsgObj["lastMsg"] = message.message!!
            lastMsgObj["lastMsgTime"] = date.time
            database!!.reference.child("Chats").child(senderRoom!!).updateChildren(lastMsgObj)
            database!!.reference.child("Chats").child(receiverRoom!!).updateChildren(lastMsgObj)
            database!!.reference.child("Chats").child(senderRoom!!).child("messages").child(randomKey!!).setValue(message).addOnSuccessListener {
                database!!.reference.child("Chats").child(receiverRoom!!).child("message").child(randomKey).setValue(message).addOnSuccessListener {  }
            }
        }
        binding!!.attachement.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,25)
        }
        val handler = Handler()
        binding!!.messageBox.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                database!!.reference.child("State").child(senderUid!!).setValue("Typing...")
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTyping,1000)
            }
            var userStoppedTyping = Runnable {
                database!!.reference.child("State").child(senderUid!!).setValue("Online")
            }
        })
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25){
            if (data != null){
                if (data.data != null){
                    val selectedImage = data.data
                    val calendar = Calendar.getInstance()
                    val refence = storage!!.reference.child("Chats").child(calendar.timeInMillis.toString()+"")
                    dialog!!.show()
                    refence.putFile(selectedImage!!).addOnCompleteListener { task->
                        dialog!!.dismiss()
                        if (task.isSuccessful){
                            refence.downloadUrl.addOnSuccessListener { uri->
                                val filePath = uri.toString()
                                val messageTxt :String = binding!!.messageBox.text.toString()
                                val date = Date()
                                val message = Message(messageTxt,senderUid, date.time)
                                message.message = "photo"
                                message.imageUrl = filePath
                                binding!!.messageBox.setText("")
                                val randomKey = database!!.reference.push().key
                                val lastMsgObj = HashMap<String,Any>()
                                lastMsgObj["lastMsg"] = message.message!!
                                lastMsgObj["lastMsgTime"] = date.time
                                database!!.reference.child("Chats").updateChildren(lastMsgObj)
                                database!!.reference.child("Chats").child(receiverRoom!!).updateChildren(lastMsgObj)
                                database!!.reference.child("Chats").child(senderRoom!!).child("messages").child(randomKey!!).setValue(message).addOnSuccessListener {
                                    database!!.reference.child("Chats").child(receiverRoom!!).child("messages").child(randomKey).setValue(message).addOnSuccessListener {  }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("State").child(currentId!!).setValue("Online")
    }
    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("State").child(currentId!!).setValue("Offline")
    }
}
