package de.android.ayrathairullin.forgetmenot.activities;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.adapter.TabAdapter;
import de.android.ayrathairullin.forgetmenot.ads.Ads;
import de.android.ayrathairullin.forgetmenot.alarm.AlarmHelper;
import de.android.ayrathairullin.forgetmenot.database.DBHelper;
import de.android.ayrathairullin.forgetmenot.databinding.ActivityMainBinding;
import de.android.ayrathairullin.forgetmenot.dialog.AddingTaskDialogFragment;
import de.android.ayrathairullin.forgetmenot.dialog.EditTaskDialogFragment;
import de.android.ayrathairullin.forgetmenot.fragments.CurrentTaskFragment;
import de.android.ayrathairullin.forgetmenot.fragments.DoneTaskFragment;
import de.android.ayrathairullin.forgetmenot.fragments.SplashFragment;
import de.android.ayrathairullin.forgetmenot.fragments.TaskFragment;
import de.android.ayrathairullin.forgetmenot.model.ModelTask;
import de.android.ayrathairullin.forgetmenot.utils.MyApplication;
import de.android.ayrathairullin.forgetmenot.utils.PreferenceHelper;

public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener,
        EditTaskDialogFragment.EditingTaskListener{
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private PreferenceHelper preferenceHelper;
    private TabAdapter tabAdapter;
    private TaskFragment currentTaskFragment;
    private TaskFragment doneTaskFragment;
    public DBHelper dbHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Ads.showBanner(this);
        PreferenceHelper.getInstance().init(getApplicationContext());

        AlarmHelper.getInstance().init(getApplicationContext());
        dbHelper = new DBHelper(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        fragmentManager = getFragmentManager();
        runSplash();
        setUI();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_splash:
                item.setChecked(!item.isChecked());
                preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
                return true;
            case R.id.rate:
                Uri uri = Uri.parse("market://details?id="
                        + MainActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this,
                            R.string.not_open_playstore,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void runSplash() {
        if (!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)
                    .addToBackStack(null) // add transaction in stack
                    .commit();
        }
    }

    private void setUI() {
        Toolbar toolbar = binding.toolbar;
        if (null != toolbar) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }

        TabLayout tabLayout= binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = binding.pager;
        tabAdapter = new TabAdapter(fragmentManager, 2);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        currentTaskFragment = (CurrentTaskFragment)tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment)tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);
        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentTaskFragment.findTasks(newText);
                doneTaskFragment.findTasks(newText);
                return false;
            }
        });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
            }
        });
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
        currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, "Task adding cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskEdited(ModelTask updatedTask) {
        currentTaskFragment.updateTask(updatedTask);
        dbHelper.update().task(updatedTask);
    }
}
