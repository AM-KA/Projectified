package com.princeakash.projectified.candidate.addApplication.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.R
import com.princeakash.projectified.recruiter.myOffers.view.MyOfferHomeFragment

//Parent Fragment for MyOffers Feature
class AddApplicationsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragement_apply_application, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_apply,
                            HomeFragment::class.java,
                            null,
                            "HomeFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }
    }
}