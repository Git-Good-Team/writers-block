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

    /**
     * Given the {@param adj_noun} {@param content}, instert data into DRAFT_GENERAL and DRAFT_CONTENT tables.
     */
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



    /**
     * Returns a cursor object that has the values _id and TITLE from the DRAFT_GENERAL table.
     * Primarily used to generate list of drafts in DraftsListActivity.java.
     */
    public Cursor getAllTitlesCursor(){
        String selectQuery = "SELECT _id, TITLE FROM DRAFT_GENERAL";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        return cursor;
    }



    /**
     * Returns the current time and date in yyyy-MM-dd HH:mm:ss format.
     * Used to store the current time a draft was written / stored.
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Takes in the {@param key} to identify which data is to be returned.
     * Returns the title and content from the given key.
     */

    public String[] getContent(long key){
        String[] result = new String[2];
        String id = key+"";
        String selectQuery = "SELECT TITLE, CONTENT FROM DRAFT_CONTENT LEFT JOIN DRAFT_GENERAL "
            + "ON DRAFT_CONTENT._id=DRAFT_GENERAL._id WHERE DRAFT_CONTENT._id="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

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

    /**
     * Prints all items within the given column.
     * Does not have any functionality for the app itself.
     */
    private void printCursor(Cursor cursor, String column){
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(column));

                System.out.println(name);
                cursor.moveToNext();
            }
        }
    }

    /**
     * Returns an ArrayList of all titles from the DRAFT_GENERAL table.
     * Used to store the current time a draft was written / stored.
     *
     * Does not have any functionality for the app itself.
     */
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

    /**
     * Returns the current time and date in yyyy-MM-dd HH:mm:ss format.
     * Used to store the current time a draft was written / stored.
     *
     * Does not have actual functionality on its own.
     */
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

}
