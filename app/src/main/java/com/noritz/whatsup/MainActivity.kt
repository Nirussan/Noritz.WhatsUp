package com.noritz.whatsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.noritz.whatsup.databinding.ActivityMainBinding;

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }

    private fun envoyerMainActivity() {
        startActivity(Intent(this,Create_profile::class.java))
    }

       /* navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle item 1 selection
                    val intent = Intent(this, Chats::class.java)
                    startActivity(intent)
                }
                R.id.settings -> {
                    // Handle item 2 selection
                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                }
                // Add more cases for other menu items as needed
            }
            // Close the navigation drawer
            menuItem.isChecked = true
            //drawerLayout.closeDrawers()
            true
        }*/

    /*private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Chats())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.home -> replaceFragment(Chats())
                R.id.settings -> replaceFragment(Settings())
                else ->{



                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }*/
}