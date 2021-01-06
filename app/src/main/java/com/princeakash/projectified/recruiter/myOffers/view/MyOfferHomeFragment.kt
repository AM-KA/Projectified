package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.OfferCardModelRecruiter
import com.princeakash.projectified.recruiter.myOffers.model.MyOffersAdapter
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel
import kotlinx.android.synthetic.main.frag_my_offers.*
import kotlinx.android.synthetic.main.frag_my_offers.view.*


class MyOfferHomeFragment() : Fragment(), MyOffersAdapter.MyOffersListener {

    //ViewModel
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel

    //Offer List for RecyclerView
    var offerList: ArrayList<OfferCardModelRecruiter> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frag_my_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)

        recruiterCandidateViewModel.safeToVisitOfferDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { safeToVisit ->
                if (safeToVisit) {
                    Log.d("OFFERHOME", "onViewCreated: Its ok to visit offer details")
                    view.progress_circular_layout.visibility = View.INVISIBLE
                    this.findNavController().navigate(R.id.home_to_offer_details)
                }
            }
        })

        recruiterCandidateViewModel.responseGetOffersByRecruiter().observe(viewLifecycleOwner, {
            offerList = it.offers as ArrayList<OfferCardModelRecruiter>
            view.progress_circular_layout.visibility = View.INVISIBLE
            view.recyclerViewOffers.adapter = MyOffersAdapter(offerList, this@MyOfferHomeFragment)
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                requireView().progress_circular_layout.visibility = View.INVISIBLE
                Toast.makeText(this@MyOfferHomeFragment.context, it, LENGTH_LONG).show()
            }
        })

        if(savedInstanceState==null){
            recruiterCandidateViewModel.nullifySafeToVisitOfferList()
        }

        view.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Offers"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        progress_circular_layout.visibility = View.VISIBLE
        val offId = offerList.get(itemPosition).offer_id
        recruiterCandidateViewModel.getOfferByIdRecruiter(offId)
    }
    
    companion object{
        private const val TAG = "MyOfferHomeFragment"
    }
}