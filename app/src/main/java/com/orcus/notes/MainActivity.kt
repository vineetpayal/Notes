package com.orcus.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.orcus.notes.ui.HomeFragment

private const val TAG = "mainActivity"

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting the navController for the navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)

        //Setting the toolbar title.
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when {
                arguments?.getInt("requestCode") == HomeFragment.EDIT_NOTE_REQUEST_CODE -> {
                    supportActionBar?.title = "Edit note"
                }
                arguments?.getInt("requestCode") == HomeFragment.ADD_NOTE_REQUEST_CODE -> {
                    supportActionBar?.title = "Add note"
                }
                else -> {
                    supportActionBar?.title = "Home"
                }
            }
        }

    }

    //This method enabled the back(or up) button on the toolbar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}