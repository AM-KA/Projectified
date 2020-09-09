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
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateExistingApplicationViewModel
import kotlinx.android.synthetic.main.flag_apply_opportunity_self.*
import kotlinx.android.synthetic.main.frag_apply_opportunity_view.view.*
import kotlinx.android.synthetic.main.frag_my_offer_details.view.*
import kotlinx.android.synthetic.main.frag_my_offer_details.view.editTextExpectation
import kotlinx.android.synthetic.main.frag_my_offer_details.view.editTextRequirement
import kotlinx.android.synthetic.main.frag_my_offer_details.view.editTextSkills
import kotlinx.android.synthetic.main.frag_myapplicationdetail.*
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

      candidateAddApplicationsViewModel!!.responseGetOffersById.observe(viewLifecycleOwner, {
          responseGetOfferById = it
          populateView()
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
        buttonCancelOpportunity=v.buttonCancel

        if(savedInstanceState==null)
        {
            offerId=arguments?.getString(offerId)

            fetchOfferDetails()
        }
        else{

            offerId = savedInstanceState.getString((OFFER_IDC)
            responseGetOffersById?.let{
                populateViews()
            }
        }



    }





    companion object{

        val OFFER_IDC ="offerId"
    }
}