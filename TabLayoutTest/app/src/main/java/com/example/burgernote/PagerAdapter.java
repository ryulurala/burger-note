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
                DrawingMemoListFragment tab1 = new DrawingMemoListFragment();
                return tab1;

            case 1:
                TextMemoListFragment tab2 = new TextMemoListFragment();
                return tab2;

            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;

            case 3:
                CalendarListFragment tab4 = new CalendarListFragment();
                return tab4;

            case 4:
                TabFragment5 tab5 = new TabFragment5();
                return tab5;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
