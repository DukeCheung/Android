package com.example.zhangxing.smarthealth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class StaticReceiver extends BroadcastReceiver {
    private static final String STATICACTION = "MyStaticFilter";
    private static final String CHANNEL_ID = "Static";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(STATICACTION)){
            Bundle bundle = intent.getExtras();
            Food food = (Food)bundle.getSerializable(STATICACTION);
            NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder=new Notification.Builder(context);

            builder.setContentTitle("今日推荐")
                    .setContentText(food.getName())
                    .setTicker("您有一条新消息")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.empty_star)
                    .setAutoCancel(true);

            Intent mIntent = new Intent(context,DetailActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mIntent.putExtra("detail", food);
            PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            Notification notify=builder.build();
            manager.notify(0,notify);
        }
    }
}
