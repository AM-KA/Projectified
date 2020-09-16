package com.princeakash.projectified.candidate.myApplications.view

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
import com.princeakash.projectified.candidate.myApplications.model.ApplicationCardModelCandidate
import com.princeakash.projectified.candidate.myApplications.model.MyApplicationsAdapter
import com.princeakash.projectified.candidate.myApplications.view.MyApplicationDetailsFragment.Companion.APPLICATION_IDC
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateExistingApplicationViewModel
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferApplicationFragment.Companion.APPLICATION_ID
import kotlinx.android.synthetic.main.frag_myapplication.*
import kotlinx.android.synthetic.main.frag_myapplication.view.*


class MyApplicationsHomeFragment() : Fragment(), MyApplicationsAdapter.MyApplicationsListener {

    var candidateExistingApplicationViewModel: CandidateExistingApplicationViewModel? = null
    var applicationList: ArrayList<ApplicationCardModelCandidate> = ArrayList()
    var errorString:String? = null

    //Determining whether re-fetching data is required or not
    var detailsViewed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       candidateExistingApplicationViewModel= ViewModelProvider(requireParentFragment()).get(CandidateExistingApplicationViewModel::class.java)
        candidateExistingApplicationViewModel!!.responseGetApplicationByCandidate.observe(this, {
            applicationList = it?.Application as ArrayList<ApplicationCardModelCandidate>
            recyclerViewApplications.adapter?.notifyDataSetChanged()
        })
       candidateExistingApplicationViewModel!!.errorString.observe(this, {
            errorString = it
            Toast.makeText(this@MyApplicationsHomeFragment.context, errorString, LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_myapplication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Start fetching applications list
        if(savedInstanceState == null || savedInstanceState.getBoolean(DETAILS_VIEWED)) {
            candidateExistingApplicationViewModel!!.getApplicationsByCandidate()
            detailsViewed = false
        }

        view.recyclerViewApplications.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = MyApplicationsAdapter(applicationList,this@MyApplicationsHomeFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DETAILS_VIEWED, detailsViewed)
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        //Set detailsViewed to true so that on coming back, we can re-load all data
        detailsViewed = true
        //Populate new fragment with details of Applications
        val bundle = Bundle()
        bundle.putString(APPLICATION_IDC, applicationList.get(itemPosition).application_id)
        parentFragmentManager.beginTransaction()
                .add(R.id.fragment_applications, MyApplicationDetailsFragment::class.java, bundle, "MyApplicationDetailsFragment")
                .addToBackStack(null)
                .commit()
    }

    companion object {
        val APPLICATION_LIST = "ApplicationsList"
        val DETAILS_VIEWED = "DetailsViewed"
    }
}