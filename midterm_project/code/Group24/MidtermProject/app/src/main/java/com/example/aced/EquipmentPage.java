package com.example.aced;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class EquipmentPage {
    private Context context;
    private List<Equipment> _equipments;
    private MyEquipmentAdapter myEquipmentAdapter;
    private MyDataBase db;
    private Spinner spinner;

    public EquipmentPage(Context context){
        this.context = context;
        _equipments = new ArrayList<Equipment>();
        db = new MyDataBase(context);
        db.initDB();
    }

    public View getView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewpager = inflater.inflate(R.layout.vp_equipments, null);
        _equipments = db.queryAllEquipment();
        myEquipmentAdapter = new MyEquipmentAdapter(context,_equipments);
        ListView listView = (ListView)viewpager.findViewById(R.id.allequipment);
        spinner = (Spinner)viewpager.findViewById(R.id.spinner1);
        listView.setAdapter(myEquipmentAdapter);
        initSpinner();
        return viewpager;
    }

    public void initSpinner(){
        final String[] mItems = new String[7];
        mItems[0] = "全部";
        mItems[1] = "攻击";
        mItems[2] = "法术";
        mItems[3] = "防御";
        mItems[4] = "移动";
        mItems[5] = "打野";
        mItems[6] = "辅助";
// 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                changeView(mItems[pos]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context,"没有选中任何选项",Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setAdapter(adapter);

    }
    public void changeView(String str){
        if(str.equals("全部")){
            _equipments = db.queryAllEquipment();
            myEquipmentAdapter.refresh(_equipments);
            myEquipmentAdapter.notifyDataSetChanged();
            return;
        }
        int i = 0;
        switch (str){
            case "攻击":
                i = 1;
                break;
            case "法术":
                i = 2;
                break;
            case "防御":
                i = 3;
                break;
            case "移动":
                i = 4;
                break;
            case "打野":
                i = 5;
                break;
            case "辅助":
                i = 7;
                break;
            default:
                break;
        }
        _equipments = db.getEquipmentByType(i);
        myEquipmentAdapter.refresh(_equipments);
        myEquipmentAdapter.notifyDataSetChanged();
    }
}
