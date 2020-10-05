package com.princeakash.projectified.recruiter.addOffer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
            domainName = requireArguments().getString(DOMAIN_NAME);
        else
            domainName = savedInstanceState.getString(DOMAIN_NAME);
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

        buttonEnlist!!.setOnClickListener {
            validateParameters()
        }
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
        recruiterAddOffersViewModel!!.addOffer(offerName, domainName!!, requirements, skills, expectation)
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

        recruiterAddOffersViewModel!!.responseAddOffer.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                responseAddOffer = it
            }
        })

        recruiterAddOffersViewModel!!.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let{
                error = it
                Toast.makeText(context, error, LENGTH_SHORT).show()
            }
        })

        loadRecruiterDetails()
    }

    private fun loadRecruiterDetails() {
        recruiterAddOffersViewModel.getLocalProfile()?.let {
            textViewName.setText(it.name)
            textViewCollege.setText(it.collegeName)
            textViewCourse.setText(it.course)
            textViewSemester.setText(it.semester)
        }
    }
}