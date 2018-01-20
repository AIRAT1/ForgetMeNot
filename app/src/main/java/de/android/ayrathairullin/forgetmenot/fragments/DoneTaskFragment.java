package de.android.ayrathairullin.forgetmenot.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.android.ayrathairullin.forgetmenot.R;

public class DoneTaskFragment extends TaskFragment {

    public DoneTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);
        recyclerView = rootView.findViewById(R.id.rvDoneTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }

}
