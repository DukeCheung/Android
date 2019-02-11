package com.example.aced;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class MinePage {

    private Context context;
    private View viewpager;

    private MySkillAdapter mySkillAdapter;
    private ListView listView;
    Hero idealHero;
    List<Hero.Skill> skills;

    Bitmap bitmap;

    public MinePage(Context context){
        this.context = context;
    }

    public View getView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        viewpager = inflater.inflate(R.layout.vp_mine, null);
        init();
        return viewpager;
    }

    private void init(){
        idealHero = new Hero();
        skills = new ArrayList<Hero.Skill>();

        mySkillAdapter = new MySkillAdapter(context,skills);

        listView = (ListView)viewpager.findViewById(R.id.listview_skill);
        listView.setAdapter(mySkillAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("确定删除该技能吗？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skills.remove(position);
                        mySkillAdapter.refresh(skills);
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return false;
            }
        });

        ImageView heroImage = (ImageView)viewpager.findViewById(R.id.heroImage);
        heroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity)(context)).startActivityForResult(intent, 1);
            }
        });

        Button addSkillBtn = (Button)viewpager.findViewById(R.id.addSkillBtn);
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取技能编辑的内容，新建一个Skill，插入list中
                EditText skillName = (EditText)viewpager.findViewById(R.id.skillNameEdit);
                EditText skillPosition = (EditText)viewpager.findViewById(R.id.skillPositionEdit);
                EditText skillDescription = (EditText)viewpager.findViewById(R.id.skillDescriptionEdit);
                EditText skillCool = (EditText)viewpager.findViewById(R.id.skillCoolEdit);
                EditText skillWaste = (EditText)viewpager.findViewById(R.id.skillWasteEdit);
                EditText skillTip = (EditText)viewpager.findViewById(R.id.skillTipEdit);
                Hero.Skill newSkill = new Hero.Skill(1,skillName.getText().toString(),skillPosition.getText().toString(),
                        skillDescription.getText().toString(),Integer.parseInt(skillCool.getText().toString()),
                        Integer.parseInt(skillWaste.getText().toString()),skillTip.getText().toString(),null);
                skills.add(newSkill);
                mySkillAdapter.refresh(skills);
            }
        });

        Button completeBtn = (Button)viewpager.findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText heroName = (EditText)viewpager.findViewById(R.id.heroName);
                if(heroName.getText().toString().equals("")){
                    Toast.makeText(context,"英雄名称不能为空.",Toast.LENGTH_SHORT).show();
                }
                else if(skills.size() < 3){
                    Toast.makeText(context,"至少要添加三个技能.",Toast.LENGTH_SHORT).show();
                }else{
                    idealHero.setName(heroName.getText().toString());
                    idealHero.setSkills(skills);

//                    ImageView heroImage = (ImageView)viewpager.findViewById(R.id.heroImage);
//                    heroImage.setDrawingCacheEnabled(true);
//                    bitmap=heroImage.getDrawingCache();
//                    heroImage.setDrawingCacheEnabled(false);
//
//                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
//                    idealHero.setIcon(byteStream.toByteArray());

                    //将创建的理想英雄写入数据库
                    //TODO

                }
            }
        });

        RadioGroup radioGroup = (RadioGroup)viewpager.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedBtn = (RadioButton)viewpager.findViewById(checkedId);
                idealHero.set_type(checkedBtn.getText().toString());
            }
        });
    }

}
