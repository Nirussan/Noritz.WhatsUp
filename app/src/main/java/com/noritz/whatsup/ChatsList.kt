package com.noritz.whatsup

//import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noritz.whatsup.databinding.ChatsBinding

//
class ChatsList : AppCompatActivity() {

    var bind : ChatsBinding? = null
    var fDB : FirebaseDatabase? = null
    var userList : ArrayList<User>? = null
    var userAdapter : UserAdapter? = null
    var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ChatsBinding.inflate(layoutInflater)
        setContentView(bind!!.root)

        fDB = FirebaseDatabase.getInstance()
        userList = ArrayList<User>()
        userAdapter = UserAdapter(this@ChatsList, userList!!)

        val layoutManager = GridLayoutManager(this@ChatsList, 1)
        bind!!.chatsRecycler.layoutManager = layoutManager
        fDB!!.reference.child("Users").child(FirebaseAuth.getInstance().uid!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        bind!!.chatsRecycler.adapter = userAdapter
        fDB!!.reference.child("Users").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                userList!!.clear()
                for (snap in snapshot.children){
                    val  user : User? = snap.getValue(User::class.java)
                    if(!user!!.id.equals(FirebaseAuth.getInstance().uid)){
                        userList!!.add(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        var currentId = FirebaseAuth.getInstance().uid
        fDB!!.reference.child("State").child(currentId!!).setValue("online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        fDB!!.reference.child("State").child(currentId!!).setValue("offline")
    }

    private fun initialisation() {
        fDB = FirebaseDatabase.getInstance()
        userList = ArrayList<User>()
        userAdapter = UserAdapter(this@ChatsList, userList!!)
    }

}