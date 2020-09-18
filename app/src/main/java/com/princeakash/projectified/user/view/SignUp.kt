package com.princeakash.projectified.user.view

import android.content.Context
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

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone:EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword:EditText
    private lateinit var editTextReEnterPassword:EditText
    private var SignUpButton: Button? = null

    //View Models and Fun Objects

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var errorString: String
    private var errorShown = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState!=null)
            errorShown = savedInstanceState.getBoolean("Error", false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        profileViewModel.responseSignUp.observe(viewLifecycleOwner, {
            Toast.makeText(context, it.message, LENGTH_SHORT).show();
        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            //errorString = it
            //if(!errorShown){
            if(it!=null){
                Toast.makeText(context, it, LENGTH_SHORT).show()
                profileViewModel.errorString.postValue(null)
            }
            //errorShown = true;
            //}
        })
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
        if (editTextReEnterPassword!!.text == null || editTextReEnterPassword!!.text!!.equals("") || editTextPassword!!.text.toString()!=editTextReEnterPassword!!.text.toString()) {
            editTextReEnterPassword!!.error = "Passwords didnt match"
            return
        }
        if (editTextName!!.text == null || editTextName!!.text!!.equals("")) {
            editTextName!!.error = "Enter Name."
            return
        }
        if (editTextPhone!!.text == null || editTextPhone!!.text!!.equals("") || editTextPhone.length()!=10 ) {
            editTextPhone!!.error = "Enter phone Number."
            return
        }

        val phoneNumber =editTextPhone!!.text!!.toString()
        val email = editTextEmail!!.text!!.toString()
        val password = editTextPassword!!.text!!.toString()
        val name= editTextName!!.text!!.toString()

        val signUp = BodySignUp(name, email, phoneNumber,password)
        profileViewModel.signUp(signUp)

        /*val nextFrag = CreateProfileFragment()
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()*/


    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("Error", errorShown)
        super.onSaveInstanceState(outState)
    }
}