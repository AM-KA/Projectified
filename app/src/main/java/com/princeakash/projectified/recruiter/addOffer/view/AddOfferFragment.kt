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
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.addOffer.model.ResponseAddOffer
import com.princeakash.projectified.recruiter.addOffer.viewmodel.RecruiterAddOffersViewModel
import kotlinx.android.synthetic.main.frag_floatopportuninty.view.*

class AddOfferFragment : Fragment() {

    //Views
    private var editTextRequirements: TextView? = null
    private var editTextSkills: TextView? = null
    private var editTextExpectation: TextView? = null
    private var buttonEnlist: Button? = null

    //ViewModels
    private var recruiterAddOffersViewModel: RecruiterAddOffersViewModel? = null
    private var responseAddOffer: ResponseAddOffer? = null
    private var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recruiterAddOffersViewModel = ViewModelProvider(requireParentFragment()).get(RecruiterAddOffersViewModel::class.java)

        recruiterAddOffersViewModel!!.responseAddOffer.observe(this, {
            responseAddOffer = it
        })

        recruiterAddOffersViewModel!!.errorString.observe(this, {
            error = it
            Toast.makeText(context, error, LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_floatopportuninty, container, false)
        editTextRequirements = view.editTextRequirement
        editTextSkills = view.editTextSkills
        editTextExpectation = view.editTextExpectation
        buttonEnlist = view.EnlistButton

        buttonEnlist!!.setOnClickListener {
            validateParameters()
        }
        return view
    }

    private fun validateParameters() {
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
        val requirements = editTextRequirements!!.text.toString()
        val skills = editTextSkills!!.text.toString()
        val expectation = editTextSkills!!.text.toString()
        recruiterAddOffersViewModel!!.addOffer(requirements, skills, expectation)
    }
}