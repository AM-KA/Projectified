package com.princeakash.projectified.recruiter.myOffers.view

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
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
    private var switchVisibility: SwitchMaterial? = null
    private var buttonEditDetails: Button? = null
    private var buttonViewApplicants: Button? = null
    private var buttonDelist: Button? = null
    private lateinit var progressCircularLayout: RelativeLayout

    //Determines whether textviews are editable or not
    private var editable = false

    //ViewModels and Observable Objects
    private var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel? = null
    private var responseGetOfferByIdRecruiter: ResponseGetOfferByIdRecruiter? = null
    private var responseDeleteOffer: ResponseDeleteOffer? = null
    private var responseUpdateOffer: ResponseUpdateOffer? = null
    private var responseToggleVisibility: ResponseToggleVisibility? = null
    private var error: String? = null

    //Offer Data
    private var offerId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_my_offer_details, container, false)
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
        editTextOfferName = v.editTextOfferName
        editTextExpectations = v.editTextExpectation
        editTextSkills = v.editTextSkills
        editTextRequirements = v.editTextRequirement
        switchVisibility = v.switchAllow
        buttonEditDetails = v.buttonEditDetails
        buttonViewApplicants = v.buttonViewApplicants
        buttonDelist = v.buttonDelist
        progressCircularLayout = v.progress_circular_layout

        buttonDelist?.setOnClickListener {
            progressCircularLayout.visibility = View.VISIBLE
            delistOpportunity()
        }

        buttonEditDetails?.setOnClickListener {
            editable = !editable
            setEditable()
            if (!editable)
                updateOffer()
        }

        buttonViewApplicants?.setOnClickListener {
            viewApplicants()
        }

        switchVisibility?.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            run {
                progressCircularLayout.visibility = View.VISIBLE
                recruiterExistingOffersViewModel!!.toggleVisibility(offerId!!, BodyToggleVisibility(isChecked))
            }
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)

        recruiterExistingOffersViewModel!!.responseGetOfferByIdRecruiter.observe(viewLifecycleOwner, {
            responseGetOfferByIdRecruiter = it
            populateViews()
            editable = false
            setEditable()
        })

        recruiterExistingOffersViewModel!!.responseToggleVisibility.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                responseToggleVisibility = it
                //TODO: Show Toast
            }
        })

        recruiterExistingOffersViewModel!!.responseUpdateOffer.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                responseUpdateOffer = it
                //TODO: Show Toast
                fetchOfferDetails()
            }
        })

        recruiterExistingOffersViewModel!!.responseDeleteOffer.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                responseDeleteOffer = it
                //TODO: Show Toast
                parentFragmentManager.popBackStackImmediate()
            }
        })

        recruiterExistingOffersViewModel!!.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                error = it
                Toast.makeText(context, error, LENGTH_SHORT).show()
            }
        })

        if (savedInstanceState == null) {
            //First loadup of Fragment
            offerId = requireArguments().getString(OFFER_ID)
            fetchOfferDetails()
        } else {
            //Restore state
            //responseGetOfferByIdRecruiter = savedInstanceState.getSerializable(RESPONSE_GET_OFFER_DETAILS) as ResponseGetOfferByIdRecruiter
            offerId = savedInstanceState.getString(OFFER_ID)
            editable = savedInstanceState.getBoolean(EDITABLE_STATUS)
            responseGetOfferByIdRecruiter?.let {
                populateViews()
            }
            setEditable()
        }
    }

    private fun viewApplicants() {
        val bundle = Bundle()
        bundle.putString(OFFER_ID, offerId)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, MyOfferApplicantsFragment::class.java, bundle, "MyOfferApplicantsFragment")
                .addToBackStack("MyOfferApplicants" + offerId)
                .commit()
    }

    private fun updateOffer() {
        if (editTextOfferName!!.text == null || editTextOfferName!!.text!!.equals("")) {
            editTextOfferName!!.error = "Enter offer name."
            return
        }

        if (editTextRequirements!!.text == null || editTextRequirements!!.text!!.equals("")) {
            editTextRequirements!!.error = "Enter requirements."
            return
        }

        if (editTextSkills!!.text == null || editTextSkills!!.text!!.equals("")) {
            editTextSkills!!.error = "Enter skills."
            return
        }

        if (editTextExpectations!!.text == null || editTextExpectations!!.text!!.equals("")) {
            editTextExpectations!!.error = "Enter expectation."
            return
        }

        val offerName = editTextOfferName!!.text!!.toString()
        val requirement = editTextRequirements!!.text!!.toString()
        val skills = editTextSkills!!.text!!.toString()
        val expectation = editTextExpectations!!.text!!.toString()

        progressCircularLayout.visibility = View.VISIBLE
        val bodyUpdateOffer = BodyUpdateOffer(offerName, requirement, skills, expectation)
        recruiterExistingOffersViewModel!!.updateOffer(offerId!!, bodyUpdateOffer)
    }

    private fun fetchOfferDetails() {
        //TODO:Start ProgressBar
        progressCircularLayout.visibility = View.VISIBLE
        offerId?.let { recruiterExistingOffersViewModel!!.getOfferByIdRecruiter(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putSerializable(RESPONSE_GET_OFFER_DETAILS, responseGetOfferByIdRecruiter)
        outState.putString(OFFER_ID, responseGetOfferByIdRecruiter?.offer?.offer_id)
        outState.putBoolean(EDITABLE_STATUS, editable)
    }

    fun populateViews() {
        editTextOfferName?.setText(responseGetOfferByIdRecruiter?.offer?.offer_name)
        editTextRequirements?.setText(responseGetOfferByIdRecruiter?.offer?.requirements)
        editTextSkills?.setText(responseGetOfferByIdRecruiter?.offer?.skills)
        editTextExpectations?.setText(responseGetOfferByIdRecruiter?.offer?.expectation)
        switchVisibility?.isChecked = (responseGetOfferByIdRecruiter?.offer?.is_visible != null) && (responseGetOfferByIdRecruiter?.offer?.is_visible == true)
    }

    fun setEditable() {
        editTextOfferName?.isEnabled = editable
        editTextExpectations?.isEnabled = editable
        editTextSkills?.isEnabled = editable
        editTextRequirements?.isEnabled = editable
        if (editable) {
            buttonEditDetails?.text = "Save Details"
        } else {
            buttonEditDetails?.text = "Edit Details"
        }
        progressCircularLayout.visibility = View.INVISIBLE
    }

    fun delistOpportunity() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this offer? You cannot undo this action later.")
                .setPositiveButton("Yes") { dialog, which ->
                    progressCircularLayout.visibility = View.VISIBLE
                    recruiterExistingOffersViewModel!!.deleteOffer(offerId!!)
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }

    companion object {
        val RESPONSE_GET_OFFER_DETAILS = "ResponseGetOfferDetails"
        val OFFER_ID = "OfferId"
        val EDITABLE_STATUS = "EditableStatus"
    }
}