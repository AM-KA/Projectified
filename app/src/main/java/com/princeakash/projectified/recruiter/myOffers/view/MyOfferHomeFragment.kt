package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragMyOffersBinding
import com.princeakash.projectified.recruiter.myOffers.model.MyOffersAdapter
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel

class MyOfferHomeFragment : Fragment(R.layout.frag_my_offers), MyOffersAdapter.MyOffersListener {

    //ViewModel
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragMyOffersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyOffersBinding.bind(view)

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null) {
            recruiterCandidateViewModel.nullifySafeToVisitOfferList()
            Log.d("Hi", "onViewCreated: New inst")
        }

        binding.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.safeToVisitOfferDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { safeToVisit ->
                if (safeToVisit) {
                    binding.progressCircularLayout.visibility = View.INVISIBLE
                    findNavController().navigate(R.id.home_to_offer_details)
                }
            }
        })

        recruiterCandidateViewModel.responseGetOffersByRecruiter().observe(viewLifecycleOwner, { response ->
            binding.progressCircularLayout.visibility = View.INVISIBLE
            var offers = response.offers
            if(offers == null)
                offers = ArrayList()
            binding.recyclerViewOffers.adapter = MyOffersAdapter(offers, this@MyOfferHomeFragment)
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@MyOfferHomeFragment.context, message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Offers"
    }

    override fun onViewDetailsClick(offerId: String) {
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.getOfferByIdRecruiter(offerId)
    }
}