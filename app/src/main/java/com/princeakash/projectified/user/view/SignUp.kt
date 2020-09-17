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
import kotlinx.android.synthetic.main.signin_user.view.*


class SignUp : Fragment(){


    private var editTextName: EditText? = null
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


        editTextEmail = v.editTextEmail
        editTextName = v.editTextName
        editTextPassword=v.editTextPassword
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
        val email = editTextEmail!!.text!!.toString()
        val password = editTextPassword!!.text!!.toString()
        val name= editTextName!!.text!!.toString()

        val signUp = BodySignUp(name, email, password)
        profileViewModel!!.signUp((signUp))

        /*val nextFrag = CreateProfileFragment()
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()*/


    }




}