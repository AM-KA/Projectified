package com.princeakash.projectified.recruiter.myOffers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.model.ApplicantCardModel
import com.princeakash.projectified.recruiter.myOffers.model.MyOfferApplicantsAdapter
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel
import kotlinx.android.synthetic.main.frag_candidates.*
import kotlinx.android.synthetic.main.frag_candidates.view.*

class MyOfferApplicantsFragment : Fragment(), MyOfferApplicantsAdapter.MyOfferApplicantListener {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private var applicantList: ArrayList<ApplicantCardModel>? = ArrayList()
    private lateinit var progressCircularLayout: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.frag_candidates, container, false)
        progressCircularLayout = v.progress_circular_layout
        v.recyclerViewApplicants?.let {
            it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            it.adapter = MyOfferApplicantsAdapter(applicantList, this)
            it.setHasFixedSize(true)
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)

        recruiterCandidateViewModel.responseGetOfferApplicants().observe(viewLifecycleOwner, {
            applicantList = it.applicants as ArrayList<ApplicantCardModel>
            recyclerViewApplicants.adapter = MyOfferApplicantsAdapter(applicantList, this)
            progressCircularLayout.visibility = View.INVISIBLE
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSeen().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { iter ->
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, iter.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { iter ->
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, iter.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.safeToVisitCandidateDetails().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit ->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.candidates_to_candidate_details)
                }
            }
        })

        if(savedInstanceState==null){
            recruiterCandidateViewModel.nullifySafeToVisitCandidateList()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidates"
    }

    override fun onViewDetailsClick(itemPosition: Int) {
        val applicationID = applicantList!!.get(itemPosition).application_id
        recruiterCandidateViewModel.getApplicationById(applicationID)
    }

    override fun onSeenClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val application_id = applicantList!!.get(itemPosition).application_id
        val currentStatus = applicantList!!.get(itemPosition).is_Seen
        progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.markSeen(application_id, !currentStatus)
    }

    override fun onSelectedClick(itemPosition: Int) {
        //Get the ApplicantCardModel
        val application_id = applicantList!!.get(itemPosition).application_id
        val currentStatus = applicantList!!.get(itemPosition).is_Selected
        progressCircularLayout.visibility = View.VISIBLE
        recruiterCandidateViewModel.markSelected(application_id, !currentStatus)
    }

    companion object {
        private const val TAG = "MyOfferApplicantsFragme"
    }
}