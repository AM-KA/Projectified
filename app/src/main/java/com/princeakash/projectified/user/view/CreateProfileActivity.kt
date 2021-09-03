package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragCreateProfileBinding
import com.princeakash.projectified.user.model.BodyUpdateProfile
import com.princeakash.projectified.user.model.ResponseUpdateProfile
import com.princeakash.projectified.user.viewmodel.ProfileViewModel

class CreateProfileActivity : AppCompatActivity() {

    private var num: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)

    //  ViewModels
    lateinit var profileViewModel: ProfileViewModel
    lateinit var responseUpdateProfile: ResponseUpdateProfile

    lateinit var binding: FragCreateProfileBinding

    private var userName: String = ""
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.responseUpdateProfile().observe(this, {
            it?.getContentIfNotHandled()?.let {
                responseUpdateProfile = it
                Toast.makeText(this, it.message, LENGTH_LONG).show()
                //TODO:Apply code=200 validation
                //Save Profile Status locally
                profileViewModel.setProfileStatus(true)
                //Navigate to Main Activity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        val adapterCourse = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item)
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextCourse.setAdapter(adapterCourse)

        val adapterSemester = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_dropdown_item)
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextSemester.setAdapter(adapterSemester)

        val adapterInterest = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_dropdown_item)
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextInterest1.setAdapter(adapterInterest)
        binding.editTextInterest2.setAdapter(adapterInterest)
        binding.editTextInterest3.setAdapter(adapterInterest)

        prepareName()

        binding.Save.setOnClickListener {
            validateParameters()
        }
    }

    private fun prepareName() {
        userName = profileViewModel.getUserName()
        binding.editTextName.setText(userName)
    }

    private fun validateParameters() {
        binding.apply{
            if (editTextName.text.toString().isEmpty()) {
                editTextName.error = "Provide your college Name."
                return
            }

            if (editTextCollege.text.toString().isEmpty()) {
                editTextCollege.error = "Provide your college Name."
                return
            }

            if (editTextCourse.text.toString().isEmpty()) {
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextSemester.text.toString().isEmpty()) {
                editTextSemester.error = "Enter Semester."
                return
            }
            if (editTextInterest1.text.toString().isEmpty()) {
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextDescription1.text.toString().isEmpty()) {
                editTextDescription1.error = "Enter Description."
                return
            }
            if (editTextHobbies.text.toString().isEmpty()) {
                editTextHobbies.error = "Enter hobbies."
                return
            }
            if (chipC1.isChecked)
                num.set(0, 1)

            if (chipCpp1.isChecked)
                num.set(1, 1)

            if (chipJava1.isChecked)
                num.set(2, 1)

            if (chipKotlin1.isChecked)
                num.set(3, 1)

            if (chipPython1.isChecked)
                num.set(4, 1)

            if (chipJavaScript1.isChecked)
                num.set(5, 1)


            userName = editTextName.text!!.toString()
            val college = editTextCollege.text!!.toString()
            val course = editTextCourse.text!!.toString()
            val semester = editTextSemester.text!!.toString()
            val interest1 = editTextInterest1.text!!.toString()
            val interest2 = editTextInterest2.text!!.toString()
            val interest3 = editTextInterest3.text!!.toString()
            val description = editTextDescription1.text!!.toString()
            val hobbies = editTextHobbies.text!!.toString()

            val bodyUpdateProfile = BodyUpdateProfile(userName, college, course, semester, num, interest1, interest2, interest3, description, hobbies, userId)
            profileViewModel.updateProfile(bodyUpdateProfile)
        }
    }
}
