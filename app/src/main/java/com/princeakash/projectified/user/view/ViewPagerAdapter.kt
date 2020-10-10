package com.princeakash.projectified.user.view

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentActivity) :FragmentStateAdapter(fm)
{
    override fun getItemCount(): Int {
        return  2
    }

    override fun createFragment(position: Int): Fragment {

       return  when (position) {
            0 -> LoginFragment()
            1 ->  SignUpFrgment()
            else
            ->  LoginFragment()
        }
    }
}