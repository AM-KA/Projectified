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
import kotlinx.android.synthetic.main.frag_available_offers.*
import kotlinx.android.synthetic.main.frag_available_offers.view.*


class GetOffersByDomainFragment : Fragment() , GetOffersByDomainAdapter.GetOffersListener{

    var candidateAddApplicationViewModel: CandidateAddApplicationViewModel? = null
    var offerList: ArrayList<OfferCardModelCandidate> = ArrayList()
    var errorString:String? = null
    var domainName:String?=null
    var listFetched:Boolean? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFetched = false
        savedInstanceState?.let {
            domainName = savedInstanceState.getString(DOMAIN_NAME)
            listFetched = savedInstanceState.getBoolean(LIST_FETCHED)
        }
        candidateAddApplicationViewModel = ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)
        candidateAddApplicationViewModel!!.responseGetOffersByDomain.observe(this, {
            offerList = it?.offers as ArrayList<OfferCardModelCandidate>
            recyclerViewOffers.adapter?.notifyDataSetChanged()
        })
        candidateAddApplicationViewModel!!.errorString.observe(this, {
            errorString = it
            Toast.makeText(this@GetOffersByDomainFragment.context, errorString, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frag_available_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Start fetching offer list
        if(savedInstanceState == null || !listFetched!!) {
            candidateAddApplicationViewModel!!.getOffersByDomain(domainName!!)
            listFetched = true
        } else if (listFetched!!){
            offerList = savedInstanceState.getParcelableArrayList<OfferCardModelCandidate>(OFFERS_LIST) as ArrayList<OfferCardModelCandidate>
        }

        view.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = GetOffersByDomainAdapter(offerList, this@GetOffersByDomainFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(OFFERS_LIST, offerList)
        outState.putBoolean(LIST_FETCHED, listFetched!!)
        outState.putString(DOMAIN_NAME, domainName)
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        //Populate new fragment with details of offer.
        val bundle = Bundle()
        bundle.putString(GetOfferDetailsCandidateFragment.OFFER_IDC, offerList.get(itemPosition).offer_id)
        parentFragmentManager.beginTransaction()
                .add(R.id.fragment_offers, GetOfferDetailsCandidateFragment::class.java, bundle, "GetOfferDetailsCandidateFragment")
                .addToBackStack(null)
                .commit()
    }

    companion object {
        val OFFERS_LIST = "OffersList"
        val LIST_FETCHED = "ListFetched"
        val DOMAIN_NAME = "DomainName"
    }
}
