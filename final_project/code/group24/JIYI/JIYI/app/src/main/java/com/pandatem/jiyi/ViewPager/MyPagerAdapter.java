package com.pandatem.jiyi.ViewPager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> mf){
        super(fm);
        this.mFragments = mf;
    }

    @Override
    public int getCount() {
        return mFragments!= null ? mFragments.size():0;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }


}
