package com.princeakash.projectified.user.view

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.princeakash.projectified.R
import kotlinx.android.synthetic.main.login.*


class LoginSignupScreens : AppCompatActivity() {

    private lateinit var TabLayout: TabLayout
    private lateinit var ViewPager: ViewPager
    private val titles = arrayOf("Login", "SignUp")
    private lateinit var pagerAdapter:LoginSignUpAdapter

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.login)

        pagerAdapter = LoginSignUpAdapter(supportFragmentManager);


        TabLayout = findViewById(R.id.tablayout1)
        ViewPager = findViewById(R.id.viewPager)
        ViewPager.setAdapter(pagerAdapter)
        TabLayoutMediator(TabLayout, viewPager) { tab: TabLayout.Tab, position: Int -> tab.setText(titles.get(position)) }.attach()



        fun BackPressed() {
            if (ViewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                ViewPager.setCurrentItem(ViewPager.getCurrentItem() - 1);
            }
        }

    }


    class LoginSignUpAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private lateinit var context: Context

        private var totaltabs: Int = 0

        private fun LoginSignUpAdapter(fm: FragmentManager, context: Context, totaltabs: Int) {
            this.context = context
            this.totaltabs = totaltabs
        }

        override fun getCount(): Int {
            return totaltabs
        }


        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 -> return LoginFragment()
                1 -> return SignUpFrgment()
                else
                -> return LoginFragment()
            }
        }
    }
}





