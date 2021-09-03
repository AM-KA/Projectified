package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragCandidatesBinding
import com.princeakash.projectified.recruiter.myOffers.model.ApplicantCardModel
import com.princeakash.projectified.recruiter.myOffers.model.MyOfferApplicantsAdapter
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel

class MyOfferApplicantsFragment : Fragment(R.layout.frag_candidates), MyOfferApplicantsAdapter.MyOfferApplicantListener {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragCandidatesBinding

    private var applicantList: ArrayList<ApplicantCardModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragCandidatesBinding.bind(view)
        binding.recyclerViewApplicants.apply{
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = MyOfferApplicantsAdapter(applicantList, this@MyOfferApplicantsFragment)
            setHasFixedSize(true)
        }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        if(savedInstanceState==null)
            recruiterCandidateViewModel.nullifySafeToVisitCandidateList()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetOfferApplicants().observe(viewLifecycleOwner, { response ->
            applicantList = response.applicants as ArrayList<ApplicantCardModel>
            binding.recyclerViewApplicants.adapter = MyOfferApplicantsAdapter(applicantList, this)
            binding.progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSeen().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.safeToVisitCandidateDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit ->
                if(safeToVisit){
                    findNavController().navigate(R.id.candidates_to_candidate_details)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidates"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        val applicationID = applicantList[itemPosition].application_id
        recruiterCandidateViewModel.getApplicationById(applicationID)
    }

    override fun onSeenClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val applicationId = applicantList[itemPosition].application_id
        val currentStatus = applicantList[itemPosition].is_Seen
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.markSeen(applicationId, !currentStatus)
    }

    override fun onSelectedClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val applicationId = applicantList[itemPosition].application_id
        val currentStatus = applicantList[itemPosition].is_Selected
        binding.progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.markSelected(applicationId, !currentStatus)
    }
}