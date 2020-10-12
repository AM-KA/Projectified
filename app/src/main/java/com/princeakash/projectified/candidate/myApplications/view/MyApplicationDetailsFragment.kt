package com.princeakash.projectified.candidate.myApplications.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateExistingApplicationViewModel
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.*
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.editTextExpectation
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.editTextRequirement
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.editTextSkills

class MyApplicationDetailsFragment : Fragment() {

    //Views
    private lateinit  var editTextRequirements: TextInputEditText
    private lateinit  var editTextSkills: TextInputEditText
    private lateinit  var editTextExpectations: TextInputEditText
    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextCollege: TextInputEditText
    private lateinit var editTextSemester: TextInputEditText
    private lateinit var editTextCourse: TextInputEditText
    private lateinit var editTextPreviousWork: TextInputEditText
    private lateinit var editTextResume: TextInputEditText
    private lateinit var imageViewSeen: ImageView
    private lateinit var imageViewSelected: ImageView
    private lateinit var buttonDeleteApplication: Button
    private lateinit var buttonUpdateDetails: Button
    private lateinit var progressCircularLayout: RelativeLayout



    //ViewModels and Observable Objects
    private lateinit var candidateExistingApplicationViewModel: CandidateExistingApplicationViewModel


    //Application Data
    private var applicationId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            //First LoadUp of Fragment
            applicationId = requireArguments().getString(APPLICATION_IDC)
        } else {
            //Restore state
            //responseGetApplicationDetailsByIdCandidate = savedInstanceState.getSerializable(RESPONSE_GET_APPLICATIONS_DETAILS) as ResponseGetApplicationDetailsByIdCandidate
            applicationId = savedInstanceState.getString(APPLICATION_IDC)

            }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_myapplicationdetail, container, false)
        editTextRequirements = v.editTextRequirement
        editTextSkills = v.editTextSkills
        editTextExpectations = v.editTextExpectation
        editTextName = v.editTextName
        editTextCollege = v.editTextCollege
        editTextSemester = v.editTextSemester
        editTextCourse = v.editTextCourse
        editTextPreviousWork = v.editTextPreviousWork
        editTextResume = v.editTextResume
        imageViewSeen = v.imageViewSeen
        imageViewSelected = v.imageViewSelected
        buttonDeleteApplication = v.buttonDeleteApplication
        buttonUpdateDetails = v.buttonUpdateDetails
        progressCircularLayout = v.progress_circular_layout


        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        candidateExistingApplicationViewModel = ViewModelProvider(requireParentFragment()).get(CandidateExistingApplicationViewModel::class.java)
        candidateExistingApplicationViewModel!!.responseGetApplicationDetailByIdCandidate().observe(viewLifecycleOwner, {
            progressCircularLayout.visibility = View.INVISIBLE
            populateViews(it)

        })


        candidateExistingApplicationViewModel!!.responseUpdateApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                fetchApplicationDetails()
            }
        })


        candidateExistingApplicationViewModel!!.responseDeleteApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                //TODO: Show Toast
                parentFragmentManager.popBackStackImmediate()
            }
        })


        candidateExistingApplicationViewModel!!.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        buttonUpdateDetails?.setOnClickListener {
            updateOffer()


        }
        buttonDeleteApplication?.setOnClickListener {
            deleteApplication()
        }


        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Application Details"
    }




    private fun updateOffer() {
        if (editTextPreviousWork!!.text == null || editTextPreviousWork!!.text!!.equals("")) {
            editTextPreviousWork!!.error = "Please Provide Drive Link of Your Previous Work."
            return
        }
        if (editTextResume!!.text == null || editTextResume!!.text!!.equals("")) {
            editTextResume!!.error = "Please Provide Drive Link of the Resume."
            return
        }

        val previousWork = editTextPreviousWork!!.text!!.toString()
        val resume = editTextResume!!.text!!.toString()

        progressCircularLayout.visibility = View.VISIBLE
        val bodyUpdateApplication = BodyUpdateApplication(resume, previousWork)
        candidateExistingApplicationViewModel!!.updateApplication(applicationId!!, bodyUpdateApplication)
    }


    private fun fetchApplicationDetails() {
        //TODO:Start ProgressBar
        progressCircularLayout.visibility = View.VISIBLE
        applicationId?.let { candidateExistingApplicationViewModel!!.getApplicationDetailByIdCandidate(it) }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(APPLICATION_IDC, applicationId)
    }



    private fun populateViews(it:ResponseGetApplicationDetailByIdCandidate) {
        if (it.code == 200) {
            editTextRequirements?.setText(it.application?.requirements)
            editTextSkills?.setText(it?.application?.skills)
            editTextExpectations?.setText(it?.application?.expectation)
            editTextName?.setText(it?.application?.recruiter_name)
            editTextCollege?.setText(it?.application?.recruiter_collegeName)
            editTextSemester?.setText(it?.application?.recruiter_semester)
            editTextCourse?.setText(it?.application?.recruiter_course)
            editTextPreviousWork?.setText(it?.application?.previousWork)
            editTextResume?.setText(it?.application?.resume)

            if (it?.application?.is_Seen!!)
                imageViewSeen?.setImageResource(R.drawable.ic_baseline_favorite_24)
            else
                imageViewSeen?.setImageResource((R.drawable.ic_outline_favorite_border_24))

            if (it?.application?.is_Selected!!)
                imageViewSelected?.setImageResource(R.drawable.ic_baseline_done_24)
            else
                imageViewSelected?.setImageResource((R.drawable.ic_baseline_done_outline_24))
            progressCircularLayout.visibility = View.INVISIBLE
        } else {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
        progressCircularLayout.visibility = View.INVISIBLE
    }


    private fun deleteApplication() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this application? You cannot undo this action later.")
                .setPositiveButton("Yes") { dialog, which ->
                    progressCircularLayout.visibility = View.VISIBLE
                    candidateExistingApplicationViewModel!!.deleteApplication(applicationId!!)
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }



    companion object {
        const val APPLICATION_IDC = "applicationId"
    }
}