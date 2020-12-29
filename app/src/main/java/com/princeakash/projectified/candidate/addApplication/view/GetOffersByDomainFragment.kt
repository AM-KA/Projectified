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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.GetOffersByDomainAdapter
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import com.princeakash.projectified.candidate.myApplications.model.OfferCardModelCandidate
import com.princeakash.projectified.recruiter.addOffer.view.AddOfferFragment
import kotlinx.android.synthetic.main.frag_available_offers.view.*
import kotlin.properties.Delegates

class GetOffersByDomainFragment : Fragment() , GetOffersByDomainAdapter.GetOffersListener{

    private lateinit var candidateAddApplicationViewModel: CandidateAddApplicationViewModel
    private var offerList: ArrayList<OfferCardModelCandidate> = ArrayList()
    private  var domainName:String?=null
    //private  var listFetched by Delegates.notNull<Boolean>()
    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState!=null) {
            domainName = savedInstanceState.getString(DOMAIN_NAME)
        }else{
            domainName = requireArguments().getString(DOMAIN_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_available_offers, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewOffers)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), VERTICAL, false)
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Offers"
        view.progress_circular_layout.visibility = View.VISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(parentFragmentManager.findFragmentByTag("GetOff")!=null)
            Log.d(TAG, "onViewCreated: Found")
        view.findViewById<FloatingActionButton>(R.id.buttonAddOffer).setOnClickListener {
            proceedToAddOffer()
        }

        candidateAddApplicationViewModel = ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)
        candidateAddApplicationViewModel.responseGetOffersByDomain().observe(viewLifecycleOwner, {
            offerList = it.offers as ArrayList<OfferCardModelCandidate>
            recyclerView.adapter = GetOffersByDomainAdapter(offerList, this)
            view.progress_circular_layout.visibility = View.INVISIBLE
        })

        candidateAddApplicationViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                view.progress_circular_layout.visibility = View.INVISIBLE
                Toast.makeText(this@GetOffersByDomainFragment.context, it, Toast.LENGTH_LONG).show()
            }
        })

        //Start fetching offer list
        if(savedInstanceState == null) {
            view.progress_circular_layout.visibility = View.VISIBLE
            candidateAddApplicationViewModel.getOffersByDomain(domainName!!)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Offers"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(OFFERS_LIST, offerList)
        outState.putString(DOMAIN_NAME, domainName)
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        //Populate new fragment with details of offer.
        val bundle = Bundle()
        bundle.putString(GetOfferDetailsCandidateFragment.OFFER_IDC, offerList.get(itemPosition).offer_id)
        Log.d(TAG, "onViewDetailsClick: " + offerList.get(itemPosition).offer_id)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_apply, GetOfferDetailsCandidateFragment::class.java, bundle, "GetOfferDetailsCandidateFragment")
                .addToBackStack("GetOfferDetailsCandi")
                .commit()
    }

    private fun proceedToAddOffer(){
        val bundle = Bundle()
        bundle.putString(DOMAIN_NAME, domainName)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_apply, AddOfferFragment::class.java, bundle, "AddOfferFragment")
                .addToBackStack("AddOffer")
                .commit()
    }
    companion object {
        val OFFERS_LIST = "OffersList"
        val DOMAIN_NAME = "DomainName"
        private const val TAG = "GetOffersByDomainFragme"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}
