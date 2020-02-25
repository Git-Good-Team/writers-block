package com.gitgood.writersblock;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                +  "DATE TEXT);");

        db.execSQL( "CREATE TABLE DRAFT_CONTENT ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "GENERAL_ID INTEGER, "
                + "TITLE TEXT, "
                + "CONTENT TEXT,"
                + "CONSTRAINT fk_GENERAL_ID "
                + "FOREIGN KEY (GENERAL_ID) "
                + "REFERENCES DRAFT_GENERAL(_id));");

        insertDraft(db, "Angry Mob","2020-01-30", "Lorem Ipsum");
        insertDraft(db, "Hello World","2000-02-30", "Lorem Ipsum the sure");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public static void insertDraft(SQLiteDatabase db, String adj_noun, String date, String content){
        ContentValues generalValues = new ContentValues();
        ContentValues contentValues = new ContentValues();

        generalValues.put("TITLE", adj_noun);
        generalValues.put("DATE", date);

        contentValues.put("TITLE", adj_noun);
        contentValues.put("CONTENT", content);

        db.insert("DRAFT_GENERAL", null, generalValues);
        db.insert("DRAFT_CONTENT", null, contentValues);
    }


}
