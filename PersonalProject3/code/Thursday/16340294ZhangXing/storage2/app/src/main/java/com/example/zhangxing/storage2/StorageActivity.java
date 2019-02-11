package com.example.zhangxing.storage2;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private ImageView imageView;
    private int flag = 0;
    private int select = 0;
    private static final String TABLE_NAME = "MEMBERTABLE";
    private String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS};
    List<String> mPermissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        final MemberDB MemDB = new MemberDB(getApplicationContext());
        this.imageView = (ImageView)findViewById(R.id.imageView);
        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        final EditText confirm = (EditText)findViewById(R.id.confirm);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        Button ok = (Button)findViewById(R.id.ok);
        Button clear = (Button)findViewById(R.id.clear);

        for(int i = 0;i < permissions.length;i++){
            if(ContextCompat.checkSelfPermission(this, permissions[i])!= PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.size()>0){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.register){
                    flag = 1;
                    confirm.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    password.setText("");
                }
                else{
                    flag = 0;
                    confirm.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    password.setText("");
                    confirm.setText("");
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member m = MemDB.getByUsername(username.getText().toString());
                if(flag==0){
                    if(username.getText().toString().equals("")){
                        Toast.makeText(getApplication(),"Username cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().equals("")){
                        Toast.makeText(getApplication(),"Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else if(MemDB.getByUsername(username.getText().toString())==null){//username does not exist
                        Toast.makeText(getApplication(),"Username not existed.", Toast.LENGTH_SHORT).show();
                    }
                    else if(!m.getPassword().equals(password.getText().toString())){//does not match
                        Toast.makeText(getApplication(),"Invalid Password.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplication(),"Correct password.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StorageActivity.this, CommentActivity.class);
                        intent.putExtra("username",username.getText().toString());
                        startActivity(intent);
                    }
                }
                else{
                    if(username.getText().toString().equals("")){
                        Toast.makeText(getApplication(),"Username cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().equals("")){
                        Toast.makeText(getApplication(),"Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else if(flag==1&&!password.getText().toString().equals(confirm.getText().toString())){
                        Toast.makeText(getApplication(),"Password Mismatch.", Toast.LENGTH_SHORT).show();
                    }
                    else if(m!=null){//username already exisited
                        Toast.makeText(getApplication(),"Username already existed.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Resources res = getResources();
                        Bitmap bmp = null;
                        if(select == 0){
                            bmp = BitmapFactory.decodeResource(res, R.drawable.me);
                        }
                        else{
                            imageView.setDrawingCacheEnabled(true);
                            bmp = imageView.getDrawingCache().copy(Bitmap.Config.RGB_565, false);
                            imageView.setDrawingCacheEnabled(false);
                        }

                        Member mem = new Member(username.getText().toString(),password.getText().toString(),null,bmp);
                        MemDB.insert(mem);

                        Toast.makeText(getApplication(),"Register succeed.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                confirm.setText("");
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1){
                    select = 1;
                    load(v);
                }
            }
        });
    }

    public void load(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            Bitmap original = null;

            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            c.close();

            // 设置参数
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.decodeFile(imagePath, options);
            int height = options.outHeight;
            int width= options.outWidth;
            int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
            int minLen = Math.min(height, width); // 原图的最小边长
            if(minLen > 100) { // 如果原始图像的最小边长大于100dp
                float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
                inSampleSize = (int)ratio;
            }
            options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
            options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
            Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(bm);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            for(int i = 0;i < grantResults.length;i++){
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(getApplication(),"权限未申请",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}






