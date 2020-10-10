package com.princeakash.projectified.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.myApplications.view.MyApplicationsHomeFragment


class SignUpFrgment :Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blanksignup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null){
            childFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_signup,
                            SignUp::class.java,
                            null,
                            "MyApplicationFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }
    }
}
