package de.android.ayrathairullin.forgetmenot.fragments;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.activities.MainActivity;
import de.android.ayrathairullin.forgetmenot.adapter.TaskAdapter;
import de.android.ayrathairullin.forgetmenot.alarm.AlarmHelper;
import de.android.ayrathairullin.forgetmenot.dialog.EditTaskDialogFragment;
import de.android.ayrathairullin.forgetmenot.model.Item;
import de.android.ayrathairullin.forgetmenot.model.ModelTask;

public abstract class TaskFragment extends Fragment{
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected TaskAdapter adapter;
    public MainActivity activity;
    public AlarmHelper alarmHelper;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }
        alarmHelper = AlarmHelper.getInstance();
        addTaskFromDB();
    }

    public abstract void addTask(ModelTask newTask, boolean saveToDB);

    public void updateTask(ModelTask task) {
        adapter.updateTask(task);
    }

    public void removeTaskDialog(final int location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_removing_message);
        Item item = adapter.getItem(location);
        if (item.isTask()) {
            ModelTask removingTask = (ModelTask)item;
            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};
            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(location);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addTask(activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]) {
                                alarmHelper.removeAlarm(timeStamp);
                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        builder.show();
    }

    public void showTaskEditDialog(ModelTask task) {
        DialogFragment editingTaskDialog = EditTaskDialogFragment.newInstance(task);
        editingTaskDialog.show(getActivity().getFragmentManager(), "EditTaskDialogFragment");
    }

    public abstract void checkAdapter();

    public abstract void findTasks(String title);

    public abstract void addTaskFromDB();

    public abstract void moveTask(ModelTask task);
}
