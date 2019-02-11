package com.example.aced;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private List<View> _views;
    private Context _context;
    private String[] _titles;

    public MyPagerAdapter(Context content, List views,String [] titles){

        _context = content;
        _views = views;
        _titles = titles;
    }

    @Override
    public int getCount() {
        return _views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = _views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View)object);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return _titles[position];
    }

}

