package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.util.Log
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
import com.princeakash.projectified.recruiter.addOffer.model.*
import com.princeakash.projectified.recruiter.myOffers.model.*
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicationFragment.Companion.APPLICATION_ID
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_candidates.*
import kotlinx.android.synthetic.main.frag_candidates.view.*
import java.util.*
import kotlin.collections.ArrayList

class MyOfferApplicantsFragment : Fragment(), MyOfferApplicantsAdapter.MyOfferApplicantListener{

    private var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel? = null
    private var error:String? = null
    private var applicantList: ArrayList<ApplicantCardModel>? = ArrayList()
    private var responseMarkAsSeen: ResponseMarkAsSeen? = null
    private var responseMarkAsSelected: ResponseMarkAsSelected? = null
    private var responseGetOfferApplicants: ResponseGetOfferApplicants? = null

    private var offerId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)

        recruiterExistingOffersViewModel!!.responseGetOfferApplicants.observe(viewLifecycleOwner, {
            responseGetOfferApplicants = it
            applicantList = it.applicants as ArrayList<ApplicantCardModel>
            recyclerViewApplicants.adapter = MyOfferApplicantsAdapter(applicantList, this)
        })

        recruiterExistingOffersViewModel!!.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                error = it
                Toast.makeText(context, error, LENGTH_SHORT).show()
            }
        })

        recruiterExistingOffersViewModel!!.responseMarkAsSeen.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { iter ->
                responseMarkAsSeen = iter

                Log.d(TAG, "onCreateView: Entered seen")
                //Take action as per response

                Log.d(TAG, "onCreateView: "+iter.application_id)
                //Search for id
                applicantList?.let {
                    Log.d(TAG, "onCreateView: List not null")
                    for (listItem in it) {
                        if (listItem.application_id.equals(iter.application_id)) {
                            listItem.is_Seen = true
                            Log.d(TAG, "onCreateView: Mark as seen is being changed")
                            responseGetOfferApplicants?.applicants = it
                            recruiterExistingOffersViewModel!!.responseGetOfferApplicants.postValue(responseGetOfferApplicants)
                            break
                        }
                    }
                }
            }
        })

        recruiterExistingOffersViewModel!!.responseMarkAsSelected.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { iter ->
                responseMarkAsSelected = iter

                Log.d(TAG, "onCreateView: Entered selected")
                //Take action as per response

                Log.d(TAG, "onCreateView: "+iter.application_id)
                //Search for id
                applicantList?.let {
                    Log.d(TAG, "onCreateView: List not null")
                    for (listItem in it) {
                        if (listItem.application_id.equals(iter.application_id)) {
                            listItem.is_Selected = true
                            Log.d(TAG, "onCreateView: Mark as selected is being changed")
                            responseGetOfferApplicants?.applicants = it
                            recruiterExistingOffersViewModel!!.responseGetOfferApplicants.postValue(responseGetOfferApplicants)
                            break
                        }
                    }
                }
            }
        })
        if(savedInstanceState == null){
            offerId = requireArguments().getString(OFFER_ID)
            offerId?.let{
                recruiterExistingOffersViewModel!!.getOfferApplicants(it)
            }
        } else {
            offerId = savedInstanceState.getString(OFFER_ID)
        }
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidates"
        return inflater.inflate(R.layout.frag_candidates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerViewApplicants?.let {
            it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            it.adapter = MyOfferApplicantsAdapter(applicantList, this)
            it.setHasFixedSize(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OFFER_ID, offerId)
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        val applicationID = applicantList!!.get(itemPosition).application_id
        val bundle = Bundle()
        bundle.putString(APPLICATION_ID, applicationID)
        parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_offers, MyOfferApplicationFragment::class.java, bundle, "MyOfferApplicationFragment")
                .addToBackStack("MyOfferApplication"+offerId)
                .commit()
    }

    override fun onSeenClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val application_id = applicantList!!.get(itemPosition).application_id
        val currentStatus = applicantList!!.get(itemPosition).is_Seen
        recruiterExistingOffersViewModel!!.markSeen(application_id, BodyMarkAsSeen(!currentStatus))
    }

    override fun onSelectedClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val application_id = applicantList!!.get(itemPosition).application_id
        val currentStatus = applicantList!!.get(itemPosition).is_Selected
        recruiterExistingOffersViewModel!!.markSelected(application_id, BodyMarkAsSelected(!currentStatus))
    }

    companion object {
        val OFFER_ID = "OfferId"
        private const val TAG = "MyOfferApplicantsFragme"
    }
}