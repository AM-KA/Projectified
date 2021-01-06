package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.viewmodel.RecruiterCandidateViewModel
import kotlinx.android.synthetic.main.frag_apply_opportunity_self.view.*
import java.net.URL


class ApplyOpportunityFragment : Fragment() {

    //Views
    private lateinit var textName: TextView
    private lateinit var textCollege: TextView
    private lateinit var textCourse: TextView
    private lateinit var textSemester: TextView
    private lateinit var editTextPreviousWork: TextInputEditText
    private lateinit var editTextResume: TextInputEditText
    private lateinit var buttonApply: Button
    private lateinit var buttonCancel: Button
    private lateinit var progressCircularLayout: RelativeLayout

    //ViewModels
    private lateinit var recruiterCandidateViewModel: RecruiterCandidateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.frag_apply_opportunity_self, container, false)

        textName = view.textViewName
        textCollege = view.textViewCollege
        textCourse = view.textViewCourse
        textSemester = view.textViewSemester
        editTextPreviousWork = view.editTextPreviousWork
        editTextResume = view.editTextResume
        buttonApply = view.buttonSubmit
        buttonCancel = view.buttonCancel
        progressCircularLayout = view.progress_circular_layout
        buttonApply.setOnClickListener {
            validateParameters()
        }
        buttonCancel.setOnClickListener {
            view.findNavController().navigate(R.id.back_to_offers_by_domain)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recruiterCandidateViewModel = ViewModelProvider(requireActivity()).get(RecruiterCandidateViewModel::class.java)
        recruiterCandidateViewModel.responseAddApplication().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, LENGTH_LONG).show()
                if(it.code.equals("200")){
                    view.findNavController().navigate(R.id.back_to_offers_by_domain)
                }
            }
        })

        recruiterCandidateViewModel.safeToVisitDomainOffers().observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let{safeToVisit->
                if(safeToVisit){
                    view.findNavController().navigate(R.id.back_to_offers_by_domain)
                }
            }
        })

        recruiterCandidateViewModel.errorString().observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                progressCircularLayout.visibility = View.INVISIBLE
                Toast.makeText(context, it, LENGTH_LONG).show()
            }
        })
        loadLocalProfile()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Apply Opportunity"
    }

    private fun validateParameters() {
        if (editTextPreviousWork.text.isNullOrEmpty()) {
            editTextPreviousWork.error = "Enter previous Work."
            return
        }
        if (editTextResume.text.isNullOrEmpty()) {
            editTextResume.error = "Enter resume link."
            return
        }

        val previousWork = editTextPreviousWork.text.toString()
        val resume = editTextResume.text.toString()

        try{
            val uri = URL(resume).toURI()
        }catch (e: Exception){
            editTextResume.error = "Enter a valid URL."
            return
        }

        AlertDialog.Builder(requireContext())
                .setTitle("Confirm Application")
                .setMessage("Are you sure you want to apply for this offer with the filled details?")
                .setPositiveButton("Yes") { dialog, which ->
                    progressCircularLayout.visibility = View.VISIBLE
                    recruiterCandidateViewModel.addApplication(resume, previousWork)
                }
                .setNegativeButton("No") { dialog, which -> }
                .create().show()
    }

    private fun loadLocalProfile() {
        progressCircularLayout.visibility = View.VISIBLE
        val profileModel = recruiterCandidateViewModel.getLocalProfile()!!
        textName.setText(profileModel.name)
        textCollege.setText(profileModel.collegeName)
        textCourse.setText(profileModel.course)
        textSemester.setText(profileModel.semester)
        progressCircularLayout.visibility = View.INVISIBLE
    }
}