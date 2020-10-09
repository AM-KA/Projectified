package com.princeakash.projectified.user.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class LoginSignUpAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {


private lateinit var context: Context

    private var totaltabs: Int = 0

    private fun LoginSignUpAdapter(fm: FragmentManager, context: Context, totaltabs: Int) {
        this.context = context
        this.totaltabs = totaltabs
    }

    override  fun getCount():Int
    {
        return totaltabs
    }


   override   fun getItem(position: Int) :Fragment {

        return when (position) {
            0 -> return LoginFragment()
            1 -> return SignUp()
            else
                -> return LoginFragment()
        }
    }
    }



