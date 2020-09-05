package com.princeakash.projectified.candidate.myApplications.view

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateExistingApplicationViewModel
import com.princeakash.projectified.recruiter.myOffers.model.BodyToggleVisibility
import com.princeakash.projectified.recruiter.myOffers.model.BodyUpdateOffer
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicantsFragment
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicationFragment.Companion.APPLICATION_ID
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferDetailsFragment
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferDetailsFragment.Companion.EDITABLE_STATUS
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_myapplicationdetail.*
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.*
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.editTextExpectation
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.editTextRequirement
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.editTextSkills

class MyApplicationDetailsFragment : Fragment(){


    //Views
    private var editTextRequirements: TextInputEditText? = null
    private var editTextSkills: TextInputEditText? = null
    private var editTextExpectations: TextInputEditText? = null
    private var editTextName:TextInputEditText? =null
    private var editTextCollege:TextInputEditText?=null
    private var editTextSemester:TextInputEditText?=null
    private var editTextCourse:TextInputEditText?=null
    private var editTextPreviousWork:TextInputEditText?=null
    private var editTextResume:TextInputEditText?=null
    private var imageViewSeen: ImageView?=null
    private var imageViewSelected:ImageView?=null
    private var buttonDeleteApplication:Button?=null
    private var buttonUpdateDetails: Button? = null



    //Determines whether textviews are editable or not
    private var editable = false

    //ViewModels and Observable Objects
    private var candidateExistingApplicationViewModel: CandidateExistingApplicationViewModel? = null
    private var responseGetApplicationDetailsByIdCandidate:ResponseGetApplicationDetailByIdCandidate? = null
    private var responseUpdateApplication: ResponseUpdateApplication?= null
    private var responseDeleteApplication: ResponseDeleteApplication?=null

    private var error:String? = null

    //Application Data
    private var applicationId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        candidateExistingApplicationViewModel= ViewModelProvider(requireParentFragment()).get(CandidateExistingApplicationViewModel::class.java)

        candidateExistingApplicationViewModel!!.responseGetApplicationDetailByIdCandidate.observe(viewLifecycleOwner, {
            responseGetApplicationDetailsByIdCandidate = it
            populateViews()
            editable = false
            setEditable()
        })

        candidateExistingApplicationViewModel!!.responseUpdateApplication.observe(viewLifecycleOwner, {
            responseUpdateApplication = it
            //TODO: Show Toast and start ProgressBar
            fetchApplicationDetails()
        })

        candidateExistingApplicationViewModel!!.responseDeleteApplication.observe(viewLifecycleOwner, {
            responseDeleteApplication= it
            //TODO: Show Toast
            parentFragmentManager.popBackStackImmediate()
        })

        candidateExistingApplicationViewModel!!.errorString.observe(viewLifecycleOwner, {
            error = it
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v =inflater.inflate(R.layout.frag_myapplicationdetail, container,false)

       editTextRequirements=v.editTextRequirement
       editTextSkills     = v.editTextSkills
        editTextExpectations=v.editTextExpectation
        editTextName=v.editTextName
        editTextCollege=v.editTextCollege
        editTextSemester=v.editTextSemester
        editTextCourse=v.editTextCourse
        editTextPreviousWork=v.editTextPreviousWork
        editTextResume=v.editTextResume
        imageViewSeen=v.imageViewSeen
        imageViewSelected=v.imageViewSelected
        buttonDeleteApplication=v.buttonDeleteApplication
        buttonUpdateDetails=v.buttonUpdateDetails

        if(savedInstanceState == null) {
            //First loadup of Fragment
            applicationId = arguments?.getString(APPLICATION_IDC)
            fetchApplicationDetails()
        }
        else{
            //Restore state
            //responseGetApplicationDetailsByIdCandidate = savedInstanceState.getSerializable(RESPONSE_GET_APPLICATIONS_DETAILS) as ResponseGetApplicationDetailsByIdCandidate
            applicationId = savedInstanceState.getString(APPLICATION_IDC)
            editable = savedInstanceState.getBoolean(EDITABLE_STATUS)
            responseGetApplicationDetailsByIdCandidate?.let {
                populateViews()
            }
            setEditable()
        }



        buttonUpdateDetails?.setOnClickListener{
            editable = !editable
            setEditable()
            if(editable)
                updateOffer()
        }

        buttonDeleteApplication?.setOnClickListener{
            deleteApplication()
        }

        return v
    }

    private fun updateOffer() {
        if(editTextPreviousWork!!.text == null || editTextPreviousWork!!.text!!.equals("")) {
            editTextPreviousWork!!.error = "Please Provide Drive Link of Your Previous Work."
            return
        }
        if(editTextResume!!.text == null || editTextResume!!.text!!.equals("")) {
            editTextResume!!.error = "Please Provide Drive Link of the Resume."
            return
        }

        val previousWork =editTextPreviousWork!!.text!!.toString()
        val resume = editTextResume!!.text!!.toString()

        val bodyUpdateApplication = BodyUpdateApplication(previousWork,resume)
        candidateExistingApplicationViewModel!!.updateApplication(applicationId!!, bodyUpdateApplication)
    }

    private fun fetchApplicationDetails() {
        //TODO:Start ProgressBar
        applicationId?.let { candidateExistingApplicationViewModel!!.getApplicationDetailByIdCandidate(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(APPLICATION_IDC, responseGetApplicationDetailsByIdCandidate?.application?.application_id)
        outState.putBoolean(EDITABLE_STATUS, editable)
    }

    fun populateViews(){
        editTextRequirements?.setText(responseGetApplicationDetailsByIdCandidate?.application?.requirements)
        editTextSkills?.setText(responseGetApplicationDetailsByIdCandidate?.application?.skills)
        editTextExpectations?.setText(responseGetApplicationDetailsByIdCandidate?.application?.expectation)
        editTextName?.setText(responseGetApplicationDetailsByIdCandidate?.application?.recruiter_name)
        editTextCollege?.setText(responseGetApplicationDetailsByIdCandidate?.application?.recruiter_collegeName)
        editTextSemester?.setText(responseGetApplicationDetailsByIdCandidate?.application?.recruiter_semester)
        editTextCourse?.setText(responseGetApplicationDetailsByIdCandidate?.application?.recruiter_course)
        editTextPreviousWork?.setText(responseGetApplicationDetailsByIdCandidate?.application?.previousWork)
        editTextResume?.setText(responseGetApplicationDetailsByIdCandidate?.application?.resume)
        if(responseGetApplicationDetailsByIdCandidate?.application?.is_seen!!)
                {
                    imageViewSeen?.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
        else
            imageViewSeen?.setImageResource((R.drawable.ic_outline_favorite_border_24))

        if(responseGetApplicationDetailsByIdCandidate?.application?.is_selected!!)
        {
            imageViewSelected?.setImageResource(R.drawable.ic_baseline_done_24)
        }
        else
            imageViewSelected?.setImageResource((R.drawable.ic_baseline_done_outline_24))

    }

    fun setEditable(){
        editTextExpectations?.isEnabled = editable
        editTextSkills?.isEnabled = editable
        editTextRequirements?.isEnabled = editable
        if(editable){
            buttonUpdateDetails?.text = "Save Details"
        }else{
            buttonUpdateDetails?.text = "Edit Details"
        }
    }

    fun deleteApplication(){
        //TODO: Add DialogBox
        candidateExistingApplicationViewModel!!.deleteApplication(applicationId!!)
    }

    companion object {
        val APPLICATION_IDC = "applicationId"
        val EDITABLE_STATUS = "EditableStatus"
    }
}