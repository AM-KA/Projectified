package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import com.princeakash.projectified.user.model.ResponseSignUp
import kotlinx.android.synthetic.main.sign_up_user.view.*


class SignUp : Fragment() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextReEnterPassword: EditText
    private lateinit var SignUpButton: Button
    private lateinit var phoneNumber: String
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var password: String
    private lateinit var progressCircularLayout: RelativeLayout

    ///ViewModels
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var responsechecksignUp: ResponseSignUp
    private var errorShown = false
    private var code: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.sign_up_user, container, false)
        editTextEmail = v.editTextEmail1
        editTextName = v.editTextName1
        editTextPhone = v.editTextPhoneNumber1
        editTextPassword = v.editTextPassword1
        editTextReEnterPassword = v.editTextReEnterPassword1
        progressCircularLayout = v.progress_circular_layout
        SignUpButton = v.SignUpButton
        SignUpButton.setOnClickListener {
            startValidationForOtpVerification()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        profileViewModel.responsecheckSignUp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                responsechecksignUp = it
                code = it.code
                if (code == 300) {
                    Toast.makeText(context, "You have already signed up. Please log in to continue ", LENGTH_SHORT).show()
                } else if (code == 200) {
                    findNavController().navigate(R.id.home_to_verify_otp)
                } else {
                    Toast.makeText(context, "Error", LENGTH_SHORT).show()
                }
            }
        })

        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startValidationForOtpVerification() {
        if (editTextEmail!!.text == null || editTextEmail!!.text!!.equals("")) {
            editTextEmail!!.error = "Enter Phone Number."
            return
        }

        if (editTextPassword!!.text == null || editTextPassword!!.text!!.equals("")) {
            editTextPassword!!.error = "Enter Password."
            return
        }
        if (editTextReEnterPassword!!.text == null || editTextReEnterPassword!!.text!!.equals("") || editTextPassword!!.text.toString() != editTextReEnterPassword!!.text.toString()) {
            editTextReEnterPassword!!.error = "Passwords didnt match"
            return
        }
        if (editTextName!!.text == null || editTextName!!.text!!.equals("")) {
            editTextName!!.error = "Enter Name."
            return
        }
        if (editTextPhone!!.text == null || editTextPhone!!.text!!.equals("") || editTextPhone.length() != 10) {
            editTextPhone!!.error = "Enter phone Number."
            return
        }

        phoneNumber = editTextPhone!!.text!!.toString()
        email = editTextEmail!!.text!!.toString()
        password = editTextPassword!!.text!!.toString()
        name = editTextName!!.text!!.toString()

        progressCircularLayout.visibility = View.VISIBLE
        profileViewModel.checksignup(name, email, phoneNumber, password)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("Error", errorShown)
        super.onSaveInstanceState(outState)
    }
}



