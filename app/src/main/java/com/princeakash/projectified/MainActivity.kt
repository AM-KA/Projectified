package com.princeakash.projectified

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.princeakash.projectified.Faq.FaqActivity
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateViewModel

import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterViewModel
import com.princeakash.projectified.user.viewmodel.ProfileViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var profileViewModel:ProfileViewModel
    private lateinit var navigationView: NavigationView

    private lateinit var recruiterViewModel: RecruiterViewModel
    private lateinit var candidateViewModel: CandidateViewModel
    private lateinit var switch: SwitchMaterial
    private var isNightModeOn: Boolean = false;

    var toolbar: Toolbar? = null
    var drawerLayout: DrawerLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kk)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        recruiterViewModel = ViewModelProvider(this).get(RecruiterViewModel::class.java)
        candidateViewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)
        recruiterViewModel.issueInitialInstructions()
        candidateViewModel.issueInitialInstructions()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigation_view)
        //switch = navigationView.findViewById(R.id.switch_darkMode)
        navigationView.setNavigationItemSelectedListener { onNavigationItemSelected(it) }

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

        switch = navigationView.getHeaderView(0).findViewById(R.id.switch_darkMode)

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

        isNightModeOn = profileViewModel.getDarkModeStatus()

        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        switch.setOnClickListener(View.OnClickListener {
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                profileViewModel.setDarkModeStatus(false);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                profileViewModel.setDarkModeStatus(true)
            }
        })
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

