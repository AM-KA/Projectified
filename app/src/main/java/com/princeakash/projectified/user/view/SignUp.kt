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
import com.princeakash.projectified.user.LoginBody
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseSignUp
import com.princeakash.projectified.user.view.VerifyOtpFragment.Companion.E_MAIL
import com.princeakash.projectified.user.view.VerifyOtpFragment.Companion.N_AME
import com.princeakash.projectified.user.view.VerifyOtpFragment.Companion.PASS_WORD
import com.princeakash.projectified.user.view.VerifyOtpFragment.Companion.PHONE_NO
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.*
import kotlinx.android.synthetic.main.sign_up_user.*
import kotlinx.android.synthetic.main.sign_up_user.view.*
import kotlinx.android.synthetic.main.signin_user.view.*



class SignUp : Fragment(){

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone:EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword:EditText
    private lateinit var editTextReEnterPassword:EditText
    private var SignUpButton: Button? = null
    private lateinit var phoneNumber:String
    private lateinit var email:String
    private lateinit var name:String
    private lateinit var password:String

    ///View Models and Fun Objects

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var errorString: String
    private lateinit var responsechecksignUp:ResponseSignUp
    private var errorShown = false
    private var code:Int=0



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
        profileViewModel.responsecheckSignUp().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                responsechecksignUp = it
                code = it.code
                if (code == 300) {
                    Toast.makeText(context, "You had already signed Up.Please Login to Continue ", LENGTH_SHORT).show()
                }
                else if(code==200)
                {
                    val bundle = Bundle()
                    bundle.putString(PHONE_NO,phoneNumber)
                    bundle.putString(E_MAIL,email)
                    bundle.putString(PASS_WORD,password)
                    bundle.putString(N_AME,name)

                    parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_initial, VerifyOtpFragment::class.java,bundle, "verify")
                            //.addToBackStack(null)
                            .commit()

                }
                else {
                    Toast.makeText(context,
                            "Error", LENGTH_SHORT).show()
                }
            profileViewModel.errorString().observe(viewLifecycleOwner, {
                it?.getContentIfNotHandled()?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            })
            }
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

         phoneNumber =editTextPhone!!.text!!.toString()
         email = editTextEmail!!.text!!.toString()
         password = editTextPassword!!.text!!.toString()
         name= editTextName!!.text!!.toString()


        val cheksignUp = BodySignUp(name, email, phoneNumber,password)
        profileViewModel.checksignup(cheksignUp)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("Error", errorShown)
        super.onSaveInstanceState(outState)
    }
}



