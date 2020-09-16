package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_candidate_details_recruiter.view.*

class MyOfferApplicationFragment : Fragment() {

    //ViewModel and Necessary LiveData Observer objects
    private var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel? = null
    private var responseGetApplicationByIdRecruiter: ResponseGetApplicationByIdRecruiter? = null
    private var error: String? = null
    private var responseMarkAsSeen: ResponseMarkAsSeen? = null
    private var responseMarkAsSelected: ResponseMarkAsSelected? = null

    //Application related data
    private var applicationID: String? = null

    //Views
    private var textViewName: TextView? = null
    private var textViewCollege: TextView? = null
    private var textViewCourse: TextView? = null
    private var textViewSemester: TextView? = null
    private var textViewPhone: TextView? = null
    private var textViewPreviousWork: TextView? = null
    private var textViewResume: TextView? = null
    private var imageViewSeen: ImageView? = null
    private var imageViewSelected: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)

        recruiterExistingOffersViewModel!!.responseGetApplicationById.observe(viewLifecycleOwner, {
            responseGetApplicationByIdRecruiter = it
            populateViews()
        })

        recruiterExistingOffersViewModel!!.errorString.observe(viewLifecycleOwner, {
            error = it
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })

        recruiterExistingOffersViewModel!!.responseMarkAsSeen.observe(viewLifecycleOwner, {
            responseMarkAsSeen = it

            //Take action as per response
        })

        recruiterExistingOffersViewModel!!.responseMarkAsSelected.observe(viewLifecycleOwner, {
            responseMarkAsSelected = it

            //Take action as per response
        })

        if (savedInstanceState == null)
            applicationID = arguments?.getString(APPLICATION_ID)
        else
            applicationID = savedInstanceState.getString(APPLICATION_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_candidate_details_recruiter, container, false)
        textViewName = v.textViewCollege
        textViewCollege = v.textViewCollege
        textViewCourse = v.textViewCourse
        textViewSemester = v.textViewSemester
        textViewPhone = v.textViewPhone
        textViewPreviousWork = v.textViewPreviousWork
        textViewResume = v.textViewResume
        imageViewSeen = v.imageViewSeen
        imageViewSelected = v.imageViewSelected

        imageViewSeen?.setOnClickListener {
            markSeen()
        }

        imageViewSelected?.setOnClickListener {
            markSelected()
        }

        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(APPLICATION_ID, applicationID)
    }

    private fun populateViews() {
        responseGetApplicationByIdRecruiter?.let {
            textViewName?.text = it.applicant_name
            textViewCollege?.text = it.applicant_collegeName
            textViewCourse?.text = it.applicant_course
            textViewSemester?.text = it.applicant_semester
            textViewPhone?.text = it.applicant_phone
            textViewPreviousWork?.text = it.previousWork
            textViewResume?.text = it.resume
        }
    }

    private fun markSeen() {
        responseGetApplicationByIdRecruiter?.let {
            val status = it.markAsSeen
            recruiterExistingOffersViewModel?.markSeen(applicationID!!, BodyMarkAsSeen(!status))
        }
    }

    private fun markSelected() {
        responseGetApplicationByIdRecruiter?.let {
            val status = it.markAsSelected
            recruiterExistingOffersViewModel?.markSelected(applicationID!!, BodyMarkAsSelected(!status))
        }
    }

    companion object {
        val APPLICATION_ID = "ApplicationId"
    }
}

