package com.princeakash.projectified.candidate.myApplications.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.ApplicationCardModelCandidate
import com.princeakash.projectified.candidate.myApplications.model.MyApplicationsAdapter
import com.princeakash.projectified.databinding.FragMyApplicationBinding
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel

class MyApplicationsHomeFragment: Fragment(R.layout.frag_my_application), MyApplicationsAdapter.MyApplicationsListener {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private var applicationList: ArrayList<ApplicationCardModelCandidate> = ArrayList()
    private lateinit var binding: FragMyApplicationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyApplicationBinding.bind(view)
        binding.recyclerViewApplications.apply{
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null)
            recruiterCandidateViewModel.nullifySafeToVisitApplicationsList()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetApplicationByCandidate().observe(viewLifecycleOwner, {
            applicationList = it?.applications as ArrayList<ApplicationCardModelCandidate>
            binding.recyclerViewApplications.adapter = MyApplicationsAdapter(applicationList, this@MyApplicationsHomeFragment)
            binding.progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.safeToVisitApplicationDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.home_to_application_details)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{ message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(this@MyApplicationsHomeFragment.context, message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Apps"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        binding.progressCircularLayout.visibility = View.VISIBLE
        val applicationID = applicationList[itemPosition].application_id
        recruiterCandidateViewModel.getApplicationDetailByIdCandidate(applicationID)
    }
}