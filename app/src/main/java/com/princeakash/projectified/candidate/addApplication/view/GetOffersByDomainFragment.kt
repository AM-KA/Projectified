package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.GetOffersByDomainAdapter
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import com.princeakash.projectified.recruiter.addOffer.view.AddOfferFragment
import kotlin.properties.Delegates

class GetOffersByDomainFragment : Fragment() , GetOffersByDomainAdapter.GetOffersListener{

    lateinit var candidateAddApplicationViewModel: CandidateAddApplicationViewModel
    var offerList: ArrayList<OfferCardModelCandidate> = ArrayList()
    var errorString:String? = null
    var domainName:String?=null
    var listFetched by Delegates.notNull<Boolean>()
    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFetched = false
        if(savedInstanceState!=null) {
            domainName = savedInstanceState.getString(DOMAIN_NAME)
            listFetched = savedInstanceState.getBoolean(LIST_FETCHED)
        }else{
            domainName = requireArguments().getString(DOMAIN_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_available_offers, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewOffers)
        /*recyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            Log.d(TAG, "onViewCreated: Setup adapter")
            this.adapter = GetOffersByDomainAdapter(offerList, this@GetOffersByDomainFragment)
        }*/
        //offerList.add(OfferCardModelCandidate("1", "Hello", "Namaste", "20-10-2010", "ISM"))
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), VERTICAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recyclerView.isNestedScrollingEnabled = true

        view.findViewById<Button>(R.id.buttonAddOffer).setOnClickListener {
            proceedToAddOffer()
        }

        candidateAddApplicationViewModel = ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)
        candidateAddApplicationViewModel.responseGetOffersByDomain.observe(viewLifecycleOwner, {
            offerList = it.offers as ArrayList<OfferCardModelCandidate>
            recyclerView.adapter = GetOffersByDomainAdapter(offerList, this)
        })

        candidateAddApplicationViewModel.errorString.observe(viewLifecycleOwner, {
            errorString = it
            Toast.makeText(this@GetOffersByDomainFragment.context, errorString, Toast.LENGTH_SHORT).show()
        })

        //Start fetching offer list
        if(savedInstanceState == null || !listFetched) {
            candidateAddApplicationViewModel.getOffersByDomain(domainName!!)
            listFetched = true
        } else if (listFetched){
            offerList = savedInstanceState.getParcelableArrayList<OfferCardModelCandidate>(OFFERS_LIST) as ArrayList<OfferCardModelCandidate>
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(OFFERS_LIST, offerList)
        outState.putBoolean(LIST_FETCHED, listFetched)
        outState.putString(DOMAIN_NAME, domainName)
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        //Populate new fragment with details of offer.
        val bundle = Bundle()
        bundle.putString(GetOfferDetailsCandidateFragment.OFFER_IDC, offerList.get(itemPosition).offer_id)
        Log.d(TAG, "onViewDetailsClick: " + offerList.get(itemPosition).offer_id)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_apply, GetOfferDetailsCandidateFragment::class.java, bundle, "GetOfferDetailsCandidateFragment")
                //.addToBackStack(null)
                .commit()
    }

    private fun proceedToAddOffer(){
        val bundle = Bundle()
        bundle.putString(DOMAIN_NAME, domainName)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_apply, AddOfferFragment::class.java, bundle, "GetOfferDetailsCandidateFragment")
                .commit()
    }
    companion object {
        val OFFERS_LIST = "OffersList"
        val LIST_FETCHED = "ListFetched"
        val DOMAIN_NAME = "DomainName"
        private const val TAG = "GetOffersByDomainFragme"
    }
}
