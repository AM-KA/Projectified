package com.princeakash.projectified.candidate.addApplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.HomeItem
import com.princeakash.projectified.candidate.addApplication.view.HomeAdapter.HomeListener
import java.util.*


class HomeFragment : Fragment(), HomeListener {

    private var list: ArrayList<HomeItem>? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_home, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        list = ArrayList()
        list!!.add(HomeItem("App Development", R.mipmap.app_dev))
        list!!.add(HomeItem("Web Development", R.mipmap.web))
        list!!.add(HomeItem("Machine Learning", R.mipmap.ml))
        list!!.add(HomeItem("Artificial Intelligence", R.mipmap.artificial_intelligence))
        list!!.add(HomeItem("Content Writing", R.mipmap.content))
        list!!.add(HomeItem("UI/UX Design", R.mipmap.ui))
        list!!.add(HomeItem("Video Editing", R.mipmap.video))
        list!!.add(HomeItem("Others", R.mipmap.misc))
        recyclerView.adapter = context?.let { HomeAdapter(list!!, it, this) }

        recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}