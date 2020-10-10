package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
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

    //ViewModel
    var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel? = null

    //Offer List for RecyclerView
    var offerList: ArrayList<OfferCardModelRecruiter> = ArrayList()

    //Determining whether re-fetching data is required or not
    var detailsViewed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "My Offers"

        return inflater.inflate(R.layout.frag_my_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)
        recruiterExistingOffersViewModel!!.responseGetOffersByRecruiter().observe(viewLifecycleOwner, {
            offerList = it.offers as ArrayList<OfferCardModelRecruiter>
            view.progress_circular_layout.visibility = View.INVISIBLE
            view.recyclerViewOffers.adapter = MyOffersAdapter(offerList, this@MyOfferHomeFragment)
        })
        recruiterExistingOffersViewModel!!.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                view.progress_circular_layout.visibility = View.INVISIBLE
                Toast.makeText(this@MyOfferHomeFragment.context, it, LENGTH_SHORT).show()
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
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DETAILS_VIEWED, detailsViewed)
    }

    override fun onViewDetailsClick(itemPosition: Int) {

        //Set detailsViewed to true so that on coming back, we can re-load all data
        detailsViewed = true

        //Populate new fragment with details of offer.
        val bundle = Bundle()
        val offID = offerList.get(itemPosition).offer_id
        bundle.putString(OFFER_ID, offID)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, MyOfferDetailsFragment::class.java, bundle, "MyOfferDetailsFragment")
                .addToBackStack("MyOfferDetails"+offID)
                .commit()
    }

    companion object {
        val DETAILS_VIEWED = "DetailsViewed"
    }
}