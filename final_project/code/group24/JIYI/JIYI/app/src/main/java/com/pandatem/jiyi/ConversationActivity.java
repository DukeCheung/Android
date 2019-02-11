package com.pandatem.jiyi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pandatem.jiyi.MyDB.MyDatabase;
import com.pandatem.jiyi.MyDB.Person;
import com.pandatem.jiyi.RecycleView.HomeRecycleViewAdapter;
import com.pandatem.jiyi.RecycleView.HomeViewHolder;

import org.w3c.dom.ls.LSException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationActivity extends AppCompatActivity{
    private HomeRecycleViewAdapter myAdapter;
    private List<Message> messages;
    private RecyclerView recyclerView;
    private String sender;
    private String receiver;
    private Button send;
    private TextView content, username;
    private ImageView back;
    private Bitmap senderPhoto, receiverPhoto;
    private MyDatabase db;
    private Message lastMsg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        List<String> users = (List<String>) this.getIntent().getSerializableExtra("users");
        messages = new ArrayList<Message>();
        content = (TextView)findViewById(R.id.conversation_input);
        send = (Button)findViewById(R.id.conversation_send);
        username = (TextView)findViewById(R.id.conversation_receiver);
        back = (ImageView) findViewById(R.id.conversation_back);
        sender = users.get(0);
        receiver = users.get(1);
        db = new MyDatabase(getApplication());
        byte[] cover1, cover2;
        cover1 = db.getCover(sender);
        cover2 = db.getCover(receiver);
        if(cover1==null||cover2==null){
//            Toast.makeText(getApplicationContext(),"User doesn't exist.",Toast.LENGTH_SHORT).show();
//            finish();
            receiver = "aaa";
            cover2 = db.getCover(receiver);
        }
        else{
            senderPhoto = BitmapFactory.decodeByteArray(cover1, 0, cover1.length);
            receiverPhoto = BitmapFactory.decodeByteArray(cover2,0,cover2.length);
        }

        initRecyclerView();
        setListener();
        getRecord();
    }
    public void setListener(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = content.getText().toString();
                if(!str.isEmpty()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate =  new Date(System.currentTimeMillis());
                    Message m = new Message(sender,receiver,str,curDate);
                    lastMsg = m;
                    messages.add(m);
                    myAdapter.notifyDataSetChanged();
                    db.insertMessage(m);
                    content.setText("");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("lastMsg", (Serializable) lastMsg);
                setResult(1, intent);
                finish();
            }
        });
        username.setText(receiver);
    }
    public void initRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.conversation_message);
        myAdapter = new HomeRecycleViewAdapter<Message>(ConversationActivity.this, R.layout.item_message,messages) {
            @Override
            public void convert(HomeViewHolder holder, Message m) {
                if(m.getSender().equals(sender)){
                    ImageView photo = holder.getView(R.id.message_sender1);
                    TextView content = holder.getView(R.id.message_content1);
                    content.setText(m.getMessage());
                    photo.setImageBitmap(senderPhoto);
                }else{
                    ConstraintLayout constraintLayout1 = holder.getView(R.id.message_fromSender);
                    ConstraintLayout constraintLayout2 = holder.getView(R.id.message_fromReceiver);
                    constraintLayout1.setVisibility(View.GONE);
                    constraintLayout2.setVisibility(View.VISIBLE);
                    ImageView photo = holder.getView(R.id.message_sender2);
                    TextView content = holder.getView(R.id.message_content2);
                    content.setText(m.getMessage());
                    photo.setImageBitmap(receiverPhoto);
                }
            }
        };
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void getRecord(){
        List<Message> temp = db.queryConversation(sender,receiver);
        if(temp!=null){
            for(int i = 0;i < temp.size();i++ ){
                messages.add(temp.get(i));
            }
        }
        int length = messages.size()-1;
        if(length>-1){
            lastMsg = messages.get(length);
        }
        else{
            lastMsg = null;
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("lastMsg", (Serializable) lastMsg);
        setResult(1, intent);
        finish();
    }
}
