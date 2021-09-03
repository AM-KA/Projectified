package com.princeakash.projectified

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.princeakash.projectified.databinding.ActivityMainBinding
import com.princeakash.projectified.faq.FaqActivity

import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel
import com.princeakash.projectified.user.viewmodel.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var profileViewModel:ProfileViewModel
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private var isNightModeOn: Boolean = false;

    private lateinit var binding: ActivityMainBinding
    private lateinit var switch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        recruiterCandidateViewModel = ViewModelProvider(this).get(RecruiterCandidateViewModel::class.java)
        recruiterCandidateViewModel.issueInitialInstructions()
        setSupportActionBar(binding.toolbar)

        binding.navigationView.setNavigationItemSelectedListener { onNavigationItemSelected(it) }

        binding.bottomNavigation.setupWithNavController(findNavController(R.id.fragment))

        switch = binding.navigationView.getHeaderView(0).findViewById(R.id.switch_darkMode)

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ){}

        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(drawerToggle)
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

