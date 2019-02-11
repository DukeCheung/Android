package com.example.zhangxing.storage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LikeDB extends SQLiteOpenHelper {

    private static final String DB_NAME= "LIKE.db";
    private static final String TABLE_NAME = "LIKETABLE";
    private static final int DB_VERSION = 1;

    public LikeDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY, username STRING, commentId INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
    }

    public long insert(String username, Integer commentId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("commentId", commentId);
        long rid = db.insert(TABLE_NAME,null,cv);
        db.close();
        return rid;
    }
    public int delete(String username, Integer commentId){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "username = ? and commentId = ?";
        String[] whereArgs = {username, commentId.toString()};
        int rows = db.delete(TABLE_NAME, whereClause,whereArgs);
        return rows;
    }
    public int deleteById(Integer commentId){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "commentId = ?";
        String[] whereArgs = {commentId.toString()};
        int rows = db.delete(TABLE_NAME, whereClause,whereArgs);
        return rows;
    }

    public int query(String username, Integer commentId){
        int m = 0;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ? and commentId = ?";
        String[] selectionArgs = {username, commentId.toString()};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs,null,null,null);
        if(c.moveToNext()){
            m = 1;
        }
        return m;
    }

}
