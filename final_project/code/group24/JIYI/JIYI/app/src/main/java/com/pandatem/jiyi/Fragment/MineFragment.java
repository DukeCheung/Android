package com.pandatem.jiyi.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pandatem.jiyi.CircleImageView.CircleImageView;
import com.pandatem.jiyi.MainActivity;
import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.MyDB.Person;
import com.pandatem.jiyi.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MineFragment extends Fragment {

    private boolean isRegiter = true;
    private byte[] photo;

    public MineFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        //initial database
        final MyDatabase myDB = new MyDatabase(getContext());

        //default cover
        Resources res = getResources();
        Bitmap temp = BitmapFactory.decodeResource(res,R.mipmap.img);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        temp.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        photo = byteStream.toByteArray();

        //initial views
        final ConstraintLayout layout1 = (ConstraintLayout)view.findViewById(R.id.lay1);
        final ConstraintLayout layout2 = (ConstraintLayout)view.findViewById(R.id.lay2);
        final com.rey.material.widget.Button okButton = (com.rey.material.widget.Button)view.findViewById(R.id.okBtn);
        final com.rey.material.widget.Button rstButton = (com.rey.material.widget.Button)view.findViewById(R.id.rstBtn);
        final CircleImageView chooseImage = (CircleImageView)view.findViewById(R.id.chooseImage);
        final EditText editUsername = (EditText)view.findViewById(R.id.username);
        final EditText editPassword = (EditText)view.findViewById(R.id.password);
        final EditText editComfirmPassword = (EditText)view.findViewById(R.id.comfirmPassword);
        final CircleImageView userCover = (CircleImageView)view.findViewById(R.id.img_user_cover);
        final TextView username = (TextView)view.findViewById(R.id.tv_user_name);
        final com.rey.material.widget.Button quitBtn = (com.rey.material.widget.Button)view.findViewById(R.id.quitBtn);
        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        final RadioButton registerBtn = (RadioButton)view.findViewById(R.id.registerBtn);
        registerBtn.setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(isRegiter){
                    editPassword.setHint("Password");
                    editComfirmPassword.setVisibility(View.GONE);
                    chooseImage.setVisibility(View.INVISIBLE);
                    editPassword.setText("");
                    isRegiter = false;
                }else{
                    editPassword.setHint("New Password");
                    editComfirmPassword.setVisibility(View.VISIBLE);
                    chooseImage.setVisibility(View.VISIBLE);
                    editPassword.setText("");
                    editComfirmPassword.setText("");
                    isRegiter = true;
                }

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRegiter){
                    if(editUsername.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Username cannot be empty.",Toast.LENGTH_SHORT).show();
                    }
                    else if(editPassword.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Password cannot be empty.",Toast.LENGTH_SHORT).show();
                    }
                    else if(!editPassword.getText().toString().equals(editComfirmPassword.getText().toString())){
                        Toast.makeText(getContext(),"Password Mismatch.",Toast.LENGTH_SHORT).show();
                    }
                    else if(myDB.userExist(editUsername.getText().toString())){
                        Toast.makeText(getContext(),"Username already existed.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Person user = new Person(editUsername.getText().toString(),editPassword.getText().toString(),photo);
                        myDB.insertUser(user);
                        Toast.makeText(getContext(),"Register successfully.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(editUsername.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Username cannot be empty.",Toast.LENGTH_SHORT).show();
                    }
                    else if(editPassword.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Password cannot be empty.",Toast.LENGTH_SHORT).show();
                    }
                    else if(!myDB.userExist(editUsername.getText().toString())){
                        Toast.makeText(getContext(),"Username not existed.",Toast.LENGTH_SHORT).show();
                    }
                    else if(!myDB.getPassword(editUsername.getText().toString()).equals(editPassword.getText().toString())){
                        Toast.makeText(getContext(),"Invalid password.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        byte[] cover = myDB.getCover(editUsername.getText().toString());
                        username.setText(editUsername.getText().toString());
                        userCover.setImageBitmap(BitmapFactory.decodeByteArray(cover, 0,cover.length));
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        ((MainActivity)getActivity()).setGlobalUsername(editUsername.getText().toString());

                    }
                }
            }
        });

        rstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUsername.setText("");
                editPassword.setText("");
                editComfirmPassword.setText("");
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                editPassword.setText("");
                Resources res = getResources();
                userCover.setImageBitmap(BitmapFactory.decodeResource(res,R.mipmap.add));
                ((MainActivity)getActivity()).setGlobalUsername("");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data != null){
            Uri uri = data.getData();
            CircleImageView chosenCover = (CircleImageView)getActivity().findViewById(R.id.chooseImage);
            try {
                Bitmap bitmap = getBitmapFormUri(getActivity(),uri);
                chosenCover.setImageBitmap(bitmap);
                //bitmap to byteArray
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
                photo = byteStream.toByteArray();
            }catch (Exception e){

            }
        }
    }

    //resolve the image from uri
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    //compress the image
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
