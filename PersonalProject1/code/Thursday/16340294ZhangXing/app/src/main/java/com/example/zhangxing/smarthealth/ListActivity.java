package com.example.zhangxing.smarthealth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<Food> data;
    private MyListViewAdapter myListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        data = new ArrayList<Food>();
        myListViewAdapter = new MyListViewAdapter(ListActivity.this, data);
        initListView();
        initFloatingButton();
        Intent intent = getIntent();
        intent.putExtra("collect",(Serializable)data);
        setResult(2, intent);
    }
    public void initListView(){
        List<Food> food = (List<Food>)this.getIntent().getSerializableExtra("collect");
        if(food.size()==0)
            data.add(new Food("收藏夹", "*", "","",""));
        else {
            if(food.get(0).getName().equals("收藏夹")){
                ;
            }
            else{
                data.add(new Food("收藏夹", "*", "","",""));
            }
            for(Food f : food){
                Food newFood = new Food();
                newFood.setName(f.getName());
                newFood.setTitle(f.getTitle());
                newFood.setType(f.getType());
                newFood.setNutrition(f.getNutrition());
                newFood.setColor(f.getColor());
                data.add(newFood);
            }
        }
        ListView listView = (ListView)findViewById(R.id.listView);
        myListViewAdapter.refresh(data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                    intent.putExtra("detail", data.get(i));
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent, 1);
                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListActivity.this);
                alertDialog.setTitle("删除").setMessage("确定删除"+data.get(i).getName().toString()).setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplication(),"删除"+data.get(position).getName().toString(),Toast.LENGTH_SHORT).show();
                                data.remove(position);
                                myListViewAdapter.refresh(data);
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.show();
                return true;
            }
        });
        listView.setAdapter(myListViewAdapter);
    }
    public void initFloatingButton(){
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("collect",(Serializable)data);
                setResult(2, intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1){
        if (resultCode == 1){
            List<Food> food = (List<Food>)data1.getSerializableExtra("food");
            for(Food f : food){
                data.add(f);
            }
        }
        myListViewAdapter.refresh(data);
    }
}