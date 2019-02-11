package com.pandatem.jiyi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.pandatem.jiyi.MyDB.Card;
import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.RecycleView.HomeRecycleViewAdapter;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public abstract class MyInfoWindowAdapter implements AMap.InfoWindowAdapter,View.OnClickListener {

    private String mCardId;
    private Context mContext;
    private MyDatabase myDB;
    private  Card mCard;

    public MyInfoWindowAdapter(Context context) {
        mContext = context;
        myDB = new MyDatabase(context);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(mContext).inflate(
                R.layout.info, null);
        render(marker, infoWindow);
        return infoWindow;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    public void render(Marker marker, View view) {

        TextView titleUi = ((TextView) view.findViewById(R.id.tv_info_title));
        ImageView imageView = (ImageView) view.findViewById(R.id.imgv_info);
        TextView contentUi = ((TextView) view.findViewById(R.id.tv_info_content));
        TextView tv_info_name1 = ((TextView) view.findViewById(R.id.tv_info_name1));
        TextView tv_info_name2 = ((TextView) view.findViewById(R.id.tv_info_name2));


        if(marker.getTitle() == null|| marker.getSnippet() == null){
            imageView.setVisibility(View.GONE);
            tv_info_name1.setText("请输入内容");
            titleUi.setText("");
            contentUi.setText("");
            tv_info_name2.setText("");;
            return;
        }

        String Title = marker.getTitle();
        String [] str =  Title.split(",");
        if(str.length == 2) {
            mCard =  myDB.queryCardById(new Integer(str[0]));
            if(mCard != null){
                byte bytes[] = mCard.getCoverBitmapBytes();
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                if(bmp != null)
                {
                    imageView.setImageBitmap(bmp);
                    imageView.setTag(mCard.getId());
                    imageView.setVisibility(View.VISIBLE);
                }
                tv_info_name1.setText("作者");
                tv_info_name2.setText("概要");

                mCardId = str[0];
                titleUi.setText(str[1]);

                //点击跳转
                //imageView.setImageResource(R.mipmap.user);
                imageView.setOnClickListener(this);
            }else{
                return;
            }

        }else{
            imageView.setVisibility(View.GONE);
            tv_info_name1.setText("名字");
            tv_info_name2.setText("详情");
            titleUi.setText(Title);
        }


        if(marker.getSnippet().length() > 10) {
            contentUi.setText(marker.getSnippet().substring(0, 8));
        }else {
            contentUi.setText(marker.getSnippet());

        }


    }



}
