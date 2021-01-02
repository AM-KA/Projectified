package com.princeakash.projectified.user.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.model.BodySignUp
import com.princeakash.projectified.user.model.LoginBody
import com.princeakash.projectified.user.viewmodel.ProfileViewModel
import com.princeakash.projectified.user.model.ResponseLogin
import com.princeakash.projectified.user.view.LoginHomeFragment.Companion.USER_NAME
import kotlinx.android.synthetic.main.verifyphoneno.view.*
import java.util.concurrent.TimeUnit


class VerifyOtpFragment : Fragment() {


    private lateinit var EditTextOtp: AutoCompleteTextView
    private lateinit var VerifyButton: Button
    private lateinit var progressCircularLayout: RelativeLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var responseLogin: ResponseLogin
    private lateinit var profileViewModel: ProfileViewModel

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var phoneno: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.verifyphoneno, container, false)

        EditTextOtp = v.editTextOtp
        VerifyButton = v.VerifyButton
        progressCircularLayout = v.progress_circular_layout
        progressCircularLayout.visibility = View.INVISIBLE

        VerifyButton.setOnClickListener {
            if (EditTextOtp.text!!.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, EditTextOtp.text.toString())
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(context, "Enter the OTP Received", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        profileViewModel.bodySignUp().observe(viewLifecycleOwner, {
            if(savedInstanceState==null){
                //First loadup, hence safe to send OTP
                phoneno = "+91" + it.phone
                sendVerificationCodetoTheUser()
            }
        })

        profileViewModel.responseSignUp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                if (it.code == 200) {
                    profileViewModel.logIn()
                }
            }
        })

        profileViewModel.responseLogin().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                responseLogin = it
                if (responseLogin.code != 200) {
                    Toast.makeText(context, responseLogin.message, Toast.LENGTH_SHORT).show()
                } else {
                    if (responseLogin.profileCompleted!!) {
                        //Navigate to main activity
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        //Navigate to CreateProfileFragment
                        val bundle = Bundle()
                        bundle.putString(USER_NAME, responseLogin.name)
                        val intent = Intent(requireActivity(), CreateProfileActivity::class.java)
                        intent.putExtra(USER_NAME, bundle)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }

            }

        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun sendVerificationCodetoTheUser() {
        auth = Firebase.auth
        phoneno?.let {
            val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(it)
                    .setTimeout(60, TimeUnit.SECONDS)
                    .setCallbacks(callbacks)
                    .setActivity(requireActivity())
                    .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }// OnVerificationStateChangedCallbacks

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            Log.d(TAG, "onVerificationCompleted:$credential")
            Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.verify_otp_to_home)
        }

        override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
            Toast.makeText(context, "Code Sent", Toast.LENGTH_SHORT).show()
            // ...
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth = Firebase.auth
        auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")

                        Toast.makeText(context, "Sign Up Done Successfully", Toast.LENGTH_SHORT).show()

                        val user = task.result?.user

                        profileViewModel.signUp()

                        // ...
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid


                        }
                    }
                }
    }
}

