package com.gitgood.writersblock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DraftDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "drafts";
    private static final int DB_VERSION = 1;

    DraftDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL( "CREATE TABLE DRAFT_GENERAL ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TITLE TEXT,"
                + "DATE DATETIME);");

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

    public long insertDraft(String adj_noun, String content){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues generalValues = new ContentValues();
        ContentValues contentValues = new ContentValues();

        generalValues.put("TITLE", adj_noun);
        generalValues.put("DATE", getDateTime());

        contentValues.put("CONTENT", content);

        long id = db.insert("DRAFT_GENERAL", null, generalValues);
        db.insert("DRAFT_CONTENT", null, contentValues);
        return id;
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

    public Cursor getAllTitlesCursor(){
        String selectQuery = "SELECT _id, TITLE FROM DRAFT_GENERAL";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        return cursor;
    }

    public ArrayList<String> getAllDates(){
        ArrayList<String> result = new ArrayList<String>();
        String selectQuery = "SELECT * FROM DRAFT_GENERAL";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                result.add(cursor.getString(cursor.getColumnIndex("DATE")));
            }while(cursor.moveToNext());
        }
        db.close();

        return result;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String[] getContent(int key){
        System.out.println("--------------------printing cursor------------");
        String[] result = new String[2];
        String id = key+"";
        String selectQuery = "SELECT TITLE, CONTENT FROM DRAFT_CONTENT LEFT JOIN DRAFT_GENERAL "
            + "ON DRAFT_CONTENT._id=DRAFT_GENERAL._id WHERE DRAFT_CONTENT._id="+id;
//JOIN DRAFT_CONTENT ON DRAFT_GENERAL._id=DRAFT_CONTENT.GENERAL_ID WHERE DRAFT_GENERAL._id = "+id
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
//        printCursor(cursor,"TITLE");
//        printCursor(cursor,"CONTENT");
        if (cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String content = cursor.getString(cursor.getColumnIndex(("CONTENT")));

            System.out.println(title);
            System.out.println(content);

            result[0] = title;
            result[1] = content;

        }



        return result;
    }

    private void printCursor(Cursor cursor, String column){
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(column));

                System.out.println(name);
                cursor.moveToNext();
            }
        }
    }


}
