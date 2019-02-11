package com.example.zhangxing.storage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        final EditText editText = (EditText)findViewById(R.id.editfile);
        Button button1 = (Button)findViewById(R.id.save);
        Button button2 = (Button)findViewById(R.id.load);
        Button button3 = (Button)findViewById(R.id.file_clear);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fileOutputStream = openFileOutput("File", MODE_PRIVATE);
                    String str = editText.getText().toString();
                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(getApplication(),"Save successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Toast.makeText(getApplication(),"Fail to save file.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fileInputStream = openFileInput("File");
                    byte[] contents = new byte[fileInputStream.available()];
                    StringBuilder sb = new StringBuilder("");
                    int len = 0;
                    while((len = fileInputStream.read(contents))>0){
                        sb.append(new String(contents, 0, len));
                    }
                    fileInputStream.close();
                    editText.setText(sb.toString());
                    Toast.makeText(getApplication(),"Load successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Toast.makeText(getApplication(),"Fail to load file.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }
}
