package com.princeakash.projectified.user.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.user.BodyUpdateProfile
import com.princeakash.projectified.user.ProfileViewModel
import com.princeakash.projectified.user.ResponseUpdateProfile
import kotlinx.android.synthetic.main.frag_update_profile.view.*

class UpdateProfileFragment : Fragment() {

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
    private lateinit var chipGroupLanguages: ChipGroup
    private lateinit var ButtonSave: Button
    private lateinit var progressCircularLayout: RelativeLayout

    private var num: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)

    //  View Models and fun Instances
    lateinit var profileViewModel: ProfileViewModel
    lateinit var responseUpdateProfile: ResponseUpdateProfile

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_update_profile, container, false)

        editTextName = v.editTextName
        editTextCollege = v.editTextCollege
        editTextCourse = v.editTextCourse
        editTextSemester = v.editTextSemester
        editTextInterest1 = v.editTextInterest1
        editTextInterest2 = v.editTextInterest2
        editTextInterest3 = v.editTextInterest3
        editTextDescription1 = v.editTextDescription
        editTextHobbies = v.editTextHobbies
        chipGroupLanguages = v.chipGroupLanguages
        editChipC = v.chipC2
        editChipCpp = v.chipCpp2
        editChipJava = v.chipJava2
        editChipKotlin = v.chipKotlin2
        editChipPython = v.chipPython2
        editChipJavaScript = v.chipJavaScript2
        progressCircularLayout = v.progress_circular_layout

        val adapterCourse = ArrayAdapter.createFromResource(requireContext(), R.array.courses, android.R.layout.simple_spinner_dropdown_item)
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextCourse.setAdapter(adapterCourse)

        val adapterSemester = ArrayAdapter.createFromResource(requireContext(), R.array.semesters, android.R.layout.simple_spinner_dropdown_item)
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextSemester.setAdapter(adapterSemester)

        val adapterInterest = ArrayAdapter.createFromResource(requireContext(), R.array.interests, android.R.layout.simple_spinner_dropdown_item)
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editTextInterest1.setAdapter(adapterInterest)
        editTextInterest2.setAdapter(adapterInterest)
        editTextInterest3.setAdapter(adapterInterest)

        ButtonSave = v.Save
        ButtonSave.setOnClickListener {
            validateParameters()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        profileViewModel.responseUpdateProfile().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_SHORT).show()
            }
        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_SHORT).show()
            }
        })
        if (savedInstanceState == null)
            loadLocalProfile()
    }

    private fun loadLocalProfile() {
        val profileModel = profileViewModel.getLocalProfile()!!
        editTextName.setText(profileModel.name)
        editTextCollege.setText(profileModel.collegeName)
        editTextCourse.setText(profileModel.course)
        editTextSemester.setText(profileModel.semester)
        editTextInterest1.setText(profileModel.interest1)
        editTextInterest2.setText(profileModel.interest2)
        editTextInterest3.setText(profileModel.interest3)
        editTextDescription1.setText(profileModel.description)
        editTextHobbies.setText(profileModel.hobbies)
        val num = profileModel.languages
        Log.d(TAG, "loadLocalProfile: " + num[4])
        editChipC.isChecked = num.get(0) == 1
        editChipCpp.isChecked = num.get(1) == 1
        editChipJava.isChecked = num.get(2) == 1
        editChipKotlin.isChecked = num.get(3) == 1
        editChipPython.isChecked = num.get(4) == 1
        editChipJavaScript.isChecked = num.get(5) == 1
    }

    private fun validateParameters() {

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

        val userName = editTextName.text!!.toString()
        val college = editTextCollege.text!!.toString()
        val course = editTextCourse.text!!.toString()
        val semester = editTextSemester.text!!.toString()
        val interest1 = editTextInterest1.text!!.toString()
        val interest2 = editTextInterest2.text!!.toString()
        val interest3 = editTextInterest3.text!!.toString()
        val description = editTextDescription1.text!!.toString()
        val hobbies = editTextHobbies.text!!.toString()

        progressCircularLayout.visibility = View.VISIBLE
        val bodyUpdateProfile = BodyUpdateProfile(userName, college, course, semester, num, interest1, interest2, interest3, description, hobbies, "0")
        profileViewModel.updateProfile(bodyUpdateProfile)
    }

    companion object {
        val ENABLED_STATUS = "EnabledStatus"
        private const val TAG = "UpdateProfileFragment"
    }
}
