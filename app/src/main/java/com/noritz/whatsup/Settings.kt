package com.noritz.whatsup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth



class Settings : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth : FirebaseAuth
    private lateinit var logoutBtn : Button
    private lateinit var changeBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, ChatsList::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, Settings::class.java))
                    true
                }
                // Add more cases for other menu items as needed
                else -> false
            }
        }

        auth = FirebaseAuth.getInstance()
        logoutBtn = findViewById(R.id.buttonSignOut)
        changeBtn = findViewById(R.id.buttonProfile)

        logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Connexion::class.java))
            finish()
        }
        changeBtn.setOnClickListener {
            startActivity(Intent(this, Create_profile::class.java))
            finish()
        }

    }
}