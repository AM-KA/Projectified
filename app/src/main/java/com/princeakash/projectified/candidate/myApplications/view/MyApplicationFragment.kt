package com.princeakash.projectified.candidate.myApplications.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferHomeFragment

//Parent Fragment for MyOffers Feature
class MyApplicationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_applications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null){
            childFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_applications,
                            MyApplicationsHomeFragment::class.java,
                            null,
                            "MyApplicationFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }
    }
}