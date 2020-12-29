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
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.ResponseGetOfferById
import com.princeakash.projectified.candidate.addApplication.view.ApplyOpportunityFragment.Companion.OFFER_IDC
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import kotlinx.android.synthetic.main.frag_apply_opportunity_view.view.*
import kotlinx.android.synthetic.main.frag_apply_opportunity_view.view.progress_circular_layout
import kotlinx.android.synthetic.main.frag_available_offers.view.*

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
    private lateinit var candidateAddApplicationsViewModel: CandidateAddApplicationViewModel

    private var offerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            offerId = requireArguments().getString(OFFER_IDC)
        } else {
            offerId = savedInstanceState.getString(OFFER_IDC)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.frag_apply_opportunity_view, container, false)
        progressCircularLayout = v.progress_circular_layout
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"

        textViewExpectations = v.textViewExpectationData
        textViewSkills = v.textViewSkillsData
        textViewRequirements = v.textViewRequirementData
        textViewName = v.textViewNameData
        textViewCollege = v.textViewCollegeData
        textViewSemester = v.textViewSemesterData
        textViewCourse = v.textViewCourseData
        buttonApplyOpportunity = v.buttonApply
        buttonCancelOpportunity = v.buttonCancel


        buttonApplyOpportunity.setOnClickListener()
        {
            val bundle = Bundle()
            bundle.putString(OFFER_IDC, offerId)
            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_apply, ApplyOpportunityFragment::class.java, bundle, "ApplyOpportunityFragment")
                    .addToBackStack("ApplyOpportunity")
                    .commit()
        }

        buttonCancelOpportunity.setOnClickListener()
        {
            /*parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_apply, HomeFragment::class.java, null, "Home Fragment")
                    .addToBackStack("HomeFragment")
                    .commit()*/
            parentFragmentManager.popBackStackImmediate()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        candidateAddApplicationsViewModel = ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)

        candidateAddApplicationsViewModel.responseGetOfferById().observe(viewLifecycleOwner, {
            populateViews(it)
        })

        candidateAddApplicationsViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        if (savedInstanceState == null) {
            progressCircularLayout.visibility = View.VISIBLE
            candidateAddApplicationsViewModel.getoffersById(offerId!!)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Offer Details"
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OFFER_IDC, offerId)
    }

    companion object {
        const val OFFER_IDC = "offerId"
    }
}