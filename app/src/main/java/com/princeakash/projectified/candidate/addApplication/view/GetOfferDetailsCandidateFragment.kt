package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import kotlinx.android.synthetic.main.frag_apply_opportunity_view.view.*

class GetOfferDetailsCandidateFragment : Fragment(){

    private lateinit var textViewRequirements: TextView
    private lateinit var textViewSkills: TextView
    private lateinit var textViewExpectations: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewCollege: TextView
    private lateinit var textViewSemester: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var buttonApplyOpportunity: Button
    private lateinit var buttonCancelOpportunity:Button

    //View Models and Observable Objects
    private lateinit var candidateAddApplicationsViewModel: CandidateAddApplicationViewModel
    private lateinit var  responseGetOfferById : ResponseGetOfferById

    private var error:String? = null

    private var offerId:String ?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_apply_opportunity_view, container, false)

        candidateAddApplicationsViewModel= ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)

        candidateAddApplicationsViewModel.responseGetOfferById.observe(viewLifecycleOwner, {
            responseGetOfferById = it
            populateViews()
        })


        candidateAddApplicationsViewModel.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                error = it
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })

        textViewExpectations = v.textViewExpectationData
        textViewSkills = v.textViewSkillsData
        textViewRequirements = v.textViewRequirementData
        textViewName=v.textViewNameData
        textViewCollege=v.textViewCollegeData
        textViewSemester=v.textViewSemesterData
        textViewCourse=v.textViewCourseData
        buttonApplyOpportunity=v.buttonApply
        buttonCancelOpportunity=v.buttonCancel

        if(savedInstanceState==null) {
            offerId=requireArguments().getString(OFFER_IDC)
            fetchOfferDetails()
        }
        else{
            offerId = savedInstanceState.getString(OFFER_IDC)
            populateViews()
        }

        buttonApplyOpportunity.setOnClickListener()
        {

            val bundle = Bundle()
            bundle.putString(OFFER_IDC, offerId)

            parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_apply, ApplyOpportunityFragment::class.java, bundle, "ApplyOpportunityFragment")
                        .addToBackStack("ApplyOpportunity")
                        .commit()
            }

        buttonCancelOpportunity.setOnClickListener()
        {

            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_apply, HomeFragment::class.java, null, "Home Fragment")
                    .addToBackStack("HomeFragment")
                    .commit()

        }


        return v
    }



    private fun fetchOfferDetails() {
        //TODO:Start ProgressBar
        candidateAddApplicationsViewModel.getoffersById(offerId!!)
    }

    private fun populateViews() {
        textViewExpectations.setText(responseGetOfferById.offer.expectation)
        textViewSkills.setText(responseGetOfferById.offer.skills)
        textViewRequirements.setText(responseGetOfferById.offer.requirements)
        textViewName.setText(responseGetOfferById.offer.recruiter_name)
        textViewCollege.setText(responseGetOfferById.offer.recruiter_collegeName)
        textViewSemester.setText(responseGetOfferById.offer.recruiter_semester)
        textViewCourse.setText(responseGetOfferById.offer.recruiter_course)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OFFER_IDC, offerId)
    }

    companion object{
        const val OFFER_IDC ="offerId"
    }
}