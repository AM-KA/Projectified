package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.SignUpUserBinding
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import com.princeakash.projectified.user.model.ResponseSignUp


class SignUpHomeFragment : Fragment() {

    ///ViewModels
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var responseCheckSignUp: ResponseSignUp
    private lateinit var binding: SignUpUserBinding

    private var errorShown = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        =  inflater.inflate(R.layout.sign_up_user, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = SignUpUserBinding.bind(view)
        binding.SignUpButton.setOnClickListener { startValidationForOtpVerification() }

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        profileViewModel.responsecheckSignUp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                responseCheckSignUp = response
                when(response.code){
                    300 -> Toast.makeText(context, "You have already signed up. Please log in to continue ", LENGTH_SHORT).show()
                    200 -> findNavController().navigate(R.id.home_to_verify_otp)
                    else -> Toast.makeText(context, "Error", LENGTH_SHORT).show()
                }
            }
        })

        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_SHORT).show()
            }
        })
    }

    private fun startValidationForOtpVerification() {
        binding.apply{
            if (editTextEmail.text.toString().isEmpty()){
                editTextEmail.error = "Enter Phone Number."
                return
            }

            if (editTextPassword.text.toString().isEmpty()){
                editTextPassword.error = "Enter Password."
                return
            }
            if (editTextReEnterPassword.text.toString().isEmpty() || editTextPassword.text.toString() != editTextReEnterPassword.text.toString()) {
                editTextReEnterPassword.error = "Passwords didn't match"
                return
            }
            if (editTextName.text.toString().isEmpty()){
                editTextName.error = "Enter Name."
                return
            }
            if (editTextPhone.text.toString().isEmpty() || editTextPhone.length() != 10) {
                editTextPhone.error = "Enter phone Number."
                return
            }

            progressCircularLayout.visibility = View.VISIBLE
            profileViewModel.checksignup(
                editTextName.text!!.toString(), editTextEmail.text!!.toString(),
                editTextPhone.text!!.toString(), editTextPassword.text!!.toString()
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("Error", errorShown)
        super.onSaveInstanceState(outState)
    }
}