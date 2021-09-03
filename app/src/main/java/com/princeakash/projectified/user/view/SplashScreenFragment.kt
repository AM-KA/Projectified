package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.auth0.android.jwt.JWT
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.viewmodel.ProfileViewModel

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        val background = object : Thread() {
            override fun run() {
                try {
                    //Sleep 3 seconds
                    sleep((3 * 1000).toLong())
                    val loginStatus = profileViewModel.getLoginStatus()
                    if (!loginStatus) {
                        takeToLogin()
                    } else {
                        val token = profileViewModel.getToken()
                        val jwt = JWT(token)
                        val profileStatus = profileViewModel.getProfileStatus()
                        if (jwt.isExpired((10).toLong())) {
                            takeToLogin()
                        } else if (!profileStatus) {
                            takeToCreateProfile()
                        } else {
                            takeToHome()
                        }
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
        startActivity(Intent(requireActivity(), LoginSignupActivity::class.java))
        requireActivity().finish()

    }

    fun takeToHome() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    fun takeToCreateProfile(){
        startActivity(Intent(requireActivity(), CreateProfileActivity::class.java))
        requireActivity().finish()
    }
}


