package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
//import com.princeakash.projectified.CustomProgressBar
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterViewModel
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
    private lateinit var recruiterViewModel: RecruiterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_my_offer_details, container, false)

        Log.d(TAG, "onCreateView: stating to create view")
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
                Log.d(TAG, "onCreateView: Running stuff")
                progressCircularLayout.visibility = View.VISIBLE
                recruiterViewModel.toggleVisibility(isChecked)
            }
        }

        Log.d(TAG, "onCreateView: Returning view")
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        //recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)
        recruiterViewModel = ViewModelProvider(requireActivity()).get(RecruiterViewModel::class.java)

        /*recruiterExistingOffersViewModel.offerIdForDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{ offerId->
                Log.d("OffDetails", "onViewCreated: Beginning to fetch data")
                progressCircularLayout.visibility = View.VISIBLE
                recruiterExistingOffersViewModel.getOfferByIdRecruiter(offerId)
            }
        })*/
        
        recruiterViewModel.responseGetOfferByIdRecruiter().observe(viewLifecycleOwner, {
            Log.d(TAG, "onViewCreated: Got the offer")
            populateViews(it)
            progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterViewModel.responseToggleVisibility().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
        })

        recruiterViewModel.responseUpdateOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                //progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
        })

        recruiterViewModel.responseDeleteOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
        })

        recruiterViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_LONG).show()
            }
        })

        recruiterViewModel.safeToVisitCandidates().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ safeToVisit->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.offer_details_to_view_applicants)
                }
            }
        })

        recruiterViewModel.safeToVisitOfferList().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ safeToVisit->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.offer_details_to_my_offers)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
    }

    private fun viewApplicants() {
        progressCircularLayout.visibility = View.VISIBLE
        recruiterViewModel.getOfferApplicants()
        /*val bundle = Bundle()
        bundle.putString(OFFER_ID, offerId)
        parentFragmentManager.beginTransaction()
        //childFragmentManager.beginTransaction()
                //.replace(R.id.fragment_offers, MyOfferApplicantsFragment::class.java, bundle, "MyOfferApplicantsFragment")
                .replace(R.id.fragment, MyOfferApplicantsFragment::class.java, bundle, "MyOfferApplicantsFragment")
                .addToBackStack("MyOfferApplicants" + offerId)
                .commit()*/
        /*val fragment = MyOfferApplicantsFragment()
        fragment.arguments = bundle
        StateManager.getInstance().showFragment(R.id.myOffersFragment, fragment)*/
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

        recruiterViewModel.updateOffer(offerName, requirement, skills, expectation)
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
                    recruiterViewModel.deleteOffer()
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }

    companion object {
        val OFFER_ID = "OfferId"
        val TAG = "OfferDetails"
    }
}