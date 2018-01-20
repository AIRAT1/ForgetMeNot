package de.android.ayrathairullin.forgetmenot.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.adapter.CurrentTasksAdapter;

public class CurrentTaskFragment extends TaskFragment {

    public CurrentTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        recyclerView = rootView.findViewById(R.id.rvCurrentTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CurrentTasksAdapter(this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
