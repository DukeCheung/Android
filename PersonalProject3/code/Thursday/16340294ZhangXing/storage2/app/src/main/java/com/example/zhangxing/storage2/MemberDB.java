package com.example.zhangxing.storage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class MemberDB extends SQLiteOpenHelper {
    private static final String DB_NAME= "MEMBER.db";
    private static final String TABLE_NAME = "MEMBERTABLE";
    private static final int DB_VERSION = 1;

    public MemberDB(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + " ( username STRING PRIMARY KEY, password  STRING, phoneNumber STRING, portrait BLOG)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
    }

    public long insert(Member m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        m.getPortrait().compress(Bitmap.CompressFormat.PNG, 100, os);
        cv.put("username", m.getUsername());
        cv.put("password", m.getPassword());
        cv.put("phoneNumber", m.getPhoneNumber());
        cv.put("portrait", os.toByteArray());
        long rid = db.insert(TABLE_NAME,null,cv);
        db.close();
        return rid;
    }
    public int deleteByUsername(String username){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "username = ?";
        String[] whereArgs = {username};
        int rows = db.delete(TABLE_NAME, whereClause,whereArgs);
        return rows;
    }
    public int update(Member m){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "username = ?";
        String[] whereArgs = {m.getUsername()};
        ContentValues cv = new ContentValues();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        m.getPortrait().compress(Bitmap.CompressFormat.PNG, 100, os);
        cv.put("username", m.getUsername());
        cv.put("password", m.getPassword());
        cv.put("phoneNumber", m.getPhoneNumber());
        cv.put("portrait", os.toByteArray());
        int rows = db.update(TABLE_NAME,cv, whereClause,whereArgs);
        db.close();
        return rows;
    }

    public Member getByUsername(String username){
        Member m = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor c = db.query(TABLE_NAME, null, selection,selectionArgs,null,null,null);
        if(c.moveToNext()){
            byte[] in=c.getBlob(3);
            Bitmap bmpout= BitmapFactory.decodeByteArray(in,0,in.length);
            m = new Member(c.getString(0),c.getString(1),c.getString(2),bmpout);
        }
        c.close();
        return m;
    }
}
