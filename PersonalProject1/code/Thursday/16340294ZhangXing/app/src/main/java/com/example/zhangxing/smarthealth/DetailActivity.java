package com.example.zhangxing.smarthealth;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        initDetail();

    }
    public void initDetail(){
        ListView listView = (ListView)findViewById(R.id.detail_view);
        List<String> str = new ArrayList<String>();
        String[] strings = new String[]{"分享信息","不感兴趣","查看更多信息","出错反馈"};
        for(int i = 0; i < 4;i++){
            str.add(strings[i]);
        }
        MyDetailAdapter myDetailAdapter = new MyDetailAdapter(DetailActivity.this,str);
        listView.setAdapter(myDetailAdapter);
        final Food food = (Food)this.getIntent().getSerializableExtra("detail");
        TextView textView = (TextView)findViewById(R.id.food);
        textView.setText(food.getName());
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout0);
        relativeLayout.setBackgroundColor(Color.parseColor(food.getColor()));
        TextView textView1 = (TextView)findViewById(R.id.detail_type);
        textView1.setText(food.getType());
        TextView textView2 = (TextView)findViewById(R.id.detail_nutrition);
        textView2.setText("富含 "+food.getNutrition());
        final ImageView star = (ImageView) findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(star.getTag().toString().equals("full")){
                    star.setTag("empty");
                    star.setImageResource(R.drawable.empty_star);
                }
                else{
                    star.setTag("full");
                    star.setImageResource(R.drawable.full_star);
                }
            }
        });
        final List<Food> collect = new ArrayList<Food>();
        ImageView imageView1 = (ImageView)findViewById(R.id.collect);
        final Intent intent = new Intent();
        imageView1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                collect.add(food);
                Toast.makeText(getApplication(),"已收藏",Toast.LENGTH_SHORT).show();
                intent.putExtra("food",(Serializable)collect);
                setResult(1, intent);
            }
        });
        ImageView imageView = (ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
