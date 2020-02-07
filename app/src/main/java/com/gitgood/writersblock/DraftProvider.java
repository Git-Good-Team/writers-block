package com.gitgood.writersblock;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class DraftProvider extends android.content.ContentProvider {

    static final String PROVIDER_NAME = "com.gitgood.writersblock.DraftProvider";
    static final String URL = "content://"+PROVIDER_NAME+"/drafts";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String ADJECTIVE = "adjective";
    static final String NOUN = "noun";
    static final String DATE = "date";



    private static HashMap<String,String> DRAFT_PROJECTION_MAP;
    static final int DRAFT = 1;
    static final int DRAFT_ID = 2;

    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"drafts",DRAFT);
        uriMatcher.addURI(PROVIDER_NAME,"drafts/#",DRAFT_ID);
    }


    /**
     * Declarations for Database
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Writers_Block";
    static final String DATABASE_TABLE_MAIN = "Draft_basic_info";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_TABLE_MAIN =
            "CREATE TABLE " + DATABASE_TABLE_MAIN +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " adjective TEXT NOT NULL, " +
                    " noun TEXT NOT NULL, " +
                    " date NUMERIC NOT NULL);";


    /**
     * Writing a helper class to create and manage the DraftProvider's data repository
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TABLE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_MAIN);
            onCreate(db);
        }
    }



    @Override
    public boolean onCreate(){

        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create database if it doesn't already exist
         */

        db = dbHelper.getWritableDatabase();

        if (db != null){
            return true;
        }

        return false;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DATABASE_TABLE_MAIN);

        switch(uriMatcher.match(uri)){
            case DRAFT:
                qb.setProjectionMap(DRAFT_PROJECTION_MAP);
                break;
            case DRAFT_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * Add a new Draft to database
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(DATABASE_TABLE_MAIN, "", values);

        //if record added successfully
        if (rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }

        //if record add was unsuccessful
        throw new SQLException("Failed to add Draft into "+uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
