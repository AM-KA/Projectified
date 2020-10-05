package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.OfferCardModelRecruiter
import com.princeakash.projectified.recruiter.myOffers.model.MyOffersAdapter
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferDetailsFragment.Companion.OFFER_ID
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_my_offers.*
import kotlinx.android.synthetic.main.frag_my_offers.view.*


class MyOfferHomeFragment() : Fragment(), MyOffersAdapter.MyOffersListener {

    var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel? = null
    var offerList: ArrayList<OfferCardModelRecruiter> = ArrayList()
    var errorString:String? = null

    //Determining whether re-fetching data is required or not
    var detailsViewed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        /*savedInstanceState?.let {
            offerList = savedInstanceState.getSerializable(OFFERS_LIST) as ArrayList<OfferCardModelRecruiter>
        }*/
        return inflater.inflate(R.layout.frag_my_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)
        recruiterExistingOffersViewModel!!.responseGetOffersByRecruiter.observe(viewLifecycleOwner, {
            offerList = it?.offers as ArrayList<OfferCardModelRecruiter>
            view.recyclerViewOffers.adapter = MyOffersAdapter(offerList, this@MyOfferHomeFragment)
        })
        recruiterExistingOffersViewModel!!.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                errorString = it
                Toast.makeText(this@MyOfferHomeFragment.context, errorString, LENGTH_SHORT).show()
            }
        })

        //Start fetching offer list
        if(savedInstanceState == null || savedInstanceState.getBoolean(DETAILS_VIEWED)) {
            recruiterExistingOffersViewModel!!.getOffersByRecruiter()
            detailsViewed = false
        }

        view.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = MyOffersAdapter(offerList, this@MyOfferHomeFragment)
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
        bundle.putString(OFFER_ID, offerList.get(itemPosition).offer_id)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, MyOfferDetailsFragment::class.java, bundle, "MyOfferDetailsFragment")
                .addToBackStack(null)
                .commit()
    }

    companion object {
        val OFFERS_LIST = "OffersList"
        val DETAILS_VIEWED = "DetailsViewed"
    }
}