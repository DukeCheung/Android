package com.pandatem.jiyi.MyDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pandatem.jiyi.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME= "myDatabase";
    private static final int DB_VERSION = 1;
    private static final String USER_TABLE_NAME = "user_table";
    private static final String CARD_TABLE_NAME = "card_table";
    private static final String MESSAGE_TABLE_NAME = "message_table";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public MyDatabase(Context c){
        super(c, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE if not exists " + USER_TABLE_NAME +
                " (username TEXT PRIMARY KEY, password TEXT, photo BLOB)";

        db.execSQL(CREATE_USER_TABLE);

        String CREATE_CARD_TABLE = "CREATE TABLE if not exists " + CARD_TABLE_NAME +
                " (id INTEGER PRIMARY KEY, cover TEXT, title TEXT, content TEXT, " +
                "username TEXT, latLng_x REAL, latLng_y REAL, position TEXT, " +
                "isPrivate TEXT, coverBitmapBytes BLOB)";
        db.execSQL(CREATE_CARD_TABLE);

        String CREATE_MESSAGE_TABLE = "CREATE TABLE if not exists " + MESSAGE_TABLE_NAME +
                " (id INTEGER PRIMARY KEY, sender TEXT, receiver TEXT, message TEXT, " +
                "time TEXT)";
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertUser(Person user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", user.getName());
        values.put("password", user.getPassword());
        values.put("photo", user.getCoverBitmapBytes());

        long rid = db.insert(USER_TABLE_NAME,null, values);
        db.close();
        return rid;
    }

    public boolean userExist(String username){
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(USER_TABLE_NAME, null, selection, selectionArgs,null,null,null);
        boolean isExisted = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isExisted;
    }

    public String getPassword(String username){
        SQLiteDatabase db = getWritableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(USER_TABLE_NAME, null, selection, selectionArgs,null,null,null);
        if(cursor.moveToFirst()){
            String password = cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();
            db.close();
            return password;
        }else{
            cursor.close();
            db.close();
            return "";
        }
    }

    public byte[] getCover(String username){
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String [] selectionArgs = {username};
        Cursor cursor = db.query(USER_TABLE_NAME, null,selection,selectionArgs,null,null,null);

        if(cursor.moveToFirst()){
            byte[] in = cursor.getBlob(cursor.getColumnIndex("photo"));
            cursor.close();
            db.close();
            return in;
        }
        cursor.close();
        db.close();
        return null;
    }

    public long insertCard(Card card){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("cover", card.getCover());
        values.put("title", card.getTitle());
        values.put("content", card.getContent());
        values.put("username", card.getPerson().getName());
        values.put("latLng_x", card.getLatLng_x());
        values.put("latLng_y", card.getLatLng_y());
        values.put("position", card.getPosition());
        values.put("isPrivate", card.getPrivate());
        values.put("coverBitmapBytes", card.getCoverBitmapBytes());

        long rid = db.insert(CARD_TABLE_NAME,null, values);
        db.close();
        return rid;
    }

    public List<Card> queryAllCard(){
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = "isPrivate = ?";
        String [] selectionArgs = {"0"};
        Cursor cursor = db.query(CARD_TABLE_NAME, null, selection, selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            String username = cursor.getString(4);
            Person person = new Person(username,getPassword(username),getCover(username)) ;
            Card card = new Card(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3), person,cursor.getDouble(5),cursor.getDouble(6),
                    cursor.getString(7),Boolean.getBoolean(cursor.getString(8)),cursor.getBlob(9));
            cards.add(card);
        }
        cursor.close();
        db.close();
        return cards;
    }

    public List<Card> queryCardByUser(String username){
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ? or isPrivate = ?";
        String [] selectionArgs = {username,"0"};
        Cursor cursor = db.query(CARD_TABLE_NAME, null, selection, selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            Person person = new Person(cursor.getString(4),getPassword(cursor.getString(4)),getCover(cursor.getString(4))) ;
            Card card = new Card(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3), person,cursor.getDouble(5),cursor.getDouble(6),
                    cursor.getString(7),Boolean.getBoolean(cursor.getString(8)),cursor.getBlob(9));
            cards.add(card);
        }
        cursor.close();
        db.close();
        return cards;
    }

    public Card queryCardById(Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String selection = "id = ?";
        String [] selectionArgs = {id.toString()};
        Cursor cursor = db.query(CARD_TABLE_NAME, null, selection, selectionArgs,null,null,null);
        if (cursor.moveToNext()){
            String username = cursor.getString(4);
            Person person = new Person(username,getPassword(username),getCover(username)) ;
            Card card = new Card(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3), person,cursor.getDouble(5),cursor.getDouble(6),
                    cursor.getString(7),Boolean.getBoolean(cursor.getString(8)),cursor.getBlob(9));
            cursor.close();
            db.close();
            return card;
        }
        cursor.close();
        db.close();
        return null;
    }

    public void insertMessage(Message message){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("sender", message.getSender());
        values.put("receiver", message.getReceiver());
        values.put("message", message.getMessage());
        values.put("time", formatter.format(message.getTime()));

        db.insert(MESSAGE_TABLE_NAME,null, values);
        db.close();
    }

    public List<Message> queryAllMessages(String username){
        SQLiteDatabase db = getReadableDatabase();

        List<Message> messages = new ArrayList<>(); //搜索与某个用户有关的来信
        String selection = "receiver = ? or sender = ?";
        String [] selectionArgs = {username,username};
        Cursor cursor = db.query(MESSAGE_TABLE_NAME, null,selection,selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            Message message = null;
            try {
                message = new Message(cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),  formatter.parse(cursor.getString(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            messages.add(message);
        }
        cursor.close();
        db.close();
        return messages;
    }

    public List<Message> queryConversation(String sender,String receiver){
        SQLiteDatabase db = getReadableDatabase();

        List<Message> messages = new ArrayList<>(); //搜索双方的来信
        String selection = "receiver = ? and sender = ? or receiver = ? and sender = ?";
        String [] selectionArgs = {receiver,sender,sender,receiver};
        Cursor cursor = db.query(MESSAGE_TABLE_NAME, null,selection,selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            Message message = null;
            try {
                message = new Message(cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),  formatter.parse(cursor.getString(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            messages.add(message);
        }
        cursor.close();
        db.close();
        return messages;
    }
}
