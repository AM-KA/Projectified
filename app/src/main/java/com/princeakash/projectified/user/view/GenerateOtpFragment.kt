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
import com.princeakash.projectified.user.model.BodyGenerateOtp
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_generate_otp.view.*


class GenerateOtpFragment : Fragment() {

    private lateinit var editTextEmail: EditText
    private lateinit var buttonGenerateOtp: Button
    private lateinit var progressCircularLayout: RelativeLayout

    //View Models and Fun Objects
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_generate_otp, container, false)
        editTextEmail = v.editTextEmail
        //editTextPassword = v.editTextPassword
        buttonGenerateOtp = v.buttonGenerateOtp
        progressCircularLayout = v.progress_circular_layout

        buttonGenerateOtp.setOnClickListener {
            generateOtp()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        profileViewModel.responseGenerateOtp().observe(viewLifecycleOwner, { it ->
            it?.getContentIfNotHandled()?.let {responseGenerateOtp->
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, responseGenerateOtp.message, LENGTH_LONG).show()
                if (responseGenerateOtp.code.equals("200")) {
                    findNavController().navigate(R.id.generate_to_verify_otp)
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

    fun generateOtp() {

        if (editTextEmail!!.text == null || editTextEmail!!.text!!.equals("")) {
            editTextEmail!!.error = "Enter Email"
            return
        }

        val email = editTextEmail!!.text!!.toString()
        progressCircularLayout.visibility = View.VISIBLE
        profileViewModel.generateOtp(email)
    }

    companion object {
        val USER_NAME = "UserName"
        val USER_ID = "UserId"
    }
}
