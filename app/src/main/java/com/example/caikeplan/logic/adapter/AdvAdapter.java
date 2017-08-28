package com.example.caikeplan.logic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.caikeplan.logic.AdFragment;

import java.util.ArrayList;

/**
 * Created by dell on 2017/3/7.
 */

public class AdvAdapter extends FragmentPagerAdapter {

    private ArrayList<AdFragment> mFragments;

    public AdvAdapter(FragmentManager fm, ArrayList<AdFragment> fragments) {
        super(fm);
        this.mFragments  = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
