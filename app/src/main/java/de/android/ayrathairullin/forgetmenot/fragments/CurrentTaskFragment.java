package de.android.ayrathairullin.forgetmenot.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.adapter.CurrentTasksAdapter;
import de.android.ayrathairullin.forgetmenot.model.ModelTask;

public class CurrentTaskFragment extends Fragment {
    private RecyclerView rvCurrentTasks;
    private RecyclerView.LayoutManager layoutManager;
    private CurrentTasksAdapter adapter;

    public CurrentTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        rvCurrentTasks = rootView.findViewById(R.id.rvCurrentTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        rvCurrentTasks.setLayoutManager(layoutManager);
        adapter = new CurrentTasksAdapter();
        rvCurrentTasks.setAdapter(adapter);
        return rootView;
    }

    public void addTask(ModelTask newTask) {
        int position = -1;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }
        if (position != -1) {
            adapter.addItem(position, newTask);
        }else {
            adapter.addItem(newTask);
        }
    }
}
