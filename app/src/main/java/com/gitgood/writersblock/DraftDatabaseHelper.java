package com.gitgood.writersblock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DraftDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "drafts";
    private static final int DB_VERSION = 1;

    DraftDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        System.out.println("Creating database");
        db.execSQL( "CREATE TABLE DRAFT_GENERAL ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TITLE TEXT,"
                +  "DATE DATE);");

        db.execSQL( "CREATE TABLE DRAFT_CONTENT ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "GENERAL_ID INTEGER, "
                + "CONTENT TEXT,"
                + "CONSTRAINT fk_GENERAL_ID "
                + "FOREIGN KEY (GENERAL_ID) "
                + "REFERENCES DRAFT_GENERAL(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void insertDraft(String adj_noun, String date, String content){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues generalValues = new ContentValues();
        ContentValues contentValues = new ContentValues();

        generalValues.put("TITLE", adj_noun);
        generalValues.put("DATE", date);

        contentValues.put("CONTENT", content);

        db.insert("DRAFT_GENERAL", null, generalValues);
        db.insert("DRAFT_CONTENT", null, contentValues);
    }

    public ArrayList<String> getAllTitles(){
        ArrayList<String> result = new ArrayList<String>();
        String selectQuery = "SELECT * FROM DRAFT_GENERAL";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(cursor.getColumnIndex("TITLE")));
            }while(cursor.moveToNext());
        }

        return result;
    }


}
