package de.android.ayrathairullin.forgetmenot.activities;

import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import de.android.ayrathairullin.forgetmenot.R;
import de.android.ayrathairullin.forgetmenot.adapter.TabAdapter;
import de.android.ayrathairullin.forgetmenot.databinding.ActivityMainBinding;
import de.android.ayrathairullin.forgetmenot.fragments.SplashFragment;
import de.android.ayrathairullin.forgetmenot.utils.PreferenceHelper;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        fragmentManager = getFragmentManager();
        runSplash();
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

        ViewPager viewPager = binding.pager;
        TabAdapter tabAdapter = new TabAdapter(fragmentManager, 2);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
