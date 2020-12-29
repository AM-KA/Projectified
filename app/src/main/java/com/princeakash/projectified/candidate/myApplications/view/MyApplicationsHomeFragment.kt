package com.princeakash.projectified.candidate.myApplications.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
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

    lateinit var progressCircularLayout: RelativeLayout
    var candidateExistingApplicationViewModel: CandidateExistingApplicationViewModel? = null
    var applicationList: ArrayList<ApplicationCardModelCandidate> = ArrayList()


    //Determining whether re-fetching data is required or not
    var detailsViewed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_myapplication, container, false)
        progressCircularLayout = v.progress_circular_layout

        candidateExistingApplicationViewModel= ViewModelProvider(requireParentFragment()).get(CandidateExistingApplicationViewModel::class.java)
        candidateExistingApplicationViewModel!!.responseGetApplicationByCandidate().observe(viewLifecycleOwner, {
            applicationList = it?.applications as ArrayList<ApplicationCardModelCandidate>
            recyclerViewApplications.adapter = MyApplicationsAdapter(applicationList, this@MyApplicationsHomeFragment)
            progressCircularLayout.visibility = View.INVISIBLE
        })

        candidateExistingApplicationViewModel!!.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@MyApplicationsHomeFragment.context,it, LENGTH_LONG).show()
            }
        })

        //Start fetching applications list
        if(savedInstanceState == null || savedInstanceState.getBoolean(DETAILS_VIEWED)) {
            progressCircularLayout.visibility = View.VISIBLE
            candidateExistingApplicationViewModel!!.getApplicationsByCandidate()
            detailsViewed = false
        }
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "My Applications"
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerViewApplications.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = MyApplicationsAdapter(applicationList,this@MyApplicationsHomeFragment)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "My Applications"
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
                .replace(R.id.fragment_applications, MyApplicationDetailsFragment::class.java, bundle, "MyApplicationDetailsFragment")
                .addToBackStack("MyAppliDetails")
                .commit()
    }

    companion object {
        val APPLICATION_LIST = "ApplicationsList"
        val DETAILS_VIEWED = "DetailsViewed"
    }
}