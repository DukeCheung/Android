package com.example.zhangxing.webapi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Looper;
import android.os.Parcel;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.JsonReader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.support.v7.widget.RecyclerView;

public class BilibiliActivity extends AppCompatActivity {
    private RecyclerObj recyclerObj = null;
    private ImageObj imageObj = null;
    private EditText editText = null;
    private RecyclerView recyclerView = null;
    List<RecyclerObj> data;
    Map<Integer,ImageObj> imageArray;
    Map<Integer,Bitmap> seekBarBitmap;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilibili);
        imageArray = new HashMap<Integer, ImageObj>();
        seekBarBitmap = new HashMap<Integer, Bitmap>();
        editText = (EditText)findViewById(R.id.editText);
        Button button = (Button)findViewById(R.id.button);
        data = new ArrayList<RecyclerObj>();


        initRecyclerView();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = editText.getText().toString();
                if(str.equals("")){
                    Toast.makeText(getApplicationContext(), "ID不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isInt(str)){
                        if(Integer.valueOf(str)>40){
                            Toast.makeText(getApplicationContext(), "请输入小于40的ID",Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if(isNetworkConnected(getApplicationContext())){
                            Observable.create(new ObservableOnSubscribe<RecyclerObj>() {
                                @Override
                                public void subscribe(ObservableEmitter<RecyclerObj> o) throws Exception{

                                    try{
                                        URL text = new URL("https://space.bilibili.com/ajax/top/showTop?mid="+str);
                                        HttpURLConnection httpURLConnection = (HttpURLConnection) text.openConnection();
                                        httpURLConnection.setRequestMethod("GET");
                                        httpURLConnection.setConnectTimeout(5000);
                                        if(httpURLConnection.getResponseCode()==200){
                                            InputStream inputStream = httpURLConnection.getInputStream();
                                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                                            StringBuilder sb = new StringBuilder();
                                            String line;
                                            while ((line = br.readLine()) != null) {
                                                sb.append(line+"\n");
                                            }
                                            br.close();
                                            httpURLConnection.disconnect();
                                            String jsonString = sb.toString();
                                            recyclerObj = new Gson().fromJson(jsonString, RecyclerObj.class);
                                        }

                                        URL text1 = new URL("https://api.bilibili.com/pvideo?aid="+recyclerObj.getData().getAid());
                                        HttpURLConnection httpURLConnection1 = (HttpURLConnection)text1.openConnection();
                                        httpURLConnection1.setRequestMethod("GET");
                                        httpURLConnection1.setConnectTimeout(5000);
                                        if(httpURLConnection1.getResponseCode()==200){
                                            InputStream inputStream = httpURLConnection1.getInputStream();
                                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                                            StringBuilder sb = new StringBuilder();
                                            String line;
                                            while ((line = br.readLine()) != null) {
                                                sb.append(line+"\n");
                                            }
                                            br.close();
                                            httpURLConnection.disconnect();
                                            String jsonString = sb.toString();
                                            imageObj = new Gson().fromJson(jsonString, ImageObj.class);
                                            recyclerObj.setImageObj(imageObj);

                                        }
                                        o.onNext(recyclerObj);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Looper.prepare();
                                        Toast.makeText(getApplicationContext(), "数据库中不存在记录",Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<RecyclerObj>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                        }

                                        @Override
                                        public void onNext(RecyclerObj recyclerObj) {
                                            data.add(recyclerObj);
                                            myRecyclerViewAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }

                                        @Override
                                        public void onComplete() {
                                        }
                                    });

                        }else{
                            Toast.makeText(getApplicationContext(),"网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"输入需为正整数", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter<RecyclerObj>(BilibiliActivity.this, R.layout.list_item, data) {
            @Override
            public void convert(MyViewHolder holder, final RecyclerObj s) {
                // Collection是自定义的一个类，封装了数据信息，也可以直接将数据做成一个Map，那么这里就是Map<String, Object>
                final ImageView image = holder.getView(R.id.image);
                final TextView play = holder.getView(R.id.play);
                final TextView video_review = holder.getView(R.id.video_review);
                final TextView duration = holder.getView(R.id.duration);
                final TextView create = holder.getView(R.id.create);
                final TextView title = holder.getView(R.id.title);
                final TextView content = holder.getView(R.id.content);
                final String str = s.getData().getCover();
                final ProgressBar progressBar = holder.getView(R.id.progressBar);
                final SeekBar processBar = holder.getView(R.id.processBar);
                final RecyclerObj r = s;
                if(image.getBackground()==null){
                    progressBar.setVisibility(View.VISIBLE);
                }

                Observable.create(new ObservableOnSubscribe<RecyclerObj>() {
                    @Override
                    public void subscribe(ObservableEmitter<RecyclerObj> o) throws Exception{
                        try{
                            URL url = new URL(str);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("GET");
                            if (conn.getResponseCode() == 200){
                                InputStream inputStream = conn.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                r.setBitmap(bitmap);
                            }
                            o.onNext(r);
                            URL text = new URL(r.getImageObj().getData().getImage().get(0));
                            HttpURLConnection connection = (HttpURLConnection)text.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("GET");
                            if(connection.getResponseCode()==200){
                                InputStream inputStream = connection.getInputStream();
                                Bitmap t = BitmapFactory.decodeStream(inputStream);
                                r.getImageObj().setBitmap(t);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "数据库中不存在记录",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    }

                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RecyclerObj>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(RecyclerObj r) {
                                progressBar.setVisibility(View.INVISIBLE);
                                image.setVisibility(View.VISIBLE);
                                image.setImageBitmap(r.getBitmap());
                                play.setText("播放: "+Integer.toString(s.getData().getPlay()));
                                video_review.setText("评论: "+Integer.toString(s.getData().getVideo_review()));
                                duration.setText("时长: "+s.getData().getDuration());
                                create.setText("创建时间: "+s.getData().getCreate());
                                title.setText(s.getData().getTitle());
                                content.setText(s.getData().getContent());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                final int length = r.getImageObj().getData().getIndex().size();
                final List<Integer> index = r.getImageObj().getData().getIndex();
                if(length!=0){
                    processBar.setMax(index.get(length-1)+10);
                }


                processBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){

                            int currentPos = 0;
                            for(int i = 0;i < length;i++){
                                if (progress<index.get(i)){
                                    break;
                                }
                                currentPos = i;
                            }
                            if(currentPos<2){
                                currentPos = 0;
                            }else{
                                currentPos -= 2;
                            }
                            currentPos%=100;
                            int x_len = r.getImageObj().getData().getImg_x_len();
                            int x_size = r.getImageObj().getData().getImg_x_size();
                            int y_size = r.getImageObj().getData().getImg_y_size();
                            if(r.getImageObj().getBitmap()!=null){
                                Bitmap bitmap1 = r.getImageObj().getBitmap();
                                int x = (currentPos%x_len)*x_size;
                                int y = (currentPos/x_len)*y_size;
                                image.setImageBitmap(bitmap1.createBitmap(bitmap1,x,y,x_size,y_size));
                            }

                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        try{
                            Thread.sleep(200);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        seekBar.setProgress(0);
                        image.setImageBitmap(r.getBitmap());
                    }
                });
            }

        };
        recyclerView.setAdapter(myRecyclerViewAdapter);

        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
            }
            @Override
            public void onLongClick(final int position) {
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public boolean isInt(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
