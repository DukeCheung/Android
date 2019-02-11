package com.example.zhangxing.smarthealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class RecyclerActivity extends AppCompatActivity {

    private List<Food> data;
    private List<Food> collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        data = new ArrayList<Food>();
        collect = new ArrayList<Food>();
        String[] name = new String[]{"大豆","十字花科蔬菜","牛奶","海鱼","菌菇类","番茄","胡萝卜","荞麦","鸡蛋"};
        String[] title = new String[]{"粮","蔬","饮","肉","蔬","蔬","蔬","粮","杂"};
        String[] type = new String[]{"粮食","蔬菜","饮品","肉食","蔬菜","蔬菜","蔬菜","粮食","杂"};
        String[] nutrition = new String[]{"蛋白质","维生素C","钙","蛋白质","微量元素","番茄红素","胡萝卜素","膳食纤维","几乎所有营养物质"};
        String[] color = new String[]{"#BB4C3B","#C48D30","#4469B0","#20A17B","#BB4C3B","#4469B0","#20A17B","#BB4C3B","#C48D30"};

        for(int i = 0;i < 9;i++){
            Food food = new Food(name[i],title[i],type[i],nutrition[i],color[i]);
            data.add(food);
        }

        initRecyclerView();
        initFloatingButton();
    }
    public void initRecyclerView(){


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter<Food>(RecyclerActivity.this, R.layout.list_item, data) {
            @Override
            public void convert(MyViewHolder holder, Food s) {
                // Colloction是自定义的一个类，封装了数据信息，也可以直接将数据做成一个Map，那么这里就是Map<String, Object>
                TextView textView = holder.getView(R.id.textView);
                textView.setText(s.getName().toString());
                TextView button = holder.getView(R.id.btn);
                button.setText(s.getTitle().toString());
            }
        };
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myRecyclerViewAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter((scaleInAnimationAdapter));
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(RecyclerActivity.this, DetailActivity.class);
                intent.putExtra("detail", data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, 1);
            }
            @Override
            public void onLongClick(final int position) {
                String str = data.get(position).getName();
                data.remove(position);
                myRecyclerViewAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplication(),"删除"+str,Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        intent.putExtra("collect",(Serializable)data);
        setResult(2, intent);
    }

    public void initFloatingButton(){
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecyclerActivity.this, ListActivity.class);
                intent.putExtra("collect", (Serializable)collect);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1){
        if (resultCode == 1){
            List<Food> food = (List<Food>)data1.getSerializableExtra("food");
            if(food!=null){
                for(Food f : food){
                    Food newFood = new Food();
                    newFood.setName(f.getName());
                    newFood.setTitle(f.getTitle());
                    newFood.setType(f.getType());
                    newFood.setNutrition(f.getNutrition());
                    newFood.setColor(f.getColor());
                    collect.add(newFood);
                }
            }
        }
        else if (resultCode==2){
            collect = (List<Food>)data1.getSerializableExtra("collect");
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        initRecyclerView();
        initFloatingButton();
    }
}
