package com.example.zhangxing.storage2;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity{

    private List<Comment> data;
    private MyListViewAdapter myListViewAdapter;
    private CommentDB commentDB;
    private MemberDB memberDB;
    private LikeDB likeDB;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentDB = new CommentDB(getApplicationContext());
        memberDB = new MemberDB(getApplicationContext());
        likeDB = new LikeDB(getApplicationContext());
        username = this.getIntent().getStringExtra("username");
        data = new ArrayList<Comment>();
        int count = commentDB.getProfilesCount();
        int commentNumber = 1;
        for(int i = 0;i < count;commentNumber++){
            Comment c = commentDB.getById(commentNumber);
            if(c!=null){
                Comment comment = new Comment(c.getUsername(),c.getTime(),c.getComment(),c.getCount(),c.getLike());
                comment.setId(c.getId());
                if(likeDB.query(username, commentNumber)==1){
                    comment.setLike(1);
                }
                else{
                    comment.setLike(0);
                }
                data.add(comment);
                i++;
            }
        }

        myListViewAdapter = new MyListViewAdapter(CommentActivity.this, data,memberDB);
        final ListView listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommentActivity.this);
                if(commentDB.getById(data.get(i).getId()).getUsername().equals(username)){
                    alertDialog.setMessage("Delete or not?").setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    likeDB.deleteById(data.get(position).getId());
                                    commentDB.deleteById(data.get(position).getId());
                                    data.remove(position);
                                    myListViewAdapter.refresh(data);
                                }
                            }).setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                }
                else{
                    alertDialog.setMessage("Report or not?").setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                }

                alertDialog.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommentActivity.this);
                String[] selectArgs = {data.get(position).getUsername()};
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?", selectArgs, null);
                String number = null;
                if(cursor.getCount()!=0){
                    cursor.moveToFirst();
                    number = "\nPhone: ";
                    do {
                        String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        number +=  phone.substring(0,3)+ " " + phone.substring(3, 7)+ " "+
                                phone.substring(7,11)+ "\n             ";
                    } while (cursor.moveToNext());
                }
                else{
                    number = "\nPhone number not exist.";
                }
                alertDialog.setTitle("Info").setMessage("Username: "+ data.get(position).getUsername()+number).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });
        listView.setAdapter(myListViewAdapter);
        myListViewAdapter.setOnItemLikeClickListener(new MyListViewAdapter.onItemLikeListener() {
            @Override
            public void onLikeClick(int i) {
                if(data.get(i).getLike() == 0){
                    data.get(i).setLike(1);
                    Integer integer = data.get(i).getCount();
                    data.get(i).setCount(integer+1);
                    likeDB.insert(username, data.get(i).getId());
                }
                else{
                    data.get(i).setLike(0);
                    Integer integer = data.get(i).getCount();
                    data.get(i).setCount(integer-1);
                    likeDB.delete(username, data.get(i).getId());
                }

                commentDB.update(data.get(i));
                myListViewAdapter.refresh(data);
            }
        });

        Button send = (Button)findViewById(R.id.send);
        final EditText editText = (EditText)findViewById(R.id.comment);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplication(), "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate =  new Date(System.currentTimeMillis());
                    String time = simpleDateFormat.format(curDate);
                    Comment comment = new Comment(username, time, editText.getText().toString(),0, 0);
                    long l = commentDB.insert(comment);
                    int i = (int)l;
                    comment.setId(commentDB.getById(i).getId());
                    data.add(comment);
                    editText.setText("");
                    myListViewAdapter.refresh(data);
                }
            }
        });
    }
}