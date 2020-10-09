package com.princeakash.projectified.recruiter.addOffer.view

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.view.GetOffersByDomainFragment.Companion.DOMAIN_NAME
import com.princeakash.projectified.recruiter.addOffer.model.ResponseAddOffer
import com.princeakash.projectified.recruiter.addOffer.viewmodel.RecruiterAddOffersViewModel
import kotlinx.android.synthetic.main.frag_floatopportuninty.view.*
import kotlinx.android.synthetic.main.frag_myapplicationdetail.*

class AddOfferFragment : Fragment() {

    //Views
    private var editTextOfferName: TextInputEditText? = null
    private var editTextRequirements: TextInputEditText? = null
    private var editTextSkills: TextInputEditText? = null
    private var editTextExpectation: TextInputEditText? = null
    private lateinit var textViewName: TextView
    private lateinit var textViewCollege: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var textViewSemester: TextView
    private lateinit var progressCircularLayout: RelativeLayout

    private var buttonEnlist: Button? = null

    //ViewModels
    private lateinit var recruiterAddOffersViewModel: RecruiterAddOffersViewModel
    private lateinit var responseAddOffer: ResponseAddOffer
    private lateinit var error: String
    private var domainName: String? = null

    /*
        For obtaining DOMAIN_NAME passed through getOffersByDomain fragment
        and setting up ViewModel and LiveData
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState==null)
            domainName = requireArguments().getString(DOMAIN_NAME)
        else
            domainName = savedInstanceState.getString(DOMAIN_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_floatopportuninty, container, false)
        editTextOfferName = view.editTextOfferName
        editTextRequirements = view.editTextRequirement
        editTextSkills = view.editTextSkills
        editTextExpectation = view.editTextExpectation
        textViewName = view.textViewNameData
        textViewCollege = view.textViewCollegeData
        textViewCourse = view.textViewCourseData
        textViewSemester = view.textViewSemesterData
        buttonEnlist = view.EnlistButton
        progressCircularLayout = view.progress_circular_layout

        buttonEnlist!!.setOnClickListener {
            validateParameters()
        }
        (requireParentFragment().requireActivity() as AppCompatActivity).supportActionBar?.title = "Add Offer"
        return view
    }

    private fun validateParameters() {
        if(editTextOfferName!!.text.isNullOrEmpty()){
            editTextOfferName!!.error = "Enter offer name."
            return
        }
        if(editTextRequirements!!.text.isNullOrEmpty()){
            editTextRequirements!!.error = "Enter requirements."
            return
        }
        if(editTextSkills!!.text.isNullOrEmpty()){
            editTextSkills!!.error = "Enter skills."
            return
        }
        if(editTextExpectation!!.text.isNullOrEmpty()){
            editTextExpectation!!.error = "Enter expectation."
            return
        }
        val offerName = editTextOfferName!!.text.toString()
        val requirements = editTextRequirements!!.text.toString()
        val skills = editTextSkills!!.text.toString()
        val expectation = editTextExpectation!!.text.toString()
        AlertDialog.Builder(requireContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to float this offer?")
                .setPositiveButton("Yes") { dialog, which ->
                    progressCircularLayout.visibility = View.VISIBLE
                    recruiterAddOffersViewModel.addOffer(offerName, domainName!!, requirements, skills, expectation)
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }

    /*
        Saving Domain Name already obtained during first call of onCreate()
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DOMAIN_NAME, domainName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recruiterAddOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterAddOffersViewModel::class.java)

        recruiterAddOffersViewModel.responseAddOffer.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                progressCircularLayout.visibility = View.INVISIBLE
                responseAddOffer = it
                // TODO: Show Toast
            }
        })

        recruiterAddOffersViewModel.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                progressCircularLayout.visibility = View.INVISIBLE
                error = it
                Toast.makeText(context, error, LENGTH_SHORT).show()
            }
        })

        loadRecruiterDetails()
    }

    private fun loadRecruiterDetails() {
        progressCircularLayout.visibility = View.VISIBLE
        recruiterAddOffersViewModel.getLocalProfile()?.let {
            textViewName.setText(it.name)
            textViewCollege.setText(it.collegeName)
            textViewCourse.setText(it.course)
            textViewSemester.setText(it.semester)
        }
        progressCircularLayout.visibility = View.INVISIBLE
    }
}