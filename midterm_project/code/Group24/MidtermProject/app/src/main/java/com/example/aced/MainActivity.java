package com.example.aced;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    private List<View> _views;
    String[] PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }

        for (int i = 0; i < mPermissionList.size(); i++) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{mPermissionList.get(i)}, i);
        }


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        initViewPaper();
    }

    private void initViewPaper() {
        _views = new ArrayList<View>();

        initViewData();

        String [] _titles  = {"首页","英雄","装备","铭文","我的"} ;
        ViewPager vp_main_content = (ViewPager)findViewById(R.id.vp_main_content);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this,_views,_titles);
        vp_main_content.setAdapter(myPagerAdapter);
    }

    private void initViewData() {
        HerosPage herosPage = new HerosPage(MainActivity.this);
        HomePage homePage = new HomePage(MainActivity.this);
        EquipmentPage equipmentPage = new EquipmentPage(MainActivity.this);
        InscriptionPage inscriptionPage = new InscriptionPage(MainActivity.this);
        MinePage minePage = new MinePage(MainActivity.this);
        View viewpagerA = homePage.getView();
        View viewpagerB = herosPage.getView();
        View viewpagerC = equipmentPage.getView();
        View viewpagerD = inscriptionPage.getView();
        View viewpagerE = minePage.getView();

        _views = new ArrayList<>();
        _views.add(viewpagerA);
        _views.add(viewpagerB);
        _views.add(viewpagerC);
        _views.add(viewpagerD);
        _views.add(viewpagerE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i =0;i<mPermissionList.size();i++){
            if (requestCode == i) {
                if(grantResults != null){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else{//禁止授权，不操作

                }}
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data != null){
            Uri uri = data.getData();
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.mipmap.liubangicon);

            ImageView heroImage = (ImageView)findViewById(R.id.heroImage);
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                heroImage.setImageBitmap(bitmap);
            }catch (Exception e){

            }
        }
    }
}
