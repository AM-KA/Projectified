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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.model.ResponseAddApplication
import com.princeakash.projectified.candidate.addApplication.viewModel.CandidateAddApplicationViewModel
import com.princeakash.projectified.user.BodySignUp
import com.princeakash.projectified.user.view.SignUp
import kotlinx.android.synthetic.main.frag_apply_opportunity_self.view.*


class ApplyOpportunityFragment : Fragment() {

    //Views
    private lateinit var textName:TextView
    private lateinit var textCollege: TextView
    private lateinit var textCourse: TextView
    private lateinit var textSemester: TextView
    private lateinit var editTextPreviousWork:TextInputEditText
    private lateinit var editTextResume:TextInputEditText
    private lateinit var buttonApply:Button
    private lateinit var buttonCancel:Button
    private lateinit var offerId:String

    //ViewModels
    private lateinit var candidateAddApplicationViewModel: CandidateAddApplicationViewModel
    private var responseAddApplication: ResponseAddApplication? = null

    // Error String
    private var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        offerId = if(savedInstanceState==null)
            requireArguments().getString(OFFER_IDC)!!
        else
            savedInstanceState.getString(OFFER_IDC)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        candidateAddApplicationViewModel= ViewModelProvider(requireParentFragment()).get(CandidateAddApplicationViewModel::class.java)
        candidateAddApplicationViewModel.responseAddApplication.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                responseAddApplication = it
                Toast.makeText(context, it.message, LENGTH_SHORT).show()
            }
        })

        candidateAddApplicationViewModel.errorString.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                error = it
                Toast.makeText(context, error, LENGTH_SHORT).show()
            }
        })

        val view = inflater.inflate(R.layout.frag_apply_opportunity_self, container, false)

        textName = view.textViewName
        textCollege= view.textViewCollege
        textCourse= view.textViewCourse
        textSemester = view.textViewSemester
        editTextPreviousWork =view.editTextPreviousWork
        editTextResume= view.editTextResume
        buttonApply=view.buttonSubmit
        buttonCancel= view.buttonCancel
        buttonApply.setOnClickListener {

            validateParameters()
        }
        buttonCancel.setOnClickListener{
            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_apply, HomeFragment::class.java, null, "HomeFragment")
                    .addToBackStack(null)
                    .commit()
        }

        loadLocalProfile()
        return view
    }

    private fun validateParameters() {
        if(editTextPreviousWork.text.isNullOrEmpty()){
            editTextPreviousWork.error = "Enter previous Work."
            return
        }
        if(editTextResume.text.isNullOrEmpty()){
            editTextResume.error = "Enter Resume."
            return
        }

        val previousWork = editTextPreviousWork.text.toString()
        val resume = editTextResume.text.toString()
        candidateAddApplicationViewModel.addApplication(resume,previousWork, offerId)
    }

    private fun loadLocalProfile(){
        val profileModel = candidateAddApplicationViewModel.getLocalProfile()!!
        textName.setText(profileModel.name)
        textCollege.setText(profileModel.collegeName)
        textCourse.setText(profileModel.course)
        textSemester.setText(profileModel.semester)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OFFER_IDC, offerId)
    }
    companion object{
        val OFFER_IDC = "offerId"
    }
}