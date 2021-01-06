package com.princeakash.projectified.candidate.myApplications.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.*
import java.net.URL

class MyApplicationDetailsFragment : Fragment() {

    //Views
    private lateinit  var textViewRequirements: TextView
    private lateinit  var textViewSkills: TextView
    private lateinit  var textViewExpectations: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewCollege: TextView
    private lateinit var textViewSemester: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var editTextPreviousWork: TextInputEditText
    private lateinit var editTextResume: TextInputEditText
    private lateinit var imageViewSeen: ImageView
    private lateinit var imageViewSelected: ImageView
    private lateinit var buttonDeleteApplication: Button
    private lateinit var buttonUpdateDetails: Button
    private lateinit var progressCircularLayout: RelativeLayout

    //ViewModels and Observable Objects
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_myapplicationdetail, container, false)
        textViewRequirements = v.textViewRequirementData
        textViewSkills = v.textViewSkillsData
        textViewExpectations = v.textViewExpectationData
        textViewName = v.textViewNameData
        textViewCollege = v.textViewCollegeData
        textViewSemester = v.textViewSemesterData
        textViewCourse = v.textViewCourseData
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

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        recruiterCandidateViewModel.responseGetApplicationDetailByIdCandidate().observe(viewLifecycleOwner, {
            progressCircularLayout.visibility = View.INVISIBLE
            populateViews(it)
        })


        recruiterCandidateViewModel.responseUpdateApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })


        recruiterCandidateViewModel.responseDeleteApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        recruiterCandidateViewModel.safeToVisitApplicationList().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {safeToVisit->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.details_to_my_applications_home)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
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

        if(savedInstanceState==null){
            recruiterCandidateViewModel.nullifySafeToVisitApplicationDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Application Details"
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

        try{
            val uri = URL(resume).toURI()
        }catch (e: Exception){
            editTextResume.error = "Enter a valid URL."
            return
        }

        progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel!!.updateApplication(resume, previousWork)
    }

    private fun populateViews(it:ResponseGetApplicationDetailByIdCandidate) {
        if (it.code == 200) {
            textViewRequirements?.setText(it.application?.requirements)
            textViewSkills?.setText(it?.application?.skills)
            textViewExpectations?.setText(it?.application?.expectation)
            textViewName?.setText(it?.application?.recruiter_name)
            textViewCollege?.setText(it?.application?.recruiter_collegeName)
            textViewSemester?.setText(it?.application?.recruiter_semester)
            textViewCourse?.setText(it?.application?.recruiter_course)
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
                    recruiterCandidateViewModel.deleteApplication()
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }
}