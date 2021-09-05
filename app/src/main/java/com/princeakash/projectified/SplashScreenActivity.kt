package com.princeakash.projectified

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.user.view.CreateProfileActivity
import com.princeakash.projectified.user.view.LoginSignupActivity
import com.princeakash.projectified.user.viewmodel.ProfileViewModel

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep((3 * 1000).toLong())

                    if (!profileViewModel.loginStatus) {
                        takeToLogin()
                    } else if (profileViewModel.isJWTExpired()) {
                        takeToLogin()
                    } else if (!profileViewModel.profileStatus) {
                        takeToCreateProfile()
                    } else {
                        takeToHome()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        //Start Thread
        background.start()
    }

    fun takeToLogin() {
        startActivity(Intent(this, LoginSignupActivity::class.java))
        finish()

    }

    fun takeToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun takeToCreateProfile(){
        startActivity(Intent(this, CreateProfileActivity::class.java))
        finish()
    }
}