package com.princeakash.projectified.user.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.princeakash.projectified.MyApplication
import com.princeakash.projectified.R

//Parent Fragment for MyOffers Feature
class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_password_reset_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null){
            childFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_password_reset_parent,
                            LoginHomeFragment(),
                            "LoginHomeFragment"
                    )
                    .commit()
        }
    }
}