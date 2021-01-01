package com.princeakash.projectified.candidate.myApplications.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.ApplicationCardModelCandidate
import com.princeakash.projectified.candidate.myApplications.model.MyApplicationsAdapter
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateViewModel
import kotlinx.android.synthetic.main.frag_myapplication.*
import kotlinx.android.synthetic.main.frag_myapplication.view.*


class MyApplicationsHomeFragment() : Fragment(), MyApplicationsAdapter.MyApplicationsListener {

    lateinit var progressCircularLayout: RelativeLayout
    private lateinit var candidateViewModel: CandidateViewModel
    var applicationList: ArrayList<ApplicationCardModelCandidate> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_myapplication, container, false)
        progressCircularLayout = v.progress_circular_layout
        v.recyclerViewApplications.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            //adapter = MyApplicationsAdapter(applicationList,this@MyApplicationsHomeFragment)
            setHasFixedSize(true)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        candidateViewModel= ViewModelProvider(requireActivity()).get(CandidateViewModel::class.java)

        candidateViewModel.responseGetApplicationByCandidate().observe(viewLifecycleOwner, {
            applicationList = it?.applications as ArrayList<ApplicationCardModelCandidate>
            recyclerViewApplications.adapter = MyApplicationsAdapter(applicationList, this@MyApplicationsHomeFragment)
            progressCircularLayout.visibility = View.INVISIBLE
        })

        candidateViewModel.safeToVisitApplicationDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.home_to_application_details)
                }
            }
        })

        candidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@MyApplicationsHomeFragment.context,it, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Apps"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        progressCircularLayout.visibility = View.VISIBLE
        val applicationID = applicationList.get(itemPosition).application_id
        candidateViewModel.getApplicationDetailByIdCandidate(applicationID)
    }
}