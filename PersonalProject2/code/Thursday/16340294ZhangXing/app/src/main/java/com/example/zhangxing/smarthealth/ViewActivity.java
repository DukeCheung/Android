package com.example.zhangxing.smarthealth;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class ViewActivity extends AppCompatActivity {

    private List<Food> data;
    private List<Food> collect;
    private MyListViewAdapter myListViewAdapter;
    private int tag;
    private int randomNumber;
    private final String WIDGETSTATICACTION = "MyWidgetStaticFilter";
    private final String STATICACTION = "MyStaticFilter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        tag = 0;
        data = new ArrayList<Food>();
        collect = new ArrayList<Food>();
        myListViewAdapter = new MyListViewAdapter(ViewActivity.this, collect);
        String[] name = new String[]{"大豆","十字花科蔬菜","牛奶","海鱼","菌菇类","番茄","胡萝卜","荞麦","鸡蛋"};
        String[] title = new String[]{"粮","蔬","饮","肉","蔬","蔬","蔬","粮","杂"};
        String[] type = new String[]{"粮食","蔬菜","饮品","肉食","蔬菜","蔬菜","蔬菜","粮食","杂"};
        String[] nutrition = new String[]{"蛋白质","维生素C","钙","蛋白质","微量元素","番茄红素","胡萝卜素","膳食纤维","几乎所有营养物质"};
        String[] color = new String[]{"#BB4C3B","#C48D30","#4469B0","#20A17B","#BB4C3B","#4469B0","#20A17B","#BB4C3B","#C48D30"};
        for(int i = 0;i < 9;i++){
            Food food = new Food(name[i],title[i],type[i],nutrition[i],color[i]);
            data.add(food);
        }
        collect.add(new Food("收藏夹", "*", "","",""));
        initView();
        initFloatingButton();

        Random random = new Random();
        randomNumber = random.nextInt(data.size());
        ComponentName componentName = new ComponentName(getPackageName(),getPackageName()+".StaticReceiver");
        Intent intent = new Intent(STATICACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable(STATICACTION,data.get(randomNumber));
        intent.putExtras(bundle);
        intent.setComponent(componentName);
        sendBroadcast(intent);

        bundle.putSerializable(WIDGETSTATICACTION,data.get(randomNumber));
        Intent widgetIntentBroadcast = new Intent();
        widgetIntentBroadcast.setAction(WIDGETSTATICACTION);
        widgetIntentBroadcast.putExtras(bundle);
        sendBroadcast(widgetIntentBroadcast);

        EventBus.getDefault().register(this);
    }
    public void initView(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter<Food>(ViewActivity.this, R.layout.list_item, data) {
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
                Intent intent = new Intent(ViewActivity.this, DetailActivity.class);
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

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(myListViewAdapter);
        myListViewAdapter.refresh(collect);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    Intent intent = new Intent(ViewActivity.this, DetailActivity.class);
                    intent.putExtra("detail", collect.get(i));
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent, 1);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewActivity.this);
                alertDialog.setTitle("删除").setMessage("确定删除"+data.get(i).getName().toString()).setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplication(),"删除"+collect.get(position).getName().toString(),Toast.LENGTH_SHORT).show();
                                collect.remove(position);
                                myListViewAdapter.refresh(collect);
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
    }

    public void initFloatingButton(){
        final FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tag==0){
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    ListView listView = (ListView)findViewById(R.id.listView);
                    recyclerView.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageResource(R.drawable.mainpage);
                    tag = 1;
                }else{
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    ListView listView = (ListView)findViewById(R.id.listView);
                    recyclerView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    floatingActionButton.setImageResource(R.drawable.collect);
                    tag = 0;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1){
        ;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        if(this.getIntent().getExtras()!=null){
            if(this.getIntent().getExtras().get("collect")!=null){
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                ListView listView = (ListView)findViewById(R.id.listView);
                recyclerView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
                floatingActionButton.setImageResource(R.drawable.mainpage);
                tag = 1;
            }
        }
    }
    @Subscribe
    public void onEvent(MessageEvent msg){
        collect.add(msg.getFood());
        myListViewAdapter.refresh(collect);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
