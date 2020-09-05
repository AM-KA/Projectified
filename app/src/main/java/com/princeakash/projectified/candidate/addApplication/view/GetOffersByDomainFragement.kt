package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.GetOffersByDomainAdapter
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import com.princeakash.projectified.recruiter.myOffers.model.MyOffersAdapter
import com.princeakash.projectified.recruiter.myOffers.model.OfferCardModelRecruiter
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicantsFragment.Companion.OFFER_ID
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferDetailsFragment
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferDetailsFragment.Companion.OFFER_ID
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_my_offers.*
import kotlinx.android.synthetic.main.frag_my_offers.view.*

class GetOffersByDomainFragement : Fragment() , GetOffersByDomainAdapter.GetOffersListener{

    var candidateAddApplicationViewModel: CandidateAddApplicationViewModel? = null
    var offerList: ArrayList<OfferCardModelCandidate> = ArrayList()
    var errorString:String? = null


    //Determining whether re-fetching data is required or not
    var detailsViewed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        candidateAddApplicationViewModel = ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)
        candidateAddApplicationViewModel!!.responseGetOffersByDomain.observe(this, {
            offerList = it?.offers as ArrayList<OfferCardModelCandidate>
            recyclerViewOffers.adapter?.notifyDataSetChanged()
        })
        candidateAddApplicationViewModel!!.errorString.observe(this, {
            errorString = it
            Toast.makeText(this@GetOffersByDomainFragement.context, errorString, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        /*savedInstanceState?.let {
            offerList = savedInstanceState.getSerializable(OFFERS_LIST) as ArrayList<OfferCardModelRecruiter>
        }*/
        return inflater.inflate(R.layout.frag_available_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Start fetching offer list
        if(savedInstanceState == null || savedInstanceState.getBoolean(DETAILS_VIEWED)) {
            candidateAddApplicationViewModel!!.getOffersByDomain()
            detailsViewed = false
        }

        view.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = GetOffersByDomainAdapter(offerList, this@GetOffersByDomainFragement)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putSerializable(OFFERS_LIST, offerList)
        outState.putBoolean(DETAILS_VIEWED, detailsViewed)
    }

    override fun onViewDetailsClick(itemPosition: Int) {

        //Set detailsViewed to true so that on coming back, we can re-load all data
        detailsViewed = true
        //Populate new fragment with details of offer.
        val bundle = Bundle()
        bundle.putString(GetOfferDetailsCandidateFragment._ID, offerList.get(itemPosition).offer_id)
        parentFragmentManager.beginTransaction()
                .add(R.id.fragment_offers, GetOfferDetailsCandidateFragment::class.java, bundle, "GetOfferDetailsCandidateFragment")
                .addToBackStack(null)
                .commit()
    }

    companion object {
        val OFFERS_LIST = "OffersList"
        val DETAILS_VIEWED = "DetailsViewed"
    }
}
