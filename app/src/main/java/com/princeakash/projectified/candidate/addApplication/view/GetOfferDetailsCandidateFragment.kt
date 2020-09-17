package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import kotlinx.android.synthetic.main.frag_apply_opportunity_view.view.*
import kotlinx.android.synthetic.main.frag_my_offer_details.view.editTextExpectation
import kotlinx.android.synthetic.main.frag_my_offer_details.view.editTextRequirement
import kotlinx.android.synthetic.main.frag_my_offer_details.view.editTextSkills
import kotlinx.android.synthetic.main.frag_myapplicationdetail.view.*

class GetOfferDetailsCandidateFragment : Fragment(){

    private var editTextRequirements: TextInputEditText? = null
    private var editTextSkills: TextInputEditText? = null
    private var editTextExpectations: TextInputEditText? = null
    private var editTextName: TextInputEditText? =null
    private var editTextCollege: TextInputEditText?=null
    private var editTextSemester: TextInputEditText?=null
    private var editTextCourse: TextInputEditText?=null
    private var buttonApplyOpportunity: Button?=null
    private var buttonCancelOpportunity:Button?=null

    //View Models and Observable Objects
    private var candidateAddApplicationsViewModel: CandidateAddApplicationViewModel? = null
    private var  responseGetOfferById : ResponseGetOfferById?=null

    private var error:String? = null

    private var offerId:String ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        candidateAddApplicationsViewModel= ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)

        candidateAddApplicationsViewModel!!.responseGetOfferById.observe(viewLifecycleOwner, {
            responseGetOfferById = it
            populateViews()
        })


          candidateAddApplicationsViewModel!!.errorString.observe(viewLifecycleOwner, {
              error = it
              Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
          })
      }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_apply_opportunity_view, container, false)

        editTextExpectations = v.editTextExpectation
        editTextSkills = v.editTextSkills
        editTextRequirements = v.editTextRequirement
        editTextName=v.editTextName
        editTextCollege=v.editTextCollege
        editTextSemester=v.editTextSemester
        editTextCourse=v.editTextCourse
        buttonApplyOpportunity=v.buttonApply

        if(savedInstanceState==null) {
            offerId=requireArguments().getString(offerId)
            fetchOfferDetails()
        }
        else{
            offerId = savedInstanceState.getString(OFFER_IDC)
            responseGetOfferById?.let{
                populateViews()
            }
        }

        buttonApplyOpportunity?.setOnClickListener()
        {

            val bundle = Bundle()
            bundle.putString(OFFER_IDC, offerId)

            parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_offers, ApplyOpportunityFragment::class.java, bundle, "ApplyOpportunityFragment")
                        .addToBackStack(null)
                        .commit()
            }
        buttonCancelOpportunity?.setOnClickListener()
        {

            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_offers, HomeFragment::class.java, null, "Home Fragment")
                    .addToBackStack(null)
                    .commit()

        }


        return v
    }

    private fun fetchOfferDetails() {
        //TODO:Start ProgressBar
        offerId?.let { candidateAddApplicationsViewModel!!.getoffersById(it) }
    }

    fun populateViews() {
        editTextExpectations?.setText(responseGetOfferById?.offer?.expectation)
        editTextSkills?.setText(responseGetOfferById?.offer?.skills)
        editTextRequirements?.setText(responseGetOfferById?.offer?.requirements)
        editTextName?.setText(responseGetOfferById?.offer?.recruiter_name)
        editTextCollege?.setText(responseGetOfferById?.offer?.recruiter_collegeName)
        editTextSemester?.setText(responseGetOfferById?.offer?.recruiter_semester)
        editTextCourse?.setText(responseGetOfferById?.offer?.recruiter_course)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(GetOfferDetailsCandidateFragment.OFFER_IDC, offerId)
    }

    companion object{
        val OFFER_IDC ="offerId"
    }
}