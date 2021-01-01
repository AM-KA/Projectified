package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.myApplications.viewModel.CandidateViewModel
import kotlinx.android.synthetic.main.frag_apply_opportunity_view.view.*

class GetOfferDetailsCandidateFragment : Fragment() {

    private lateinit var textViewRequirements: TextView
    private lateinit var textViewSkills: TextView
    private lateinit var textViewExpectations: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewCollege: TextView
    private lateinit var textViewSemester: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var buttonApplyOpportunity: Button
    private lateinit var buttonCancelOpportunity: Button
    private lateinit var progressCircularLayout: RelativeLayout

    //View Models and Observable Objects
    private lateinit var candidateViewModel: CandidateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_apply_opportunity_view, container, false)
        progressCircularLayout = v.progress_circular_layout
        textViewExpectations = v.textViewExpectationData
        textViewSkills = v.textViewSkillsData
        textViewRequirements = v.textViewRequirementData
        textViewName = v.textViewNameData
        textViewCollege = v.textViewCollegeData
        textViewSemester = v.textViewSemesterData
        textViewCourse = v.textViewCourseData
        buttonApplyOpportunity = v.buttonApply
        buttonCancelOpportunity = v.buttonCancel

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        candidateViewModel = ViewModelProvider(requireActivity()).get(CandidateViewModel::class.java)

        candidateViewModel.responseGetOfferById().observe(viewLifecycleOwner, {
            populateViews(it)
        })

        candidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()

            }
        })


        buttonApplyOpportunity.setOnClickListener()
        {
            view.findNavController().navigate(R.id.view_details_to_apply_opportunity)
        }

        buttonCancelOpportunity.setOnClickListener()
        {
            view.findNavController().navigate(R.id.back_to_offers_by_domain)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
    }

    private fun populateViews(it:ResponseGetOfferById) {
        if (it.code == 200) {
            textViewExpectations.setText(it.offer.expectation)
            textViewSkills.setText(it.offer.skills)
            textViewRequirements.setText(it.offer.requirements)
            textViewName.setText(it.offer.recruiter_name)
            textViewCollege.setText(it.offer.recruiter_collegeName)
            textViewSemester.setText(it.offer.recruiter_semester)
            textViewCourse.setText(it.offer.recruiter_course)
            progressCircularLayout.visibility = View.INVISIBLE
        }
    }

    companion object {
        const val OFFER_IDC = "offerId"
    }
}