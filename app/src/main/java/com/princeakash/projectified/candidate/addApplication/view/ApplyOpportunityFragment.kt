package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.ResponseAddApplication
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import kotlinx.android.synthetic.main.frag_apply_opportunity_self.view.*


class ApplyOpportunityFragment : Fragment() {

    //Views
    private var TextName:TextView? = null
    private var TextCollege: TextView? = null
    private var TextCourse: TextView? = null
    private var TextSemester: TextView? = null
    private var TextPhone: TextView? = null
    private var editTextPreviousWork:TextInputEditText?=null
    private var editTextResume:TextInputEditText?=null
    private var buttonApply:Button?=null
    private var buttonCancel:Button?=null
    private var offerId:String?=null

    //ViewModels
    private var candidateAddApplicationViewModel: CandidateAddApplicationViewModel? = null
    private var responseAddApplication: ResponseAddApplication? = null

    // Error String
    private var error: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState==null)
            offerId = requireArguments().getString(ApplyOpportunityFragment.OFFER_IDC);
        else
            offerId= savedInstanceState.getString(ApplyOpportunityFragment.OFFER_IDC);

        candidateAddApplicationViewModel!!.responseAddApplication.observe(this, {
            responseAddApplication = it
        })

        candidateAddApplicationViewModel!!.errorString.observe(this, {
            error = it
            Toast.makeText(context, error, LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_apply_opportunity_self, container, false)
        TextName = view.textViewName
        TextCollege= view.textViewCollege
        TextCourse= view.textViewCourse
        TextSemester = view.textViewSemester
        TextPhone=  view.textViewPhone
        editTextPreviousWork =view.editTextPreviousWork
        editTextResume= view.editTextResume
        buttonApply=view.buttonCancel
        buttonCancel= view.buttonCancel
        buttonApply!!.setOnClickListener {

            validateParameters()
        }
        buttonCancel!!.setOnClickListener{
            parentFragmentManager.beginTransaction()
                    .add(R.id.fragment_offers, HomeFragment::class.java, null, "HomeFragment")
                    .addToBackStack(null)
                    .commit()
        }
        return view
    }

    private fun validateParameters() {
        if(editTextPreviousWork!!.text.isNullOrEmpty()){
            editTextPreviousWork!!.error = "Enter previous Work."
            return
        }
        if(editTextResume!!.text.isNullOrEmpty()){
            editTextResume!!.error = "Enter Resume."
            return
        }

        val previousWork = editTextPreviousWork!!.text.toString()
        val resume = editTextResume!!.text.toString()
        offerId?.let { candidateAddApplicationViewModel!!.addApplication(resume,previousWork, it) }


        TODO("tOAST to say Applies Succesfully")
    }



    companion object{
        val OFFER_IDC = "offerId"
    }
}