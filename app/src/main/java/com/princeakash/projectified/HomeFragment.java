package com.princeakash.projectified;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeAdapter.HomeListener {

    private ArrayList<HomeAdapter.HomeItem> list;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        list.add(new HomeAdapter.HomeItem("App Development", R.mipmap.app_dev));
        list.add(new HomeAdapter.HomeItem("Web Development", R.mipmap.web));
        list.add(new HomeAdapter.HomeItem("Machine Learning", R.mipmap.ml));
        list.add(new HomeAdapter.HomeItem("Artificial Intelligence", R.mipmap.artificial_intelligence));
        list.add(new HomeAdapter.HomeItem("Content Writing", R.mipmap.content));
        list.add(new HomeAdapter.HomeItem("UI/UX Design", R.mipmap.ui));
        list.add(new HomeAdapter.HomeItem("Video Editing", R.mipmap.video));
        list.add(new HomeAdapter.HomeItem("Others", R.mipmap.misc));

        recyclerView.setAdapter(new HomeAdapter(list, getContext(), this));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        return view;
    }
}