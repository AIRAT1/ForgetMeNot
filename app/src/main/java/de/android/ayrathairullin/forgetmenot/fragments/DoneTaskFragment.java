package de.android.ayrathairullin.forgetmenot.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.adapter.DoneTasksAdapter;
import de.android.ayrathairullin.forgetmenot.database.DBHelper;
import de.android.ayrathairullin.forgetmenot.model.ModelTask;

public class DoneTaskFragment extends TaskFragment {

    public DoneTaskFragment() {
        // Required empty public constructor
    }

    OnTaskRestoreListener onTaskRestoreListener;

    public interface OnTaskRestoreListener {
        void onTaskRestore(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTaskRestoreListener = (OnTaskRestoreListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTaskRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);
        recyclerView = rootView.findViewById(R.id.rvDoneTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DoneTasksAdapter(this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void addTaskFromDB() {
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_DONE)},
                DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        onTaskRestoreListener.onTaskRestore(task);
    }
}
