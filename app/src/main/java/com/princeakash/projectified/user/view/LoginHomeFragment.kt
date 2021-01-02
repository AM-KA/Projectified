package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.model.LoginBody
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import com.princeakash.projectified.user.model.ResponseLogin
import kotlinx.android.synthetic.main.signin_user.view.*


class LoginHomeFragment : Fragment() {

    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var LogInButton: Button? = null
    private var textViewForgotPassword: TextView? = null
    private lateinit var progressCircularLayout: RelativeLayout

    //ViewModels
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var responseLogin: ResponseLogin

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.signin_user, container, false)
        editTextEmail = v.editTextEmail
        editTextPassword = v.editTextPassword
        LogInButton = v.LogInButton
        textViewForgotPassword = v.textViewForgotPassword
        progressCircularLayout = v.progress_circular_layout

        LogInButton?.setOnClickListener {
            displayHomeScreen()
        }

        textViewForgotPassword?.setOnClickListener {
            takeToGenerateOtp()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        profileViewModel.responseLogin().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                responseLogin = it
                if (responseLogin.code != 200) {
                    Toast.makeText(context, responseLogin.message, LENGTH_LONG).show()
                } else {
                    if (responseLogin.profileCompleted!!) {
                        //Navigate to main activity
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        //Navigate to CreateProfileFragment
                        val intent = Intent(requireActivity(), CreateProfileActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_LONG).show()
            }
        })
    }

    fun displayHomeScreen() {
        if (editTextEmail!!.text == null || editTextEmail!!.text!!.equals("")) {
            editTextEmail!!.error = "Enter email."
            return
        }
        if (editTextPassword!!.text == null || editTextPassword!!.text!!.equals("")) {
            editTextPassword!!.error = "Enter password."
            return
        }
        val email = editTextEmail!!.text!!.toString()
        val password = editTextPassword!!.text!!.toString()

        progressCircularLayout.visibility = View.VISIBLE
        profileViewModel.logIn(email, password)
    }

    fun takeToGenerateOtp(){
        findNavController().navigate(R.id.home_to_generate_otp)
    }

    companion object {
        val USER_NAME = "UserName"
        val USER_ID = "UserId"
    }
}
