package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.ApplicantCardModel
import com.princeakash.projectified.recruiter.myOffers.model.BodyMarkAsSeen
import com.princeakash.projectified.recruiter.myOffers.model.BodyMarkAsSelected
import com.princeakash.projectified.recruiter.myOffers.model.MyOfferApplicantsAdapter
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicationFragment.Companion.APPLICATION_ID
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterExistingOffersViewModel
import kotlinx.android.synthetic.main.frag_candidates.*
import kotlinx.android.synthetic.main.frag_candidates.view.*

class MyOfferApplicantsFragment : Fragment(), MyOfferApplicantsAdapter.MyOfferApplicantListener {

    private lateinit var recruiterExistingOffersViewModel: RecruiterExistingOffersViewModel
    private var applicantList: ArrayList<ApplicantCardModel>? = ArrayList()
    private lateinit var progressCircularLayout: RelativeLayout

    private var offerId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_candidates, container, false)
        progressCircularLayout = v.progress_circular_layout

        recruiterExistingOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterExistingOffersViewModel::class.java)

        recruiterExistingOffersViewModel.responseGetOfferApplicants().observe(viewLifecycleOwner, {
            applicantList = it.applicants as ArrayList<ApplicantCardModel>
            recyclerViewApplicants.adapter = MyOfferApplicantsAdapter(applicantList, this)
            progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterExistingOffersViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_SHORT).show()
            }
        })

        recruiterExistingOffersViewModel.responseMarkAsSeen().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { iter ->
                progressCircularLayout.visibility = View.INVISIBLE
                //Search for id
                applicantList?.let {
                    for (listItem in it) {
                        if (listItem.application_id.equals(iter.application_id)) {
                            listItem.is_Seen = true
                            progressCircularLayout.visibility = View.VISIBLE
                            recruiterExistingOffersViewModel.refreshApplicants(it)
                            break
                        }
                    }
                    Toast.makeText(context, iter.message, LENGTH_SHORT).show()
                }
            }
        })

        recruiterExistingOffersViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { iter ->
                progressCircularLayout.visibility = View.INVISIBLE

                //Search for id
                applicantList?.let {
                    for (listItem in it) {
                        if (listItem.application_id.equals(iter.application_id)) {
                            listItem.is_Selected = true
                            progressCircularLayout.visibility = View.VISIBLE
                            recruiterExistingOffersViewModel.refreshApplicants(it)
                            break
                        }
                    }
                    Toast.makeText(context, iter.message, LENGTH_SHORT).show()
                }
            }
        })
        if (savedInstanceState == null) {
            offerId = requireArguments().getString(OFFER_ID)
            offerId?.let {
                progressCircularLayout.visibility = View.VISIBLE
                recruiterExistingOffersViewModel.getOfferApplicants(it)
            }
        } else {
            //progressCircularLayout.visibility = View.VISIBLE
            offerId = savedInstanceState.getString(OFFER_ID)
        }
        //(requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidates"
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerViewApplicants?.let {
            it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            it.adapter = MyOfferApplicantsAdapter(applicantList, this)
            it.setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidates"
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
                .addToBackStack("MyOfferApplication" + offerId)
                .commit()
    }

    override fun onSeenClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val application_id = applicantList!!.get(itemPosition).application_id
        val currentStatus = applicantList!!.get(itemPosition).is_Seen
        progressCircularLayout.visibility = View.VISIBLE
        recruiterExistingOffersViewModel.markSeen(application_id, BodyMarkAsSeen(!currentStatus))
    }

    override fun onSelectedClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val application_id = applicantList!!.get(itemPosition).application_id
        val currentStatus = applicantList!!.get(itemPosition).is_Selected
        progressCircularLayout.visibility = View.VISIBLE
        recruiterExistingOffersViewModel.markSelected(application_id, BodyMarkAsSelected(!currentStatus))
    }

    companion object {
        val OFFER_ID = "OfferId"
        private const val TAG = "MyOfferApplicantsFragme"
    }
}