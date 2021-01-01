package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.ResponseGetApplicationByIdRecruiter
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterViewModel
import kotlinx.android.synthetic.main.frag_candidate_details_recruiter.view.*

class MyOfferApplicationFragment : Fragment() {

    //ViewModel and Necessary LiveData Observer objects
    private lateinit var recruiterViewModel: RecruiterViewModel
    private var responseGetApplicationByIdRecruiter: ResponseGetApplicationByIdRecruiter? = null
    private lateinit var progressCircularLayout: RelativeLayout

    //Views
    private lateinit var textViewName: TextView
    private lateinit var textViewCollege: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var textViewSemester: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewPreviousWork: TextView
    private lateinit var textViewResume: TextView
    private lateinit var imageViewSeen: ImageView
    private lateinit var imageViewSelected: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_candidate_details_recruiter, container, false)
        textViewName = v.textViewName
        textViewCollege = v.textViewCollege
        textViewCourse = v.textViewCourse
        textViewSemester = v.textViewSemester
        textViewPhone = v.textViewPhone
        textViewPreviousWork = v.textViewWorkDescription
        textViewResume = v.textViewResumeDescription
        imageViewSeen = v.imageViewSeen
        imageViewSelected = v.imageViewSelected
        progressCircularLayout = v.progress_circular_layout
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recruiterViewModel = ViewModelProvider(requireActivity()).get(RecruiterViewModel::class.java)

        recruiterViewModel.responseGetApplicationById().observe(viewLifecycleOwner, {
            responseGetApplicationByIdRecruiter = it
            populateViews(it)
        })

        recruiterViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_LONG).show()
            }
        })

        recruiterViewModel.responseMarkAsSeen().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                //Take action as per response
                if(it.code==200 || it.code == 403)
                    imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
        })

        recruiterViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                //Take action as per response
                if(it.code==200 || it.code == 403)
                    imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
        })

        imageViewSeen.setOnClickListener {
            markSeen()
        }

        imageViewSelected.setOnClickListener {
            markSelected()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidate Details"
    }

    private fun populateViews(it: ResponseGetApplicationByIdRecruiter) {
        if (it.code == 200) {
            textViewName.text = it.application?.applicant_name
            textViewCollege.text = it.application?.applicant_collegeName
            textViewCourse.text = it.application?.applicant_course
            textViewSemester.text = it.application?.applicant_semester
            textViewPhone.text = it.application?.applicant_phone
            textViewPreviousWork.text = it.application?.previousWork
            textViewResume.text = it.application?.resume
            it.application?.is_Seen?.let {
                if (it)
                    imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            it.application?.is_Selected?.let {
                if (it)
                    imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
            }
        } else {
            Toast.makeText(context, it.message, LENGTH_LONG).show()
        }
        progressCircularLayout.visibility = View.INVISIBLE
    }

    private fun markSeen() {
        responseGetApplicationByIdRecruiter?.let {
            var status = true
            if (it.application != null)
                status = it.application?.is_Seen!!
            progressCircularLayout.visibility = View.VISIBLE
            recruiterViewModel.markSeen(!status)
        }
    }

    private fun markSelected() {
        responseGetApplicationByIdRecruiter?.let {
            var status = true
            if (it.application != null)
                status = it.application?.is_Selected!!
            progressCircularLayout.visibility = View.VISIBLE
            recruiterViewModel.markSelected(!status)
        }
    }

    companion object {
        const val APPLICATION_ID = "ApplicationId"
    }
}