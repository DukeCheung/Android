package com.example.aced;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class HerosPage {

    private List<View> _views;
    private View viewpager;
    private MyListViewAdapter _myAdapter;
    private List<String> _heros_sift;
    private List<Hero> _selected_heros;
    private List<Hero> _heros;
    private List<Inscription> _inscriptions;
    private List<Hero.Skill> _skills;
    private List<Equipment> _equipments;
    private Vector<Vector<String>> _radiobuttons;
    private Context context;
    private MyDataBase db;

    private Spinner hero_sift;
    private ListView hero_list;
    private RadioGroup hero_group;
    private EditText hero_search;

    public HerosPage(Context context){
        this.context = context;
    }

    public View getView()
    {
        db = new MyDataBase(context);
        db.initDB();

        LayoutInflater inflater = LayoutInflater.from(context);
        viewpager = inflater.inflate(R.layout.vp_heros, null);
        hero_list = (ListView) viewpager.findViewById(R.id.listview_hero_list);
        hero_sift = (Spinner) viewpager.findViewById(R.id.spinner_hero_sift);
        hero_group = (RadioGroup) viewpager.findViewById(R.id.radiogroup_hero_select);
        hero_search = (EditText) viewpager.findViewById(R.id.et_hero_serch);
        initHeros();
        initListView(hero_list);
        initSpinner(hero_sift);
        initSearch();

        return viewpager;
    }

    private void initSearch(){
        hero_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search_heros();
            }
        });
        hero_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                search_heros();
            }
        });
    }

    private void changeRadioGroup(int position){
        hero_group.removeAllViews();
        if(position == 0){
            _myAdapter =(MyListViewAdapter) hero_list.getAdapter();
            _myAdapter.refresh(_heros);
        }else{
            Vector selecter = _radiobuttons.get(position-1);
            for(int i = 0;i<selecter.size();i++){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(selecter.get(i).toString());
                radioButton.setWidth(hero_group.getWidth()/selecter.size());

                hero_group.addView(radioButton);
                if(i == 0)
                {
                    radioButton.setChecked(true);
                }
            }
        }
    }

    private void initSpinner(Spinner hero_sift) {
        _radiobuttons = new Vector<>();
        Vector<String>profession = new Vector<>();
        profession.add("坦克");
        profession.add("战士");
        profession.add("刺客");
        profession.add("法师");
        profession.add("射手");
        profession.add("辅助");
        Vector<String>Heroictype = new Vector<>();
        Heroictype.add("近战");
        Heroictype.add("远程");
        Heroictype.add("物理");
        Heroictype.add("魔法");
        Vector<String>Heroicpositioning = new Vector<>();
        Heroicpositioning.add("上单");
        Heroicpositioning.add("中单");
        Heroicpositioning.add("打野");
        Heroicpositioning.add("ADC");
        Heroicpositioning.add("辅助");
        _radiobuttons.add(profession);
        _radiobuttons.add(Heroictype);
        _radiobuttons.add(Heroicpositioning);

        _heros_sift = new ArrayList<String>();
        _heros_sift.add("全部");
        _heros_sift.add("职业");
        _heros_sift.add("类型");
        _heros_sift.add("位置");
//        _heros_sift.add("价格");
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,_heros_sift);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        hero_sift.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeRadioGroup(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context,"没有选择",Toast.LENGTH_SHORT).show();
            }
        });
        hero_sift.setAdapter(adapter);
    }


    public void initHeros(){
        _selected_heros = new ArrayList<>();
        _heros = new ArrayList<Hero>();
        _heros = db.loadAllHero();
        _selected_heros = db.loadAllHero();
    }

    public void initListView(ListView lv_hero_list){

        lv_hero_list.setDivider(null);
        _myAdapter = new MyListViewAdapter(context,R.layout.list_item_hero,_heros);


        //将对应的hero传到detail中，跳转到detail
        _myAdapter.setOnItemHero1ClickListener(new MyListViewAdapter.onItemHero1ClickListener() {
            @Override
            public void onHero1Click(int position) {
                Toast.makeText(context,""+_selected_heros.get(position*3).getName(),Toast.LENGTH_SHORT).show();
                Intent detailIntent  = new Intent(context,DetailActivity.class);
                detailIntent.putExtra("hero",  _selected_heros.get(position*3));
                context.startActivity(detailIntent);
            }
        });

        _myAdapter.setOnItemHero2ClickListener(new MyListViewAdapter.onItemHero2ClickListener() {
            @Override
            public void onHero2Click(int position) {
                Toast.makeText(context,""+_selected_heros.get(position*3+1).getName(),Toast.LENGTH_SHORT).show();
                Intent detailIntent  = new Intent(context,DetailActivity.class);
                detailIntent.putExtra("hero",  _selected_heros.get(position*3+1));
                context.startActivity(detailIntent);
            }
        });

        _myAdapter.setOnItemHero3ClickListener(new MyListViewAdapter.onItemHero3ClickListener() {
            @Override
            public void onHero3Click(int position) {
                Toast.makeText(context,""+_selected_heros.get(position*3+2).getName(),Toast.LENGTH_SHORT).show();
                Intent detailIntent  = new Intent(context,DetailActivity.class);
                detailIntent.putExtra("hero",  _selected_heros.get(position*3+2));
                context.startActivity(detailIntent);
            }
        });

        lv_hero_list.setAdapter(_myAdapter);
        _myAdapter.notifyDataSetChanged();
    }

    private void search_heros(){
        RadioButton radioButton = (RadioButton) viewpager.findViewById(hero_group.getCheckedRadioButtonId());
        String checkText = "";
        if(radioButton != null){
            checkText = radioButton.getText().toString();
        }
        _selected_heros.clear();
        for(int i = 0; i<_heros.size(); i++){
            Hero temp = _heros.get(i);
            if((temp.get_type().contains(checkText) || temp.getPositon().contains(checkText) ||
                    temp.getOccupation().contains(checkText))&& temp.getName().contains(hero_search.getText().toString())){
                _selected_heros.add(_heros.get(i));
            }
        }
        _myAdapter =(MyListViewAdapter) hero_list.getAdapter();
        _myAdapter.refresh(_selected_heros);
    }
}