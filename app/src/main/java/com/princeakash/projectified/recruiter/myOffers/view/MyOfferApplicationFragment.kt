package com.princeakash.projectified.recruiter.myOffers.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragCandidateDetailsRecruiterBinding
import com.princeakash.projectified.recruiter.myOffers.model.ResponseGetApplicationByIdRecruiter
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel

class MyOfferApplicationFragment : Fragment(R.layout.frag_candidate_details_recruiter) {

    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private var responseGetApplicationByIdRecruiter: ResponseGetApplicationByIdRecruiter? = null

    private lateinit var binding: FragCandidateDetailsRecruiterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragCandidateDetailsRecruiterBinding.bind(view)
        binding.textViewResume.setOnClickListener {
            try {
                val uri = Uri.parse(binding.textViewResume.text.toString())
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(context, "Sorry, the Resume URL is not valid.", LENGTH_LONG).show()
            }
        }
        binding.imageViewSeen.setOnClickListener { markSeen() }
        binding.imageViewSelected.setOnClickListener { markSelected() }

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetApplicationById().observe(viewLifecycleOwner, {
            responseGetApplicationByIdRecruiter = it
            populateViews(it)
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
                //Take action as per response
                if(response.code==200 || response.code == 403)
                    binding.imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })

        recruiterCandidateViewModel.responseMarkAsSelected().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                //Take action as per response
                if(response.code==200 || response.code == 403)
                    binding.imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
                Toast.makeText(context, response.message, LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Candidate Details"
    }

    private fun populateViews(it: ResponseGetApplicationByIdRecruiter) {
        binding.apply {
            if (it.code == 200) {
                textViewName.text = it.application?.applicant_name
                textViewCollege.text = it.application?.applicant_collegeName
                textViewCourse.text = it.application?.applicant_course
                textViewSemester.text = it.application?.applicant_semester
                textViewPhone.text = it.application?.applicant_phone
                textViewPreviousWork.text = it.application?.previousWork
                textViewResume.text = it.application?.resume
                it.application?.is_Seen?.let {
                    if (it)
                        imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
                it.application?.is_Selected?.let {
                    if (it)
                        imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
                }
            } else {
                Toast.makeText(context, it.message, LENGTH_LONG).show()
            }
            progressCircularLayout.visibility = View.INVISIBLE
        }
    }

    private fun markSeen() {
        responseGetApplicationByIdRecruiter?.let {
            var status = true
            if (it.application != null)
                status = it.application?.is_Seen!!
            binding.progressCircularLayout.visibility = View.VISIBLE
            recruiterCandidateViewModel.markSeen(null, !status)
        }
    }

    private fun markSelected() {
        responseGetApplicationByIdRecruiter?.let {
            var status = true
            if (it.application != null)
                status = it.application?.is_Selected!!
            binding.progressCircularLayout.visibility = View.VISIBLE
            recruiterCandidateViewModel.markSelected(null, !status)
        }
    }
}