package com.princeakash.projectified.candidate.myApplications.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.princeakash.projectified.R

//Parent Fragment for MyOffers Feature
class MyApplicationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_applications, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //childFragmentManager.beginTransaction()
        //       .replace()
    }
}