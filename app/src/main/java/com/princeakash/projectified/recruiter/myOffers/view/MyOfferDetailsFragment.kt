package com.princeakash.projectified.recruiter.myOffers.view

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
//import com.princeakash.projectified.CustomProgressBar
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_my_offer_details.view.*

class MyOfferDetailsFragment() : Fragment() {

    //Views
    private lateinit var editTextOfferName: TextInputEditText
    private lateinit var editTextRequirements: TextInputEditText
    private lateinit var editTextSkills: TextInputEditText
    private lateinit var editTextExpectations: TextInputEditText
    private lateinit var switchVisibility: SwitchMaterial
    private lateinit var buttonEditDetails: Button
    private lateinit var buttonViewApplicants: Button
    private lateinit var buttonDelist: Button
    private lateinit var progressCircularLayout: RelativeLayout
    private lateinit var listener: CompoundButton.OnCheckedChangeListener

    //ViewModel
    private lateinit var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel

    //Offer Data
    private var offerId: String = ""



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

        buttonDelist.setOnClickListener {
            progressCircularLayout.visibility = View.VISIBLE
            delistOpportunity()
        }

        buttonEditDetails.setOnClickListener {
            updateOffer()
        }

        buttonViewApplicants.setOnClickListener {
            viewApplicants()
        }


        listener = CompoundButton.OnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            run {
                progressCircularLayout.visibility = View.VISIBLE
                recruiterExistingOffersViewModel.toggleVisibility(offerId, BodyToggleVisibility(isChecked))
            }
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)

        recruiterExistingOffersViewModel.responseGetOfferByIdRecruiter().observe(viewLifecycleOwner, {
            populateViews(it)
            progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterExistingOffersViewModel.responseToggleVisibility().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
        })

        recruiterExistingOffersViewModel.responseUpdateOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
                fetchOfferDetails()
            }
        })

        recruiterExistingOffersViewModel.responseDeleteOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
                parentFragmentManager.popBackStackImmediate()
            }
        })

        recruiterExistingOffersViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_LONG).show()
            }
        })

        if (savedInstanceState == null) {
            //First loadup of Fragment
            offerId = requireArguments().getString(OFFER_ID)!!
            fetchOfferDetails()
        } else {
            //Restore state
            offerId = savedInstanceState.getString(OFFER_ID)!!
        }
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
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
        if (editTextOfferName.text == null || editTextOfferName.text!!.equals("")) {
            editTextOfferName.error = "Enter offer name."
            return
        }

        if (editTextRequirements.text == null || editTextRequirements.text!!.equals("")) {
            editTextRequirements.error = "Enter requirements."
            return
        }

        if (editTextSkills.text == null || editTextSkills.text!!.equals("")) {
            editTextSkills.error = "Enter skills."
            return
        }

        if (editTextExpectations.text == null || editTextExpectations.text!!.equals("")) {
            editTextExpectations.error = "Enter expectation."
            return
        }

        val offerName = editTextOfferName.text.toString()
        val requirement = editTextRequirements.text.toString()
        val skills = editTextSkills.text.toString()
        val expectation = editTextExpectations.text.toString()

        progressCircularLayout.visibility = View.VISIBLE
        val bodyUpdateOffer = BodyUpdateOffer(offerName, requirement, skills, expectation)
        recruiterExistingOffersViewModel.updateOffer(offerId, bodyUpdateOffer)
    }

    private fun fetchOfferDetails() {
        progressCircularLayout.visibility = View.VISIBLE
        recruiterExistingOffersViewModel.getOfferByIdRecruiter(offerId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OFFER_ID, offerId)
    }

    private fun populateViews(responseGetOfferByIdRecruiter: ResponseGetOfferByIdRecruiter) {
        switchVisibility.setOnCheckedChangeListener(null)
        editTextOfferName.setText(responseGetOfferByIdRecruiter.offer?.offer_name)
        editTextRequirements.setText(responseGetOfferByIdRecruiter.offer?.requirements)
        editTextSkills.setText(responseGetOfferByIdRecruiter.offer?.skills)
        editTextExpectations.setText(responseGetOfferByIdRecruiter.offer?.expectation)
        switchVisibility.isChecked = (responseGetOfferByIdRecruiter.offer?.is_visible != null) && (responseGetOfferByIdRecruiter.offer?.is_visible == true)
        switchVisibility.setOnCheckedChangeListener(listener)
    }

    private fun delistOpportunity() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this offer? You cannot undo this action later.")
                .setPositiveButton("Yes") { dialog, which ->
                    progressCircularLayout.visibility = View.VISIBLE
                    recruiterExistingOffersViewModel.deleteOffer(offerId)
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }

    companion object {
        val OFFER_ID = "OfferId"
    }
}