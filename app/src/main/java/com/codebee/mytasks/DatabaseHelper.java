package com.codebee.mytasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME = "Tasks.db";
    public static final String TABLE_NAME = "tasks_table";
    public static final String COL_1 = "Id";
    public static final String COL_2 = "Label";
    public static final String COL_3 = "Description";
    public static final String COL_4 = "Date";
    public static final String COL_5 = "Time";
    public static final String COL_6 = "Timestamp";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_1 + " INTEGER PRIMARY KEY," +
                COL_2 + " TEXT," +
                COL_3 + " TEXT," +
                COL_4 + " TEXT," +
                COL_5 + " TEXT," +
                COL_6 + " INTEGER" +
                ");" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(MyTask myTask){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,myTask.getLabel());
        contentValues.put(COL_3,myTask.getDescription());
        contentValues.put(COL_4,myTask.getDate());
        contentValues.put(COL_5,myTask.getTime());
        contentValues.put(COL_6,myTask.getTimestamp());
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<MyTask> getData(){
        ArrayList<MyTask> myTaskArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TABLE_NAME,null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                MyTask myTask = new MyTask();
                myTask.setId(cursor.getString(cursor.getColumnIndex(COL_1)));
                myTask.setLabel(cursor.getString(cursor.getColumnIndex(COL_2)));
                myTask.setDescription(cursor.getString(cursor.getColumnIndex(COL_3)));
                myTask.setDate(cursor.getString(cursor.getColumnIndex(COL_4)));
                myTask.setTime(cursor.getString(cursor.getColumnIndex(COL_5)));
                myTask.setTimestamp(cursor.getLong(cursor.getColumnIndex(COL_6)));

                myTaskArrayList.add(myTask);
            }
        }
        return myTaskArrayList;
    }

    public int deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,COL_1 + " =?",new String[]{id});
    }

    public boolean updateData(MyTask myTask){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,myTask.getLabel());
        contentValues.put(COL_3,myTask.getDescription());
        contentValues.put(COL_4,myTask.getDate());
        contentValues.put(COL_5,myTask.getTime());
        contentValues.put(COL_6,myTask.getTimestamp());
        db.update(TABLE_NAME,contentValues,COL_1 + " =?",new String[]{myTask.getId()});
        return true;
    }
}
