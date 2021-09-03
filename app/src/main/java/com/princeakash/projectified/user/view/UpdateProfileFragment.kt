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
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragUpdateProfileBinding
import com.princeakash.projectified.user.model.BodyUpdateProfile
import com.princeakash.projectified.user.viewmodel.ProfileViewModel

class UpdateProfileFragment : Fragment() {

    private var num: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)

    //  View Models and fun Instances
    lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragUpdateProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.frag_update_profile, container, false)


    private fun setAdapters() {
        val adapterCourse = ArrayAdapter.createFromResource(requireContext(), R.array.courses, android.R.layout.simple_spinner_dropdown_item)
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextCourse.setAdapter(adapterCourse)

        val adapterSemester = ArrayAdapter.createFromResource(requireContext(), R.array.semesters, android.R.layout.simple_spinner_dropdown_item)
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextSemester.setAdapter(adapterSemester)

        val adapterInterest = ArrayAdapter.createFromResource(requireContext(), R.array.interests, android.R.layout.simple_spinner_dropdown_item)
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editTextInterest1.setAdapter(adapterInterest)
        binding.editTextInterest2.setAdapter(adapterInterest)
        binding.editTextInterest3.setAdapter(adapterInterest)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragUpdateProfileBinding.bind(view)
        setAdapters()
        binding.Save.setOnClickListener { validateParameters() }

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        subscribeToObservers()

        if (savedInstanceState == null)
            loadLocalProfile()
    }

    private fun subscribeToObservers() {
        profileViewModel.responseUpdateProfile().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_SHORT).show()
            }
        })
        profileViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_SHORT).show()
            }
        })
    }

    private fun loadLocalProfile() {
        val profileModel = profileViewModel.getLocalProfile()!!
        binding.apply{
            editTextName.setText(profileModel.name)
            editTextCollege.setText(profileModel.collegeName)
            editTextCourse.setText(profileModel.course)
            editTextSemester.setText(profileModel.semester)
            editTextInterest1.setText(profileModel.interest1)
            editTextInterest2.setText(profileModel.interest2)
            editTextInterest3.setText(profileModel.interest3)
            editTextDescription.setText(profileModel.description)
            editTextHobbies.setText(profileModel.hobbies)
            val num = profileModel.languages
            Log.d(TAG, "loadLocalProfile: " + num[4])
            chipC.isChecked = num[0] == 1
            chipCpp.isChecked = num[1] == 1
            chipJava.isChecked = num[2] == 1
            chipKotlin.isChecked = num[3] == 1
            chipPython.isChecked = num[4] == 1
            chipJavaScript.isChecked = num[5] == 1
        }
    }

    private fun validateParameters() {
        binding.apply{
            if (editTextCollege.text.toString().isEmpty()){
                editTextCollege.error = "Provide your college Name."
                return
            }

            if (editTextCourse.text.toString().isEmpty()){
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextSemester.text.toString().isEmpty()){
                editTextSemester.error = "Enter Semester."
                return
            }
            if (editTextInterest1.text.toString().isEmpty()){
                editTextCourse.error = "Enter Course."
                return
            }
            if (editTextDescription.text.toString().isEmpty()){
                editTextDescription.error = "Enter Description."
                return
            }
            if (editTextHobbies.text.toString().isEmpty()){
                editTextHobbies.error = "Enter hobbies."
                return
            }
            if (chipC.isChecked) {
                num[0] = 1
            }
            if (chipCpp.isChecked) {
                num[1] = 1
            }
            if (chipJava.isChecked) {
                num[2] = 1
            }
            if (chipKotlin.isChecked) {
                num[3] = 1
            }
            if (chipPython.isChecked) {
                num[4] = 1
            }
            if (chipJavaScript.isChecked) {
                num[5] = 1
            }

            val userName = editTextName.text.toString()
            val college = editTextCollege.text.toString()
            val course = editTextCourse.text.toString()
            val semester = editTextSemester.text.toString()
            val interest1 = editTextInterest1.text.toString()
            val interest2 = editTextInterest2.text.toString()
            val interest3 = editTextInterest3.text.toString()
            val description = editTextDescription.text.toString()
            val hobbies = editTextHobbies.text.toString()

            progressCircularLayout.visibility = View.VISIBLE
            val bodyUpdateProfile = BodyUpdateProfile(userName, college, course, semester, num, interest1, interest2, interest3, description, hobbies, "0")
            profileViewModel.updateProfile(bodyUpdateProfile)
        }
    }

    companion object {
        private const val TAG = "UpdateProfileFragment"
    }
}
