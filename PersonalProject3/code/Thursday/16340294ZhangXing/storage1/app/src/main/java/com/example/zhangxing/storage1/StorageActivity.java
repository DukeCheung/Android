package com.example.zhangxing.storage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StorageActivity extends AppCompatActivity {

    private int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        SharedPreferences sharedPref = getSharedPreferences("Storage", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final EditText editText1 = (EditText)findViewById(R.id.new_password);
        final EditText editText2 = (EditText)findViewById(R.id.confirm_password);

        final String password = sharedPref.getString("password","");
        if(!password.isEmpty()){
            editText1.setVisibility(View.INVISIBLE);
            editText2.setHint("Password");
            flag = 1;
        }

        Button button1 = (Button)findViewById(R.id.confirm);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password1 = editText1.getText().toString();
                final String password2 = editText2.getText().toString();
                if(flag==0){
                    if(password1.isEmpty()){
                        Toast.makeText(getApplication(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else if(!password1.equals(password2)){
                        Toast.makeText(getApplication(), "Password Mismatched.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(StorageActivity.this, FileActivity.class);
                        editor.putString("password",password1);
                        editor.commit();
                        startActivity(intent);
                    }
                }
                else{
                    if(password2.equals(password)){
                        Intent intent = new Intent(StorageActivity.this, FileActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplication(),"Invalid Password.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        Button button2 = (Button)findViewById(R.id.clear);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText("");
                if(flag==0){
                    editText1.setText("");
                }
            }
        });
    }
}
