package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.view.GetOffersByDomainFragment.Companion.DOMAIN_NAME
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.AI
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.ANDROID
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.CONTENT
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.ML
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.OTHERS
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.UIUX
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.VIDEO
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.Companion.WEB
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.HomeItem
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.HomeListener
//import java.util.*


class HomeFragment : Fragment(), HomeListener {

    private lateinit var list: ArrayList<HomeItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_home, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        list = ArrayList()
        list.add(HomeItem("App Development", R.mipmap.app_dev,ANDROID))
        list.add(HomeItem("Web Development", R.mipmap.web, WEB))
        list.add(HomeItem("Machine Learning", R.mipmap.ml, ML))
        list.add(HomeItem("Artificial Intelligence", R.mipmap.artificial_intelligence, AI))
        list.add(HomeItem("Content Writing", R.mipmap.content, CONTENT))
        list.add(HomeItem("UI/UX Design", R.mipmap.ui, UIUX))
        list.add(HomeItem("Video Editing", R.mipmap.video, VIDEO))
        list.add(HomeItem("Others", R.mipmap.misc, OTHERS))
        recyclerView.adapter = context?.let { HomeAdapter(list, it, this) }
        recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        return view
    }

    override fun onDomainClick(position: Int) {
        val domainArg = list[position].domainArg
        val bundle = Bundle()
        bundle.putString(DOMAIN_NAME, domainArg)
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_apply, GetOffersByDomainFragment::class.java, bundle, "GetOffersByDomain:$domainArg")
                .commit()
    }
}