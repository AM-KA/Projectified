package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.GetOffersByDomainAdapter
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import com.princeakash.projectified.databinding.FragAvailableOffersBinding
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel

class GetOffersByDomainFragment : Fragment() , GetOffersByDomainAdapter.GetOffersListener{

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private var offerList: ArrayList<OfferCardModelCandidate> = ArrayList()
    private lateinit var binding: FragAvailableOffersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.frag_available_offers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragAvailableOffersBinding.bind(view)
        binding.buttonAddOffer.setOnClickListener { proceedToAddOffer() }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null){
            recruiterCandidateViewModel.nullifySafeToVisitDomainOffers()
        }
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOffersByDomain().observe(viewLifecycleOwner, {
            offerList = it.offers as ArrayList<OfferCardModelCandidate>
            binding.recyclerViewOffers.adapter = GetOffersByDomainAdapter(offerList, this)
            binding.progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.safeToVisitDomainOfferDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.offers_to_view_details)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@GetOffersByDomainFragment.context, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offers"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        //Populate new fragment with details of offer.
        binding.progressCircularLayout.visibility = View.VISIBLE
        val offerId = offerList[itemPosition].offer_id
        recruiterCandidateViewModel.getOfferById(offerId)
    }

    private fun proceedToAddOffer(){
        findNavController().navigate(R.id.offers_to_add_offer)
    }
}