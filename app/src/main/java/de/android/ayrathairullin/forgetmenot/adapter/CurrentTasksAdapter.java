package de.android.ayrathairullin.forgetmenot.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.fragments.CurrentTaskFragment;
import de.android.ayrathairullin.forgetmenot.model.Item;
import de.android.ayrathairullin.forgetmenot.model.ModelTask;
import de.android.ayrathairullin.forgetmenot.utils.Utils;

public class CurrentTasksAdapter extends TaskAdapter {
    private static final int TYPE_TASK = 0;
    private static final int TYPE_SEPARATOR = 1;

    public CurrentTasksAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.model_task, parent, false);
                TextView title = v.findViewById(R.id.tvTaskTitle);
                TextView date = v.findViewById(R.id.tvTaskDate);
                return new TaskViewHolder(v, title, date);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        if (item.isTask()) {
            viewHolder.itemView.setEnabled(true);
            ModelTask task = (ModelTask)item;
            TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;

            taskViewHolder.title.setText(task.getTitle());
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isTask()) {
            return TYPE_TASK;
        } else {
            return TYPE_SEPARATOR;
        }
    }
}
