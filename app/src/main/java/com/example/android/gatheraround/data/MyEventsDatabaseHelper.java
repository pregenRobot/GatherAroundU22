package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;



/**
 * Created by tamimazmain on 2017/09/08.
 */

public class MyEventsDatabaseHelper extends SQLiteOpenHelper {

    private static String COL_ID="_id";
    private static String COL_GLOBAL_ID="GLOBAL_ID";
    private static final String DATABASE_NAME="MyeventList.db";
    private static final String TABLE_NAME="myevent_table";
    private static final int DB_VERSION = 1;
    private Context mContext;

    public MyEventsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_GLOBAL_ID +" TEXT)";
        sqLiteDatabase.execSQL(createTable);
        Log.v("MyEvents DatabaseHelper",createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    boolean addData(String key){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db==null){
            Log.v("Database","Failed to create");
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GLOBAL_ID, key);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean checkforExistingEvent(String toCheck){
        ArrayList<String> returner = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, TABLE_NAME, new String[]{COL_ID,COL_GLOBAL_ID},null,null,null,null,null,null);
        Log.v("MyEventsDatabaseCursor",mCursor.toString());

        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            returner.add(mCursor.getString(mCursor.getColumnIndex(COL_GLOBAL_ID)));
            Log.v("MyEventsDatabase","Searching Through myGlobal IDs" + mCursor.getString(mCursor.getColumnIndex(COL_GLOBAL_ID)));
        }
        boolean checker = false;
        for(String x:returner){
            Log.v("MyEventsDatabase","Now Checking: " + x);
            if(x.equals(toCheck)){
                checker = true;
                Log.v("Checker","True!");
            }else{
                Log.v("Checker","False!");
            }
        }
        return checker;
    }
}
