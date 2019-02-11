package com.pandatem.jiyi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandatem.jiyi.CircleImageView.CircleImageView;
import com.pandatem.jiyi.MyDB.Card;
import com.pandatem.jiyi.MyDB.MyDatabase;

public class CardDetailActivity extends AppCompatActivity {
    private Card mCard;
    private MyDatabase myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        myDB = new MyDatabase(this);

        Integer id =(Integer)getIntent().getSerializableExtra("cardId");
        mCard =  myDB.queryCardById(id);
        com.rey.material.widget.ImageView img_cover  = (com.rey.material.widget.ImageView)findViewById(R.id.detail_img_cover);
        CircleImageView img_user_cover=(CircleImageView)findViewById(R.id.detail_img_person_cover);
        TextView tv_content = (TextView)findViewById(R.id.detail_tv_content);
        TextView tv_position = (TextView)findViewById(R.id.detail_tv_position);
        TextView tv_user_name=(TextView)findViewById(R.id.detail_tv_person_name);

        tv_content.setText(mCard.getContent());
        if(mCard.getPosition().length() > 12) {
            tv_position.setText(mCard.getPosition().substring(0, 11));
        }else {
            tv_position.setText(mCard.getPosition());

        }
        tv_user_name.setText(mCard.getPerson().getName());

        byte bytes1[] = mCard.getPerson().getCoverBitmapBytes();
        if(bytes1 != null) {
            Bitmap bmp1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            if(bmp1 !=null ){
                img_user_cover.setImageBitmap(bmp1);
            }else{
                img_user_cover.setImageResource(R.mipmap.user);
            }
        }else{
            img_user_cover.setImageResource(R.mipmap.user);
        }

        byte bytes2[] = mCard.getCoverBitmapBytes();
        if(bytes2 != null) {
            Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length);
            if(bmp2 != null){
                img_cover.setImageBitmap(bmp2);

            }else {
                img_cover.setImageResource(R.mipmap.demo);
            }
        }else {
            img_cover.setImageResource(R.mipmap.demo);
        }


        img_user_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), ConversationActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("username",card.getPerson().getName());
//                        intent.putExtras(bundle);
//                        startActivity(intent);
            }
        });

    }


}
