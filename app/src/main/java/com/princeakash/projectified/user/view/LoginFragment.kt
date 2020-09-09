package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.signin_user.view.*

class LoginFragment :Fragment() {


        private var editTextPhoneNumber: EditText?=null
        private var editTextPassword: EditText?=null
        private var LogInButton:Button?=null
        private var SignUpButton:Button?=null


        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

                val v=inflater.inflate(R.layout.signin_user,container,false)


                editTextPhoneNumber=v.editTextPhoneNumber
                editTextPassword=v.editTextPassword
                LogInButton=v.LogInButton
                SignUpButton=v.SignUpButton

                LogInButton?.setOnClickListener {

                        displayHomeScreen()
                }

















}