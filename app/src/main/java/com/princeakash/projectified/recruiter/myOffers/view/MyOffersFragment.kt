package com.princeakash.projectified.recruiter.myOffers.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.fragment_my_offers.*

//Parent Fragment for MyOffers Feature
class MyOffersFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null){
            childFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_offers,
                            MyOfferHomeFragment::class.java,
                            null,
                            "MyOffersFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }
    }
}