package com.android.main_stay.Classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import com.android.main_stay.R;
import com.android.main_stay.fragment.Assignments;
import com.android.main_stay.fragment.QuizList;


public class MainFragment extends Fragment {
    public static FragmentTabHost mTabHost;

    //Mandatory Constructor
    public MainFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mTabHost = rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmenta").setIndicator("Quiz"),
                QuizList.class, null);

        TextView txttab1 = mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        txttab1.setTextSize(15);
        txttab1.setAllCaps(false);

        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Assignments"),
                Assignments.class, null);

        TextView txttab2 = mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        txttab2.setTextSize(15);
        txttab2.setAllCaps(false);

        return rootView;
    }
}