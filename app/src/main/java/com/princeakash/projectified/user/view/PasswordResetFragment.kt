package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_password_reset.view.*


class PasswordResetFragment : Fragment() {

    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonSetPassword: Button
    private lateinit var progressCircularLayout: RelativeLayout

    //View Models and Fun Objects
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_password_reset, container, false)
        editTextPassword = v.editTextPassword
        editTextConfirmPassword = v.editTextConfirmPassword
        buttonSetPassword = v.buttonSetPassword
        progressCircularLayout = v.progress_circular_layout

        buttonSetPassword.setOnClickListener {
            setNewPassword()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        profileViewModel.responseUpdatePassword().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {responseUpdatePassword->
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, responseUpdatePassword.message, LENGTH_LONG).show()
                if(responseUpdatePassword.code == "200"){
                    findNavController().navigate(R.id.reset_to_login)
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

    fun setNewPassword() {
        if (editTextPassword!!.text == null || editTextPassword!!.text!!.equals("")) {
            editTextPassword!!.error = "Enter password."
            return
        }

        if (editTextConfirmPassword!!.text == null || editTextConfirmPassword!!.text!!.equals("")) {
            editTextConfirmPassword!!.error = "Enter password again."
            return
        }

        if(editTextConfirmPassword!!.text.toString() != editTextPassword!!.text.toString()){
            editTextConfirmPassword!!.error = "Passwords don't match."
            return
        }

        val password = editTextPassword!!.text!!.toString()
        progressCircularLayout.visibility = View.VISIBLE
        profileViewModel.updatePassword(password)
    }

    companion object {
        val USER_NAME = "UserName"
        val USER_ID = "UserId"
    }
}
