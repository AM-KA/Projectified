package com.princeakash.projectified

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.princeakash.projectified.Faq.FaqActivity

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    var toolbar: Toolbar? = null
    var drawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kk)
        //getSupportActionBar()!!.hide()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
        //Setting the navigation controller to Bottom Nav
        //Setting up the action bar
        //NavigationUI.setupActionBarWithNavController(this, navController)

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ){}

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout?.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


   fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.AboutUs -> {
                val intent = Intent(this@MainActivity, AboutUsActivity::class.java)
                startActivity(intent)
            }
            R.id.Ratings -> {


            }
            R.id.LogOut -> {


            }
            R.id.Share -> {
                TODO("Sharing Link")
            }
            R.id.Faq -> {
                val intent = Intent(this@MainActivity, FaqActivity::class.java)
                startActivity(intent)
            }
            R.id.Help -> {

                val address = "amkafoundation@gmail.com"
                //TODO("Gmail of amka Foundation ")
                val subject: String = "Give Review For our app"
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, address)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }


        }
        return true
    }
}

