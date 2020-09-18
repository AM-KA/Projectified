package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.princeakash.projectified.R
import com.princeakash.projectified.user.BodySignUp
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseSignUp
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.*
import kotlinx.android.synthetic.main.sign_up_user.*
import kotlinx.android.synthetic.main.sign_up_user.view.*
import kotlinx.android.synthetic.main.signin_user.view.*
import kotlinx.android.synthetic.main.signin_user.view.SignUpButton


class SignUp : Fragment(){

    private var editTextName: EditText? = null
    private var editTextPhone:EditText?=null
    private var editTextEmail: EditText? = null
    private var editTextPassword:EditText?=null
    private  var editTextReEnterPassword:EditText?=null
    private var SignUpButton: Button? = null

    //View Models and Fun Objects

    private val profileViewModel: ProfileViewModel? = null
    private val responseSignUp: ResponseSignUp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            val v = inflater.inflate(R.layout.sign_up_user, container, false)


        editTextEmail = v.editTextEmail1
        editTextName = v.editTextFullName1
        editTextPhone=v.editTextphonenumber1
        editTextPassword=v.editTextPassword1
        editTextReEnterPassword=v.editTextReEnterPassword1
        SignUpButton = v.SignUpButton

       SignUpButton?.setOnClickListener {

            displayHomeScreen()
        }

        return v
    }

    private  fun displayHomeScreen() {
        if (editTextEmail!!.text == null || editTextEmail!!.text!!.equals("")) {
            editTextEmail!!.error = "Enter Phone Number."
            return
        }

        if (editTextPassword!!.text == null || editTextPassword!!.text!!.equals("")) {
            editTextPassword!!.error = "Enter Password."
            return
        }
        if (editTextReEnterPassword!!.text == null || editTextReEnterPassword!!.text!!.equals("") || editTextPassword!=editTextReEnterPassword) {
            editTextReEnterPassword!!.error = "Passwords didnt match"
            return
        }
        if (editTextName!!.text == null || editTextName!!.text!!.equals("")) {
            editTextName!!.error = "Enter Name."
            return
        }
        if (editTextphonenumber1!!.text == null || editTextphonenumber1!!.text!!.equals("") || editTextphonenumber1.length()!=10 ) {
            editTextphonenumber1!!.error = "Enter phone Number."
            return
        }

        val phoneNumber =editTextphonenumber1!!.text!!.toString()
        val email = editTextEmail!!.text!!.toString()
        val password = editTextPassword!!.text!!.toString()
        val name= editTextName!!.text!!.toString()

        val signUp = BodySignUp(name, email, phoneNumber,password)
        profileViewModel!!.signUp((signUp))

        /*val nextFrag = CreateProfileFragment()
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()*/


    }




}