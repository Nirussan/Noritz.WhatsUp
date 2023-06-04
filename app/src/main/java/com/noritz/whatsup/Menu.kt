package com.noritz.whatsup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Menu : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var logoutBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.menu)

            auth = FirebaseAuth.getInstance()
            logoutBtn = findViewById(R.id.buttonSignOut)
            
            logoutBtn.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this, Connexion::class.java))
            }
    }
}