package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.SigninUserBinding
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import com.princeakash.projectified.user.model.ResponseLogin

class LoginHomeFragment : Fragment() {

    //ViewModels
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var responseLogin: ResponseLogin
    private lateinit var binding: SigninUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.signin_user, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()

        binding = SigninUserBinding.bind(view)
        binding.LogInButton.setOnClickListener { displayHomeScreen() }
        binding.textViewForgotPassword.setOnClickListener { takeToGenerateOtp() }
    }

    private fun subscribeToObservers() {
        profileViewModel.responseLogin().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { respLogin ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                responseLogin = respLogin
                if (responseLogin.code != 200) {
                    Toast.makeText(context, responseLogin.message, LENGTH_LONG).show()
                } else {
                    
                    if (responseLogin.profileCompleted!!)
                        startActivity(Intent(activity, MainActivity::class.java))
                    else
                        startActivity(Intent(activity, CreateProfileActivity::class.java))

                    requireActivity().finish()
                }
            }
        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })
    }

    private fun displayHomeScreen() {
        binding.apply {
            if (editTextEmail.text.toString().isEmpty()){
                editTextEmail.error = "Enter email."
                return
            }
            if (editTextPassword.text.toString().isEmpty()){
                editTextPassword.error = "Enter password."
                return
            }
            val email = editTextEmail.text!!.toString()
            val password = editTextPassword.text!!.toString()

            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.logIn(email, password)
        }
    }

    private fun takeToGenerateOtp(){
        findNavController().navigate(R.id.home_to_generate_otp)
    }

    companion object {
        const val USER_NAME = "UserName"
    }
}
