package com.example.zhangxing.storage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommentDB extends SQLiteOpenHelper {

    private static final String DB_NAME= "COMMENT.db";
    private static final String TABLE_NAME = "COMMENTTABLE";
    private static final int DB_VERSION = 1;

    public CommentDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY, username STRING, time STRING, comment STRING, count INTEGER, likes INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
    }

    public long insert(Comment m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", m.getUsername());
        cv.put("time", m.getTime());
        cv.put("comment", m.getComment());
        cv.put("count", m.getCount());
        cv.put("likes", m.getLike());
        long rid = db.insert(TABLE_NAME,null,cv);
        db.close();
        return rid;
    }
    public int deleteById(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {id.toString()};
        int rows = db.delete(TABLE_NAME, whereClause,whereArgs);
        return rows;
    }
    public int update(Comment m){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {m.getId().toString()};
        ContentValues cv = new ContentValues();
        cv.put("username", m.getUsername());
        cv.put("time", m.getTime());
        cv.put("comment", m.getComment());
        cv.put("count", m.getCount());
        cv.put("likes", m.getLike());
        int rows = db.update(TABLE_NAME,cv, whereClause,whereArgs);
        db.close();
        return rows;
    }

    public Comment getById(Integer id){
        Comment m = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "_id = ?";
        String[] selectionArgs = {id.toString()};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs,null,null,null);
        if(c.getCount()==0||!c.moveToFirst()){
            c.close();
            return null;
        }
        else{
            m = new Comment(c.getString(1),c.getString(2),c.getString(3),c.getInt(4),c.getInt(5));
            m.setId(c.getInt(0));
        }
        c.close();
        return m;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
