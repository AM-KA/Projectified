package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.*
import com.princeakash.projectified.user.view.LoginFragment.Companion.USER_NAME
import kotlinx.android.synthetic.main.frag_create_profile.*
import kotlinx.android.synthetic.main.frag_update_profile.view.*

class UpdateProfileFragment :Fragment() {
    private var editTextName: TextInputEditText? = null
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
    private var editTextHobbies: AutoCompleteTextView? = null
    private var chipGroupLanguages: ChipGroup? = null
    private var ButtonSave:Button?=null

    private var  num:IntArray = intArrayOf(0, 0, 0, 0, 0,0)

    //  View Models and fun Instances
    var profileViewModel: ProfileViewModel?=null
    var responseUpdateProfile:ResponseUpdateProfile?=null

    private var enabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel!!.responseUpdateProfile.observe(viewLifecycleOwner, {
            responseUpdateProfile = it
            Toast.makeText(context, it.message, LENGTH_SHORT).show()
        })
        if(savedInstanceState==null)
        {
            enabled = false
            setEditable()
        }else{
            enabled = savedInstanceState.getBoolean(ENABLED_STATUS)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_update_profile, container, false)

        editTextName = v.editTextName
        editTextCollege = v.editTextCollege
        editTextCourse = v.editTextCourse
        editTextSemester = v.editTextSemester
        editTextInterest1 = v.editTextInterest1
        editTextInterest2 = v.editTextInterest2
        editTextInterest3 = v.editTextInterest3
        editTextDescription1=v.editTextDescription
        editTextHobbies = v.editTextHobbies
        chipGroupLanguages = v.chipGroupLanguages
        editChipC = v.chipC2
        editChipCpp = v.chipCpp2
        editChipJava = v.chipJava2
        editChipKotlin = v.chipKotlin2
        editChipPython = v.chipPython2
        editChipJavaScript = v.chipJavaScript2

        ButtonSave = v.Save
        ButtonSave?.setOnClickListener {
            if(enabled)
                validateParameters()
            else{
                enabled = true
                setEditable()
            }
        }
        if(savedInstanceState==null)
            loadLocalProfile()
        return v
    }

    private fun loadLocalProfile() {
        val profileModel = profileViewModel!!.getLocalProfile()!!
        editTextName!!.setText(profileModel.name)
        editTextCollege!!.setText(profileModel.collegeName)
        editTextCourse!!.setText(profileModel.course)
        editTextSemester!!.setText(profileModel.semester)
        editTextInterest1!!.setText(profileModel.interest1)
        editTextInterest2!!.setText(profileModel.interest2)
        editTextInterest3!!.setText(profileModel.interest3)
        editTextDescription1!!.setText(profileModel.description)
        editTextHobbies!!.setText(profileModel.hobbies)
        val num = profileModel.languages

        if(num.get(0)==1){
            editChipC!!.isChecked = true
        }
        if(num.get(1)==1){
            editChipCpp!!.isChecked = true
        }
        if (num.get(2)==1){
            editChipJava!!.isChecked = true
        }
        if (num.get(3)==1){
            editChipKotlin!!.isChecked = true
        }
        if (num.get(4)==1){
            editChipPython!!.isChecked = true
        }
        if (num.get(5)==1){
            editChipJavaScript!!.isChecked = true
        }
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
        if (editTextHobbies!!.text == null || editTextHobbies!!.text!!.equals("")) {
            editTextHobbies!!.error = "Enter hobbies."
            return
        }
        if (editChipC!!.isChecked()) {
            num.set(0, 1)
        }
        if (editChipCpp!!.isChecked()) {
            num.set(1, 1)
        }
        if (editChipJava!!.isChecked()) {
            num.set(2, 1)
        }
        if (editChipKotlin!!.isChecked()) {
            num.set(3, 1)
        }
        if (editChipPython!!.isChecked()) {
            num.set(4, 1)
         }
        if (editChipJavaScript!!.isChecked()) {
            num.set(5, 1)
        }

        val userName = editTextName!!.text!!.toString()
        val college = editTextCollege!!.text!!.toString()
        val course = editTextCourse!!.text!!.toString()
        val semester= editTextSemester!!.text!!.toString()
        val interest1 = editTextInterest1!!.text!!.toString()
        val interest2 = editTextInterest2!!.text!!.toString()
        val interest3 = editTextInterest3!!.text!!.toString()
        val description = editTextDescription1!!.text!!.toString()
        val hobbies = editTextHobbies!!.text!!.toString()

        val bodyUpdateProfile = BodyUpdateProfile(userName, college, course, semester, num, interest1, interest2, interest3, description, hobbies)
        profileViewModel!!.updateProfile(bodyUpdateProfile)
    }

    fun setEditable(){
        editTextName!!.isEnabled = enabled
        editTextCollege!!.isEnabled = enabled
        editTextCourse!!.isEnabled = enabled
        editTextSemester!!.isEnabled = enabled
        editTextInterest1!!.isEnabled = enabled
        editTextInterest2!!.isEnabled = enabled
        editTextInterest3!!.isEnabled = enabled
        chipGroupLanguages!!.isEnabled = enabled
        editTextHobbies!!.isEnabled = enabled
        editTextDescription1!!.isEnabled = enabled
        if(enabled)
            ButtonSave!!.text = "Save"
        else
            ButtonSave!!.text = "Edit"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ENABLED_STATUS, enabled)
    }
    companion object{
        val ENABLED_STATUS = "EnabledStatus"
    }
}
