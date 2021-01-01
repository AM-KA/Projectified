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
import com.princeakash.projectified.user.model.LoginBody
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import com.princeakash.projectified.user.model.ResponseLogin
import kotlinx.android.synthetic.main.signin_user.view.*
import kotlinx.android.synthetic.main.signin_user.view.progress_circular_layout
import kotlinx.android.synthetic.main.verifyphoneno.view.*


class VerifyEmailOtpFragment : Fragment() {

    private lateinit var editTextOtp: EditText
    private lateinit var verifyButton: Button
    private lateinit var progressCircularLayout: RelativeLayout

    //View Models and Fun Objects
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.verifyphoneno, container, false)
        editTextOtp = v.editTextOtp
        verifyButton = v.VerifyButton
        progressCircularLayout = v.progress_circular_layout

        verifyButton.setOnClickListener {
            verifyOtp()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        profileViewModel.responseVerifyOtp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {responseVerifyOtp->
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, responseVerifyOtp.message, LENGTH_LONG).show()
                if (responseVerifyOtp.code.equals("200")){
                    //TODO: Allowed to update password
                    findNavController().navigate(R.id.verify_to_password_reset)
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

    fun verifyOtp() {
        if (editTextOtp!!.text == null || editTextOtp!!.text!!.equals("") || editTextOtp!!.text!!.length<6) {
            editTextOtp!!.error = "Enter a valid OTP."
            return
        }

        val resetOtp = editTextOtp!!.text!!.toString()
        progressCircularLayout.visibility = View.VISIBLE
        profileViewModel.verifyOtp(resetOtp)
    }

    companion object {
        val USER_NAME = "UserName"
        val USER_ID = "UserId"
    }
}
