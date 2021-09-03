package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragMyOfferDetailsBinding
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel

class MyOfferDetailsFragment: Fragment() {

    private lateinit var listener: CompoundButton.OnCheckedChangeListener

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragMyOfferDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.frag_my_offer_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyOfferDetailsBinding.bind(view)
        binding.buttonDelist.setOnClickListener { deListOpportunity() }
        binding.buttonEditDetails.setOnClickListener { updateOffer() }
        binding.buttonViewApplicants.setOnClickListener { viewApplicants() }
        listener = CompoundButton.OnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            run {
                Log.d(TAG, "onCreateView: Running stuff")
                binding.progressCircularLayout.visibility = View.VISIBLE
                recruiterCandidateViewModel.toggleVisibility(isChecked)
            }
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null)
            recruiterCandidateViewModel.nullifySafeToVisitOfferDetails()

    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOfferByIdRecruiter().observe(viewLifecycleOwner, {
            populateViews(it)
            binding.progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.responseToggleVisibility().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseUpdateOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseDeleteOffer().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.safeToVisitCandidates().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.offer_details_to_view_applicants)
                }
            }
        })

        recruiterCandidateViewModel.safeToVisitOfferList().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.offer_details_to_my_offers)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
    }

    private fun viewApplicants() {
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.getOfferApplicants()
    }

    private fun updateOffer() {
        binding.apply{
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

            recruiterCandidateViewModel.updateOffer(offerName, requirement, skills, expectation)
        }
    }

    private fun populateViews(responseGetOfferByIdRecruiter: ResponseGetOfferByIdRecruiter) {
        binding.apply{
            switchAllow.setOnCheckedChangeListener(null)
            editTextOfferName.setText(responseGetOfferByIdRecruiter.offer?.offer_name)
            editTextRequirements.setText(responseGetOfferByIdRecruiter.offer?.requirements)
            editTextSkills.setText(responseGetOfferByIdRecruiter.offer?.skills)
            editTextExpectations.setText(responseGetOfferByIdRecruiter.offer?.expectation)
            switchAllow.isChecked = (responseGetOfferByIdRecruiter.offer?.is_visible != null) && (responseGetOfferByIdRecruiter.offer?.is_visible == true)
            switchAllow.setOnCheckedChangeListener(listener)
        }
    }

    private fun deListOpportunity() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this offer? You cannot undo this action later.")
                .setPositiveButton("Yes") { _, _ ->
                    binding.progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.deleteOffer()
                }
                .setNegativeButton("No") { _, _ -> }
                .create().show()
    }

    companion object {
        const val TAG = "OfferDetails"
    }
}