package com.example.zhangxing.smarthealth;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.textclassifier.TextClassification;
import android.widget.*;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    String radioMsg="图片";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String message = null;
                if(checkedId==R.id.rbt1){
                    message = "图片";
                } else if(checkedId==R.id.rbt2){
                    message = "视频";
                } else if(checkedId==R.id.rbt3){
                    message = "问答";
                } else if(checkedId==R.id.rbt4){
                    message = "资讯";
                }
                radioMsg = message;
                Toast.makeText(getApplication(),message+"被选中",Toast.LENGTH_SHORT).show();
            }
        });
        final Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View target){
                EditText editText = (EditText)findViewById(R.id.text);
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                } else if(editText.getText().toString().equals("transition")){
                    Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                    startActivity(intent);
                } else{
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("提示");
                    if(editText.getText().toString().equals("Health")){
                        alertDialog.setMessage(radioMsg+"搜索成功");
                    } else{
                        alertDialog.setMessage("搜索失败");
                    }
                    alertDialog.setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplication(),"对话框“确定”按钮被点击",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplication(),"对话框“取消”按钮被点击",Toast.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

    }
}
