package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.user.LoginBody
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseLogin
import kotlinx.android.synthetic.main.frag_myapplicationdetail.*
import kotlinx.android.synthetic.main.signin_user.view.*

class LoginFragment :Fragment() {


        private var editTextEmail: EditText? = null
        private var editTextPassword: EditText? = null
        private var LogInButton: Button? = null
        private var SignUpButton: Button? = null

        //View Models and Fun Objects

        private val profileViewModel: ProfileViewModel? = null
        private val responseLogin: ResponseLogin? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
        }

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
                        TODO("Open Sign Up Screen Using transaction")

                }

                return v
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
                profileViewModel!!.logIn(logIn)



        }
}