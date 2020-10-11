package com.princeakash.projectified.user.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.MainActivity
import com.princeakash.projectified.R
import com.princeakash.projectified.user.BodyCreateProfile
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseCreateProfile

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextCollege: TextInputEditText
    private lateinit var editTextCourse: AutoCompleteTextView
    private lateinit var editTextSemester: AutoCompleteTextView
    private lateinit var editTextInterest1: AutoCompleteTextView
    private lateinit var editTextInterest2: AutoCompleteTextView
    private lateinit var editTextInterest3: AutoCompleteTextView
    private lateinit var editChipC: Chip
    private lateinit var editChipCpp: Chip
    private lateinit var editChipJava: Chip
    private lateinit var editChipPython: Chip
    private lateinit var editChipKotlin: Chip
    private lateinit var editChipJavaScript: Chip
    private lateinit var editTextDescription1: TextInputEditText
    private lateinit var editTextHobbies: AutoCompleteTextView
    private lateinit var ButtonSave: Button

    private var num: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)

    //  View Models and fun Instances
    lateinit var profileViewModel: ProfileViewModel
    lateinit var responseCreateProfile: ResponseCreateProfile

    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frag_create_profile)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.responseCreateProfile().observe(this, {
            it?.getContentIfNotHandled()?.let {
                responseCreateProfile = it
                Toast.makeText(this, it.message, LENGTH_SHORT).show()
                //Navigate to Main Activity
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        editTextName = findViewById(R.id.editTextName)
        editTextCollege = findViewById(R.id.editTextCollege)
        editTextCourse = findViewById(R.id.editTextCourse)
        editTextSemester = findViewById(R.id.editTextSemester)
        editTextInterest1 = findViewById(R.id.editTextInterest1)
        editTextInterest2 = findViewById(R.id.editTextInterest2)
        editTextInterest3 = findViewById(R.id.editTextInterest3)
        editTextDescription1 = findViewById(R.id.editTextDescription1)
        editTextHobbies = findViewById(R.id.editTextHobbies)
        editChipC = findViewById(R.id.chipC1)
        editChipCpp = findViewById(R.id.chipCpp1)
        editChipJava = findViewById(R.id.chipJava1)
        editChipKotlin = findViewById(R.id.chipKotlin1)
        editChipPython = findViewById(R.id.chipPython1)
        editChipJavaScript = findViewById(R.id.chipJavaScript1)

        val adapterCourse = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item)
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextCourse.setAdapter(adapterCourse)

        val adapterSemester = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_dropdown_item)
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextSemester.setAdapter(adapterSemester)

        val adapterInterest = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_dropdown_item)
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextInterest1.setAdapter(adapterInterest)
        editTextInterest2.setAdapter(adapterInterest)
        editTextInterest3.setAdapter(adapterInterest)


        ButtonSave = findViewById(R.id.Save)
        ButtonSave.setOnClickListener {
            validateParameters()
        }
    }

    private fun validateParameters() {
        if (editTextName.text == null || editTextName.text!!.equals("")) {
            editTextName.error = "Provide your college Name."
            return
        }

        if (editTextCollege.text == null || editTextCollege.text!!.equals("")) {
            editTextCollege.error = "Provide your college Name."
            return
        }

        if (editTextCourse.text == null || editTextCourse.text!!.equals("")) {
            editTextCourse.error = "Enter Course."
            return
        }
        if (editTextSemester.text == null || editTextSemester.text!!.equals("")) {
            editTextSemester.error = "Enter Semester."
            return
        }
        if (editTextInterest1.text == null || editTextInterest1.text!!.equals("")) {
            editTextCourse.error = "Enter Course."
            return
        }
        if (editTextDescription1.text == null || editTextDescription1.text!!.equals("")) {
            editTextDescription1.error = "Enter Description."
            return
        }
        if (editTextHobbies.text == null || editTextHobbies.text!!.equals("")) {
            editTextHobbies.error = "Enter hobbies."
            return
        }
        if (editChipC.isChecked) {
            num.set(0, 1)
        }
        if (editChipCpp.isChecked) {
            num.set(1, 1)
        }
        if (editChipJava.isChecked) {
            num.set(2, 1)
        }
        if (editChipKotlin.isChecked) {
            num.set(3, 1)
        }
        if (editChipPython.isChecked) {
            num.set(4, 1)
        }
        if (editChipJavaScript.isChecked) {
            num.set(5, 1)
        }

        userName = editTextName.text!!.toString()
        val college = editTextCollege.text!!.toString()
        val course = editTextCourse.text!!.toString()
        val semester = editTextSemester.text!!.toString()
        val interest1 = editTextInterest1.text!!.toString()
        val interest2 = editTextInterest2.text!!.toString()
        val interest3 = editTextInterest3.text!!.toString()
        val description = editTextDescription1.text!!.toString()
        val hobbies = editTextHobbies.text!!.toString()

        val bodycreateProfile = BodyCreateProfile(userName, college, course, semester, num, interest1, interest2, interest3, description, hobbies, "0")
        profileViewModel.createProfile(bodycreateProfile)
    }
}
