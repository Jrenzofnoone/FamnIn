package com.example.farmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class cal_dash extends Fragment {
    private TabLayout tabLayout;
    private ViewPager ViewPager;

    public cal_dash() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cal_tab, container, false);
        tabLayout = view.findViewById(R.id.tab_cal);
        ViewPager = view.findViewById(R.id.vp_cal);

        tabLayout.setupWithViewPager(ViewPager);
        // Initialize tabLayout here

        VPadapter vPadapter =new VPadapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vPadapter.addFragment(new cal_fragment_basic(),"basic");
        vPadapter.addFragment(new cal_fragment_advance(),"advance");
        ViewPager.setAdapter(vPadapter);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // You can initialize UI elements and set up calculation logic here
    }

}
