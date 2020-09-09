package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicantsFragment
import com.princeakash.projectified.user.BodyCreateProfile
import com.princeakash.projectified.user.ProfileRepository
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseCreateProfile
import kotlinx.android.synthetic.main.frag_create_profile.*
import kotlinx.android.synthetic.main.frag_create_profile.view.*
import kotlinx.android.synthetic.main.frag_create_profile.view.editTextCollege
import kotlinx.android.synthetic.main.frag_create_profile.view.editTextCourse
import kotlinx.android.synthetic.main.frag_create_profile.view.editTextInterest1
import kotlinx.android.synthetic.main.frag_create_profile.view.editTextInterest2
import kotlinx.android.synthetic.main.frag_create_profile.view.editTextSemester
import kotlinx.android.synthetic.main.frag_update_profile.view.*
import kotlinx.android.synthetic.main.signin_user.*
import org.w3c.dom.Text

class CreateProfileFragment :Fragment()
{

    private var editTextCollege: TextInputEditText? = null
    private var editTextCourse: AutoCompleteTextView? = null
    private var editTextSemester:AutoCompleteTextView? = null
    private var editTextInterest1 :AutoCompleteTextView?=null
    private var editTextInterest2 :AutoCompleteTextView?=null
    private var editTextInterest3 :AutoCompleteTextView?=null
    private var editChipC: Chip?=null
    private var editChipCpp: Chip?=null
    private var editChipJava :Chip?=null
    private var editChipPython:Chip?=null
    private var editChipKotlin :Chip?=null
    private var editChipJavaScript :Chip?=null
    private var editTextDescription1:TextInputEditText?=null

    private var ButtonSave:Button?=null


    private var  num:IntArray = intArrayOf(0, 0, 0, 0, 0,0)

    //  View Models and fun Instances

    var profileViewModel: ProfileViewModel?=null
    var profileRepository:ProfileRepository?=null
    var responseCreateProfile:ResponseCreateProfile?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_create_profile, container, false)

        editTextCollege = v.editTextCollege
        editTextCourse = v.editTextCourse
        editTextSemester = v.editTextSemester
        editTextInterest1 = v.editTextInterest1
        editTextInterest2 = v.editTextInterest2
        editTextInterest3 = v.editTextInterest1
        editTextDescription1=v.editTextDescription1
        editChipC = v.chipC1
        editChipCpp = v.chipCpp1
        editChipJava = v.chipJava1
        editChipKotlin = v.chipKotlin1
        editChipPython = v.chipPython1
        editChipJavaScript = v.chipJavaScript1

        ButtonSave = v.Save


        ButtonSave?.setOnClickListener {

            validateParameters();

        }

        return v
    }

        private fun validateParameters() {

            if (editTextCollege!!.text == null || editTextCollege!!.text!!.equals("")) {
                editTextCollege!!.error = "Provide your college Name."
                return
            }

            if (editTextCourse!!.text == null || editTextCourse!!.text!!.equals("")) {
                editTextCourse!!.error = "Enter Course."
                return
            }
            if (editTextSemester!!.text == null || editTextSemester!!.text!!.equals("")) {
                editTextSemester!!.error = "Enter Semester."
                return
            }
            if (editTextInterest1!!.text == null || editTextInterest1!!.text!!.equals("")) {
                editTextCourse!!.error = "Enter Course."
                return
            }
            if (editTextDescription1!!.text == null || editTextDescription1!!.text!!.equals("")) {
                editTextDescription1!!.error = "Enter Description."
                return
            }


            if (editChipC!!.isChecked()) {
                num?.set(0, 1)


            }
            if (editChipCpp!!.isChecked()) {
                num?.set(1, 1)
            }
            if (editChipJava!!.isChecked()) {
                num?.set(2, 1)
            }
            if (editChipKotlin!!.isChecked()) {
                num?.set(3, 1)
            }
            if (editChipPython!!.isChecked()) {
                num?.set(4, 1)

            }
            if (editChipJavaScript!!.isChecked()) {
                num?.set(5, 1)
            }
            val college = editTextCollege!!.text!!.toString()
            val course = editTextCourse!!.text!!.toString()
            val semester= editTextSemester!!.text!!.toString()
            val interest1 = editTextInterest1!!.text!!.toString()
            val interest2 = editTextInterest2!!.text!!.toString()
            val interest3 = editTextInterest3!!.text!!.toString()
            val description = editTextDescription1!!.text!!.toString()

            val token = profileRepository!!.getToken()
            val bodycreateProfile = BodyCreateProfile(college,course,semester,num,interest1,interest2,interest3,description)
            profileViewModel!!.createProfile(token ,bodycreateProfile)



        }

            }
