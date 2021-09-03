package com.princeakash.projectified.candidate.myApplications.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.model.*
import com.princeakash.projectified.databinding.FragMyApplicationDetailsBinding
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel
import java.net.URL

class MyApplicationDetailsFragment : Fragment() {

    //ViewModels and Observable Objects
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel
    private lateinit var binding: FragMyApplicationDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.frag_my_application_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragMyApplicationDetailsBinding.bind(view)

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        subscribeToObservers()

        binding.buttonUpdateDetails.setOnClickListener { updateOffer() }
        binding.buttonDeleteApplication.setOnClickListener { deleteApplication() }

        if(savedInstanceState==null)
            recruiterCandidateViewModel.nullifySafeToVisitApplicationDetails()

    }

    private fun subscribeToObservers() {
        recruiterCandidateViewModel.responseGetApplicationDetailByIdCandidate().observe(viewLifecycleOwner, {
            binding.progressCircularLayout.visibility = View.INVISIBLE
            populateViews(it)
        })


        recruiterCandidateViewModel.responseUpdateApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }
        })


        recruiterCandidateViewModel.responseDeleteApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { response ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }
        })

        recruiterCandidateViewModel.safeToVisitApplicationList().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {safeToVisit->
                if(safeToVisit){
                    findNavController().navigate(R.id.details_to_my_applications_home)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { message ->
                binding.progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Application Details"
    }

    private fun updateOffer() {
        binding.apply {
            if (editTextPreviousWork.text.toString().isEmpty()){
                editTextPreviousWork.error = "Please Provide Drive Link of Your Previous Work."
                return
            }
            if (editTextResume.text.toString().isEmpty()){
                editTextResume.error = "Please Provide Drive Link of the Resume."
                return
            }

            val previousWork = editTextPreviousWork.text.toString()
            val resume = editTextResume.text.toString()

            try{
                URL(resume).toURI()
            }catch (e: Exception){
                editTextResume.error = "Enter a valid URL."
                return
            }

            progressCircularLayout.visibility = View.VISIBLE
            recruiterCandidateViewModel.updateApplication(resume, previousWork)
        }
    }

    private fun populateViews(response:ResponseGetApplicationDetailByIdCandidate) {
        binding.apply {
            if (response.code == 200) {
                textViewRequirement.text = response.application?.requirements
                textViewSkills.text = response.application?.skills
                textViewExpectation.text = response.application?.expectation
                textViewName.text = response.application?.recruiter_name
                textViewCollege.text = response.application?.recruiter_collegeName
                textViewSemester.text = response.application?.recruiter_semester
                textViewCourse.text = response.application?.recruiter_course
                editTextPreviousWork.setText(response.application?.previousWork)
                editTextResume.setText(response.application?.resume)

                if (response.application?.is_Seen!!)
                    imageViewSeen.setImageResource(R.drawable.ic_baseline_favorite_24)
                else
                    imageViewSeen.setImageResource((R.drawable.ic_outline_favorite_border_24))

                if (response.application?.is_Selected!!)
                    imageViewSelected.setImageResource(R.drawable.ic_baseline_done_24)
                else
                    imageViewSelected.setImageResource((R.drawable.ic_baseline_done_outline_24))
                progressCircularLayout.visibility = View.INVISIBLE
            } else {
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }
            progressCircularLayout.visibility = View.INVISIBLE
        }
    }

    private fun deleteApplication() {
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this application? You cannot undo this action later.")
                .setPositiveButton("Yes") { _,_ ->
                    binding.progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.deleteApplication()
                }
                .setNegativeButton("No") { _,_ -> }
                .create().show()
    }
}