package com.example.burgernote;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int num;

    public PagerAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new TextMemoListFragment();

            case 1:
                return new DrawingMemoListFragment();

            case 2:
                return new RecordingMemoListFragment();

            case 3:
                return new CalendarListFragment();

            case 4:
                return new SettingsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
