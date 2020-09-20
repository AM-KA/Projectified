package com.princeakash.projectified.user.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.LoginBody
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseLogin
import kotlinx.android.synthetic.main.signin_user.view.*


class LoginFragment : Fragment() {

        private var editTextEmail: EditText? = null
        private var editTextPassword: EditText? = null
        private var LogInButton: Button? = null
        private var SignUpButton: Button? = null

        //View Models and Fun Objects

        private lateinit var profileViewModel: ProfileViewModel
        private lateinit var responseLogin: ResponseLogin

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                val v = inflater.inflate(R.layout.signin_user, container, false)
                editTextEmail = v.editTextEmail
                editTextPassword = v.editTextPassword
                LogInButton = v.LogInButton
                SignUpButton = v.SignUpButton

                LogInButton?.setOnClickListener {
                        displayHomeScreen()
                }
                SignUpButton?.setOnClickListener {
                        requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_initial, SignUp(), "LoginFragment")
                                //.addToBackStack(null)
                                .commit()
                }
                return v
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
                profileViewModel.responseLogin.observe(viewLifecycleOwner, {
                        it?.getContentIfNotHandled()?.let {
                                responseLogin = it
                                if(responseLogin.code!=200){
                                        Toast.makeText(context, responseLogin.message, LENGTH_SHORT).show()
                                }else{
                                        profileViewModel.setToken(responseLogin.token)
                                        profileViewModel.setLoginStatus(true)
                                        if(responseLogin.profileCompleted){
                                                //Navigate to main activity
                                                val intent = Intent(activity, MainActivity::class.java)
                                                startActivity(intent)
                                        }else{
                                                //Navigate to CreateProfileFragment
                                                responseLogin.profile?.let {
                                                        profileViewModel.setLocalProfile(it)
                                                }
                                                val bundle = Bundle()
                                                bundle.putString(USER_NAME, responseLogin.userName)
                                                requireActivity().supportFragmentManager.beginTransaction()
                                                        .replace(R.id.fragment_initial, CreateProfileFragment::class.java, bundle, "LoginFragment")
                                                        .addToBackStack(null)
                                                        .commit()
                                        }
                                }
                        }
                })
        }

        fun displayHomeScreen() {
                if (editTextEmail!!.text == null || editTextEmail!!.text!!.equals("")) {
                        editTextEmail!!.error = "Enter Phone Number."
                        return
                }
                if (editTextPassword!!.text == null || editTextPassword!!.text!!.equals("")) {
                        editTextPassword!!.error = "Enter Password."
                        return
                }
                val email = editTextEmail!!.text!!.toString()
                val password = editTextPassword!!.text!!.toString()

                val logIn = LoginBody(email, password)
                profileViewModel.logIn(logIn)
        }

        companion object{
                val USER_NAME = "UserName"
        }
}
