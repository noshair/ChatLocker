package com.locker.whatschatlocker.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.locker.whatschatlocker.fragments.TMDirectChatFragment;
import com.locker.whatschatlocker.fragments.TMMainFragment;


public class TMTabLayoutAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;

    public TMTabLayoutAdapter(Context context2, FragmentManager fragmentManager, int i) {
        super(fragmentManager);
        context = context2;
        totalTabs = i;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new TMMainFragment();
        }
        if (i == 1) {
            return new TMDirectChatFragment();
        }
          return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
