package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_my_offer_details.view.*

class MyOfferDetailsFragment() : Fragment() {

    //Views
    private var editTextOfferName: TextInputEditText? = null
    private var editTextRequirements: TextInputEditText? = null
    private var editTextSkills: TextInputEditText? = null
    private var editTextExpectations: TextInputEditText? = null
    private var switchVisibility: Switch? = null
    private var buttonEditDetails: Button? = null
    private var buttonViewApplicants:Button? = null
    private var buttonDelist:Button? = null

    //Determines whether textviews are editable or not
    private var editable = false

    //ViewModels and Observable Objects
    private var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel? = null
    private var responseGetOfferByIdRecruiter: ResponseGetOfferByIdRecruiter? = null
    private var responseDeleteOffer: ResponseDeleteOffer? = null
    private var responseUpdateOffer: ResponseUpdateOffer?= null
    private var responseToggleVisibility: ResponseToggleVisibility? = null
    private var error:String? = null

    //Offer Data
    private var offerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)

        recruiterExistingOffersViewModel!!.responseGetOfferByIdRecruiter.observe(viewLifecycleOwner, {
            responseGetOfferByIdRecruiter = it
            populateViews()
            editable = false
            setEditable()
        })

        recruiterExistingOffersViewModel!!.responseToggleVisibility.observe(viewLifecycleOwner, {
            responseToggleVisibility = it
            //TODO: Show Toast
        })

        recruiterExistingOffersViewModel!!.responseUpdateOffer.observe(viewLifecycleOwner, {
            responseUpdateOffer = it
            //TODO: Show Toast and start ProgressBar
            fetchOfferDetails()
        })

        recruiterExistingOffersViewModel!!.responseDeleteOffer.observe(viewLifecycleOwner, {
            responseDeleteOffer = it
            //TODO: Show Toast
            parentFragmentManager.popBackStackImmediate()
        })

        recruiterExistingOffersViewModel!!.errorString.observe(viewLifecycleOwner, {
            error = it
            Toast.makeText(context, error, LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_my_offer_details, container, false)

        editTextOfferName = v.editTextOfferName
        editTextExpectations = v.editTextExpectation
        editTextSkills = v.editTextSkills
        editTextRequirements = v.editTextRequirement
        switchVisibility = v.switchAllow
        buttonEditDetails = v.buttonEditDetails
        buttonViewApplicants = v.buttonViewApplicants
        buttonDelist = v.buttonDelist

        if(savedInstanceState == null) {
            //First loadup of Fragment
            offerId = arguments?.getString(OFFER_ID)
            fetchOfferDetails()
        }
        else{
            //Restore state
            //responseGetOfferByIdRecruiter = savedInstanceState.getSerializable(RESPONSE_GET_OFFER_DETAILS) as ResponseGetOfferByIdRecruiter
            offerId = savedInstanceState.getString(OFFER_ID)
            editable = savedInstanceState.getBoolean(EDITABLE_STATUS)
            responseGetOfferByIdRecruiter?.let {
                populateViews()
            }
            setEditable()
        }

        buttonDelist?.setOnClickListener{
            delistOpportunity()
        }

        buttonEditDetails?.setOnClickListener{
            editable = !editable
            setEditable()
            if(editable)
               updateOffer()
        }

        buttonViewApplicants?.setOnClickListener{
            viewApplicants()
        }

        switchVisibility?.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            run {
                recruiterExistingOffersViewModel!!.toggleVisibility(offerId!!, BodyToggleVisibility(isChecked))
            }
        }
        return v
    }

    private fun viewApplicants() {
        val bundle = Bundle()
        bundle.putString(OFFER_ID, offerId)
        parentFragmentManager.beginTransaction()
                .add(R.id.fragment_offers, MyOfferApplicantsFragment::class.java, bundle, "MyOfferApplicantsFragment")
                .addToBackStack(null)
                .commit()
    }

    private fun updateOffer() {
        if(editTextOfferName!!.text == null || editTextOfferName!!.text!!.equals("")) {
            editTextOfferName!!.error = "Enter offer name."
            return
        }

        if(editTextRequirements!!.text == null || editTextRequirements!!.text!!.equals("")) {
            editTextRequirements!!.error = "Enter requirements."
            return
        }

        if(editTextSkills!!.text == null || editTextSkills!!.text!!.equals("")) {
            editTextSkills!!.error = "Enter skills."
            return
        }

        if(editTextExpectations!!.text == null || editTextExpectations!!.text!!.equals("")) {
            editTextExpectations!!.error = "Enter expectation."
            return
        }

        val offerName = editTextOfferName!!.text!!.toString()
        val requirement = editTextRequirements!!.text!!.toString()
        val skills = editTextSkills!!.text!!.toString()
        val expectation = editTextSkills!!.text!!.toString()
        val bodyUpdateOffer = BodyUpdateOffer(offerName, requirement, skills, expectation)
        recruiterExistingOffersViewModel!!.updateOffer(offerId!!, bodyUpdateOffer)
    }

    private fun fetchOfferDetails() {
        //TODO:Start ProgressBar
        offerId?.let { recruiterExistingOffersViewModel!!.getOfferByIdRecruiter(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putSerializable(RESPONSE_GET_OFFER_DETAILS, responseGetOfferByIdRecruiter)
        outState.putString(OFFER_ID, responseGetOfferByIdRecruiter?.offer?.offer_id)
        outState.putBoolean(EDITABLE_STATUS, editable)
    }

    fun populateViews(){
        editTextOfferName?.setText(responseGetOfferByIdRecruiter?.offer?.offer_name)
        editTextRequirements?.setText(responseGetOfferByIdRecruiter?.offer?.requirements)
        editTextSkills?.setText(responseGetOfferByIdRecruiter?.offer?.skills)
        editTextExpectations?.setText(responseGetOfferByIdRecruiter?.offer?.expectation)
        switchVisibility?.isChecked = (responseGetOfferByIdRecruiter?.offer?.is_visible != null) && (responseGetOfferByIdRecruiter?.offer?.is_visible == true)
    }

    fun setEditable(){
        editTextOfferName?.isEnabled = editable
        editTextExpectations?.isEnabled = editable
        editTextSkills?.isEnabled = editable
        editTextRequirements?.isEnabled = editable
        if(editable){
            buttonEditDetails?.text = "Save Details"
        }else{
            buttonEditDetails?.text = "Edit Details"
        }
    }

    fun delistOpportunity(){
        //TODO: Add DialogBox
        recruiterExistingOffersViewModel!!.deleteOffer(offerId!!)
    }

    companion object {
        val RESPONSE_GET_OFFER_DETAILS = "ResponseGetOfferDetails"
        val OFFER_ID = "OfferId"
        val EDITABLE_STATUS = "EditableStatus"
    }
}