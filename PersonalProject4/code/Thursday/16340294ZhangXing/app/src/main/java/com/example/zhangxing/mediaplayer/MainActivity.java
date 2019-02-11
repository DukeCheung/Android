package com.example.zhangxing.mediaplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

public class MainActivity extends AppCompatActivity {
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private ImageView play, stop, file, back;
    private CircleImageView image;
    private SeekBar seekBar;
    private TextView current, total, singer, song;
    private String path = null;
    private ObjectAnimator animator;
    private boolean flag = true;//是否重新启动
    private Binder binder;
    int totalTime;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        bindMusicService();
        init();
        setClickListener();
    }

    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainActivity.this.binder = (Binder) service;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try{
                binder.transact(0,data,reply,0);
            }catch (Exception e){
                e.printStackTrace();
            }
            int i = reply.readInt();
            totalTime = i;
            total.setText(time.format(i));
            seekBar.setMax(i);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }


    };

    private void init() {

        play = (ImageView) findViewById(R.id.play);
        stop = (ImageView) findViewById(R.id.stop);
        current = (TextView) findViewById(R.id.music_current);
        total = (TextView) findViewById(R.id.music_total);
        seekBar = (SeekBar) findViewById(R.id.bar);
        back = (ImageView) findViewById(R.id.back);
        image = (CircleImageView) findViewById(R.id.image);
        file = (ImageView) findViewById(R.id.file);
        singer = (TextView)findViewById(R.id.singer);
        song = (TextView)findViewById(R.id.song);
    }

    private void setClickListener() {
        animator = ObjectAnimator.ofFloat(image, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//设置动画重复次数（-1代表一直转）
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    binder.transact(1,data,reply,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (play.getTag().toString().equals("0")) {
                    play.setTag("1");
                    play.setImageResource(R.drawable.pause);
                    if (flag) {
                        animator.start();
                    } else {
                        animator.resume();
                    }
                } else {
                    play.setTag("0");
                    play.setImageResource(R.drawable.play);
                    animator.pause();
                }
                if(flag==true){
                    flag = false;
                    Observable.create(new ObservableOnSubscribe<Integer>() {
                        @Override
                        public void subscribe(ObservableEmitter<Integer> o) throws Exception{
                            int t = 0;
                            int isEnd = 0;
                            do{
                                if(binder!=null&&flag==false) {
                                    try {
                                        Parcel data = Parcel.obtain();
                                        Parcel reply = Parcel.obtain();
                                        binder.transact(4, data, reply, 0);
                                        t = reply.readInt();
                                        o.onNext(t);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try{
                                        Parcel data = Parcel.obtain();
                                        Parcel reply = Parcel.obtain();
                                        binder.transact(6, data, reply, 0);
                                        isEnd = reply.readInt();
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(isEnd==1){
                                    flag=true;
                                    break;
                                }
                                Thread.sleep(200);
                            }while(true);
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(Integer integer) {
                                    current.setText(time.format(integer));
                                    seekBar.setProgress(integer);
                                    if(flag){
                                        seekBar.setProgress(0);
                                        current.setText(time.format(0));
                                        play.setTag("0");
                                        play.setImageResource(R.drawable.play);
                                        animator.end();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    binder.transact(2,data,reply,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
                seekBar.setProgress(0);
                current.setText(time.format(0));
                play.setTag("0");
                play.setImageResource(R.drawable.play);
                animator.end();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handler.removeCallbacks(runnable);
                unbindService(sc);
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if (fromUser) {
                    try{
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        data.writeInt(progress);
                        binder.transact(3,data,reply,0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //musicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeInt(seekBar.getProgress());
                    binder.transact(3,data,reply,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType(“image/*”);//选择图片
                //intent.setType("audio/*"); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(getApplication(), "权限未申请", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
            }
            try{
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeString(path);
                binder.transact(5,data,reply,0);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                binder.transact(0,data,reply,0);
                totalTime = reply.readInt();
                seekBar.setMax(totalTime);
                total.setText(time.format(totalTime));
            }catch (Exception e){
                e.printStackTrace();
            }
            play.setTag("0");
            play.setImageResource(R.drawable.play);
            animator.end();
            flag = true;
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            song.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            singer.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            byte[] d = mmr.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(d, 0, d.length);
            image.setImageBitmap(bitmap);
            mmr.release();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
