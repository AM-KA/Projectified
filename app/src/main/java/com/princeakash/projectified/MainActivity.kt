package com.princeakash.projectified

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.princeakash.projectified.Faq.FaqActivity
import com.princeakash.projectified.user.viewmodel.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var navigationView: NavigationView

    var toolbar: Toolbar? = null
    var drawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kk)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { onNavigationItemSelected(it) }

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.myOffersFragment, R.id.myApplicationsFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

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
                profileViewModel.setLoginStatus(false)
                val intent = Intent(this, InitialActivity::class.java)
                startActivity(intent)
                finish()
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
                val subject: String = "App Review: Projectified"
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$address")
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

