package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.GetOffersByDomainAdapter
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateViewModel
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterViewModel
import kotlinx.android.synthetic.main.frag_available_offers.view.*

class GetOffersByDomainFragment : Fragment() , GetOffersByDomainAdapter.GetOffersListener{

    private lateinit var candidateAddApplicationViewModel: CandidateViewModel
    private lateinit var recruiterViewModel: RecruiterViewModel
    private var offerList: ArrayList<OfferCardModelCandidate> = ArrayList()
    private lateinit var recyclerView:RecyclerView
    private lateinit var currentDomainName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_available_offers, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewOffers)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), VERTICAL, false)
        view.progress_circular_layout.visibility = View.VISIBLE
        view.buttonAddOffer.setOnClickListener {
            proceedToAddOffer()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        candidateAddApplicationViewModel = ViewModelProvider(requireActivity()).get(CandidateViewModel::class.java)
        recruiterViewModel = ViewModelProvider(requireActivity()).get(RecruiterViewModel::class.java)

        candidateAddApplicationViewModel.responseGetOffersByDomain().observe(viewLifecycleOwner, {
            offerList = it.offers as ArrayList<OfferCardModelCandidate>
            recyclerView.adapter = GetOffersByDomainAdapter(offerList, this)
            view.progress_circular_layout.visibility = View.INVISIBLE
        })

        candidateAddApplicationViewModel.safeToVisitOfferDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.offers_to_view_details)
                }
            }
        })

        candidateAddApplicationViewModel.currentDomainName().observe(viewLifecycleOwner, {
            currentDomainName = it
        })

        candidateAddApplicationViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                view.progress_circular_layout.visibility = View.INVISIBLE
                Toast.makeText(this@GetOffersByDomainFragment.context, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offers"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        //Populate new fragment with details of offer.
        requireView().progress_circular_layout.visibility = View.VISIBLE
        val offerId = offerList.get(itemPosition).offer_id
        candidateAddApplicationViewModel.getoffersById(offerId)
    }

    private fun proceedToAddOffer(){
        recruiterViewModel.setDomain(currentDomainName)
        requireView().findNavController().navigate(R.id.offers_to_add_offer)
    }
    companion object {
        private const val TAG = "GetOffersByDomainFragme"
    }
}
