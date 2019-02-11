package com.pandatem.jiyi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.pandatem.jiyi.ViewPager.MyPagerAdapter;
import com.pandatem.jiyi.ViewPager.MyViewPager;

import com.pandatem.jiyi.Fragment.*;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String globalUsername;

    private int WRITE_COARSE_LOCATION_REQUEST_CODE=11;
    private MyViewPager mViewPager;
    MyPagerAdapter myPagerAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_map:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_private:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_mine:
                    mViewPager.setCurrentItem(3);
                    return  true;
            }
            return false;
        }
    };

    public String getGlobalUsername(){
        return globalUsername;
    }

    public void setGlobalUsername(String str){
        this.globalUsername = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalUsername = "";

        mViewPager = findViewById(R.id.viewpager);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        init();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{  Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }




    }


    private void init(){
        List<Fragment> fragments = new ArrayList<>();

        com.pandatem.jiyi.Fragment.HomeFragment homeFragment = new com.pandatem.jiyi.Fragment.HomeFragment();
        fragments.add(homeFragment);
        com.pandatem.jiyi.Fragment.MapFragment mapFragment = new com.pandatem.jiyi.Fragment.MapFragment();
        fragments.add(mapFragment);
        com.pandatem.jiyi.Fragment.PrivateFragment privateFragment = new com.pandatem.jiyi.Fragment.PrivateFragment();
        fragments.add(privateFragment);
        com.pandatem.jiyi.Fragment.MineFragment mineFragment = new com.pandatem.jiyi.Fragment.MineFragment();
        fragments.add(mineFragment);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mViewPager.setScanScroll(false);
        mViewPager.setAdapter(myPagerAdapter);



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else{//禁止授权，不操作

            }
        }
        //可在此继续其他操作。
    }

}
