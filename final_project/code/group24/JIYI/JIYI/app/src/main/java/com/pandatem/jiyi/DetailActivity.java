package com.pandatem.jiyi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandatem.jiyi.MyDB.Card;
import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.MyDB.Person;

import java.io.ByteArrayOutputStream;

public class DetailActivity extends AppCompatActivity {
    private TextView submit, pickPermission;
    private Card card;
    private ImageView pickImage, back, image, person;
    private EditText content;
    private MyDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        card =(Card)getIntent().getSerializableExtra("card");
        findView();
        setListener();
        myDB = new MyDatabase(this);
    }

    private void findView(){
        submit = (TextView)findViewById(R.id.submit);
        pickPermission = (TextView)findViewById(R.id.pickPermission);
        pickImage = (ImageView)findViewById(R.id.pickImage);
        back = (ImageView)findViewById(R.id.back);
        image = (ImageView)findViewById(R.id.image);
        person = (ImageView)findViewById(R.id.person);
        content = (EditText)findViewById(R.id.content);
    }

    private void setListener(){
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);
                String str = pickPermission.getText().toString();
                final String permission;
                if(str.equals("公开")){
                    permission = "私人";
                }
                else {
                    permission = "公开";
                }
                alertDialog.setTitle("权限设置").setMessage("确定更改为"+permission+"吗？").setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pickPermission.setText(permission);
                                if(permission.equals("公开")){
                                    card.setPrivate(false);
                                }else{
                                    card.setPrivate(true);
                                }
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to be continued
                card.setContent(content.getText().toString());
                card.setId((int) myDB.insertCard(card));
                Intent intent = new Intent();
                intent.putExtra("card",card);
                setResult(1, intent);
                finish();
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(v);
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
            if(minLen > 80) { // 如果原始图像的最小边长大于100dp
                float ratio = (float)minLen / 80.0f; // 计算像素压缩比例
                inSampleSize = (int)ratio;
            }
            options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
            options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
            Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
            image.setVisibility(View.VISIBLE);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setImageBitmap(bm);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes=baos.toByteArray();
            card.setCoverBitmapBytes(bytes);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
