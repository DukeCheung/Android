package com.example.aced;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class InscriptionPage {
    private Context context;
    private View viewpager;
    private MyDataBase db;

    private Spinner ins_sift;
    private ListView ins_list;
    private RadioGroup ins_group;
    private EditText ins_search;


    private List<Inscription> _Inscriptions;
    private List<Inscription> _selectedIns;

    private List<String> _ins_sift;
    private MyInscriptionAdapter _myAdapter;
    private Vector<Vector<String>> _radiobuttons;

    public InscriptionPage(Context context){
        this.context = context;
    }

    public View getView(){

        db = new MyDataBase(context);
        db.initDB();

        LayoutInflater inflater = LayoutInflater.from(context);
        viewpager = inflater.inflate(R.layout.vp_inscriptions, null);
        ins_list = (ListView) viewpager.findViewById(R.id.lv_ins_list);
        ins_sift = (Spinner) viewpager.findViewById(R.id.sp_ins_sift);
        ins_group = (RadioGroup) viewpager.findViewById(R.id.rg_ins_select);
        ins_search = (EditText) viewpager.findViewById(R.id.et_ins_serch);

        initIns();
        initSearch();
        initSpinner(ins_sift);
        return viewpager;
    }

    private void initSearch(){
        ins_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search_ins();
            }
        });
        ins_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                search_ins();
            }
        });
    }

    private void changeRadioGroup(int position){
        ins_group.removeAllViews();
        if(position == 0){
            _myAdapter =(MyInscriptionAdapter) ins_list.getAdapter();
            _myAdapter.refresh(_Inscriptions);
        }else{
            Vector selecter = _radiobuttons.get(position-1);
            for(int i = 0;i<selecter.size();i++){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(selecter.get(i).toString());
                radioButton.setWidth(ins_group.getWidth()/selecter.size());

                ins_group.addView(radioButton);
                if(i == 0)
                {
                    radioButton.setChecked(true);
                }
            }
        }
    }

    public void initIns(){
        _selectedIns = new ArrayList<>();
        _Inscriptions = new ArrayList<>();
        _Inscriptions = db.queryAllInscriptions();

        _myAdapter = new MyInscriptionAdapter(context,_Inscriptions);

        ins_list.setAdapter(_myAdapter);
        _myAdapter.notifyDataSetChanged();

    }

    private void search_ins(){
        _selectedIns.clear();
        RadioButton radioButton = (RadioButton) viewpager.findViewById(ins_group.getCheckedRadioButtonId());
        List<String> selector = new ArrayList<>();
        String checkText = "";
        if(radioButton != null){
            checkText = radioButton.getText().toString();
            selector.add(checkText);
        }
        if(!ins_search.getText().toString().equals("")){
            selector.add(ins_search.getText().toString());
        }
        _myAdapter =(MyInscriptionAdapter) ins_list.getAdapter();
        if(selector.isEmpty()) {
             _myAdapter.refresh(_Inscriptions);
             return;
        }else {
            for(int i = 0; i < _Inscriptions.size();i++){
                String text = _Inscriptions.get(i).getInscriptionDescription() + _Inscriptions.get(i).getInscriptionName()
                        + _Inscriptions.get(i).getinscriptionType() + "等级"+ _Inscriptions.get(i).getInscriptionGrade().toString();
                boolean f = true;
                for(int j = 0;j<selector.size();j++){
                    if(!text.contains(selector.get(j))){
                        f = false;
                        break;
                    }
                }
                if (f){
                    _selectedIns.add(_Inscriptions.get(i));
                }
            }
        }
        _myAdapter.refresh(_selectedIns);
    }

    private void initSpinner(Spinner ins_sift) {
        _radiobuttons = new Vector<>();
        Vector<String>property = new Vector<>();
        property.add("攻击");
        property.add("防御");
        property.add("生命");
        property.add("冷却");
        property.add("物理");
        property.add("法术");
        Vector<String>type = new Vector<>();
        type.add("红色");
        type.add("蓝色");
        type.add("绿色");
        Vector<String>grade = new Vector<>();
        grade.add("等级1");
        grade.add("等级2");
        grade.add("等级3");
        grade.add("等级4");
        grade.add("等级5");


        _radiobuttons.add(property);
        _radiobuttons.add(type);
        _radiobuttons.add(grade);

        _ins_sift = new ArrayList<String>();
        _ins_sift.add("全部");
        _ins_sift.add("类型");
        _ins_sift.add("属性");
        _ins_sift.add("等级");
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,_ins_sift);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ins_sift.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeRadioGroup(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context,"没有选择",Toast.LENGTH_SHORT).show();
            }
        });
        ins_sift.setAdapter(adapter);
    }
}
