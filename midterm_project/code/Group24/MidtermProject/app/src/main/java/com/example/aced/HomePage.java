package com.example.aced;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

class HomePage {
    private Context context;

    public HomePage(Context context){
        this.context = context;
    }

    public View getView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewpager = inflater.inflate(R.layout.vp_homepage, null);
        return viewpager;
    }
}
