package com.erudito.main_stay.Classes;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.widget.TextView;

import com.erudito.main_stay.fragment.Dashboard;
import com.erudito.main_stay.fragment.Feed;
import com.erudito.main_stay.fragment.Notifications;
import com.erudito.main_stay.fragment.Profile;
import com.erudito.main_stay.fragment.Quiz;
import com.google.android.material.tabs.TabLayout;
import com.android.main_stay.R;
import com.erudito.main_stay.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {

    /**
     * The {@link androidx.core.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link androidx.core.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager mViewPager;
    public static TabLayout tabLayout;

    PreferenceHelper mPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mViewPager = findViewById(R.id.container);
        mPreferenceHelper = new PreferenceHelper(this);

        setupViewPager(mViewPager);

        String count = mPreferenceHelper.getString("notifcount");
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.achievements_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.feed_selector);
        tabLayout.getTabAt(2).setIcon(R.drawable.quiz_selector);
        tabLayout.getTabAt(3).setIcon(R.drawable.notifications_selector).setCustomView(R.layout.row_feed);
        tabLayout.getTabAt(4).setIcon(R.drawable.profile_selector);

        if (tabLayout.getTabAt(3).getCustomView() != null) {
            TextView b = tabLayout.getTabAt(3).getCustomView().findViewById(R.id.txtcount);
            if (b != null) {
                b.setText(count);
            }
        }
            }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Dashboard());
        adapter.addFragment(new Feed());
        adapter.addFragment(new Quiz());
        adapter.addFragment(new Notifications());
        adapter.addFragment(new Profile());
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(4);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Integer> mFragmentIconList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            // mFragmentIconList.add(icon);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

}
