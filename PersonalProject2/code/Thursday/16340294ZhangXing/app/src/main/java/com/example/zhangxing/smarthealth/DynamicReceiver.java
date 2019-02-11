package com.example.zhangxing.smarthealth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "MyDynamicFilter";
    private static final String WIDGETDYNAMICACTION = "MyWidgetDynamicFilter";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMICACTION)) {    //动作检测
            Bundle bundle = intent.getExtras();
            Food food = (Food)bundle.getSerializable(DYNAMICACTION);
            NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder=new Notification.Builder(context);

            builder.setContentTitle("已收藏")
                    .setContentText(food.getName())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.full_star)
                    .setAutoCancel(true);

            Intent mIntent = new Intent(context,ViewActivity.class);
            mIntent.putExtra("collect", "listview");
            PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            Notification notify=builder.build();
            manager.notify(0,notify);
        }

        if(intent.getAction().equals(WIDGETDYNAMICACTION)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Bundle bundle = intent.getExtras();
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            Food food = (Food)bundle.getSerializable(WIDGETDYNAMICACTION);
            view.setTextViewText(R.id.appwidget_text, "已收藏 "+food.getName());
            view.setImageViewResource(R.id.appwidget_image,R.drawable.full_star);

            Intent i = new Intent(context, ViewActivity.class);
            i.putExtra("collect", "listview");
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.appwidget_image, pi); //设置点击事件
            ComponentName me = new ComponentName(context, NewAppWidget.class);
            appWidgetManager.updateAppWidget(me, view);
        }
    }
}
