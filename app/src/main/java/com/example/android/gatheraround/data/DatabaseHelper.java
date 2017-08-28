package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.gatheraround.custom_classes.Participants;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventList.db";
    private static final String TABLE_NAME = "event_table";
    public static final String COL1 = "ID";
    private static final String COL2 = "EVENTNAME";
    private static final String COL3 = "UNIXTIMESTAMP";
    private static final String COL4 = "PARTICIPANTS";
    private static final String COL5 = "LOCATION";
    private static final String COL6 = "LOCATIONNAME";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1/**version**/);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " EVENTNAME TEXT, UNIXTIMESTAMP INTEGER, PARTICIPANTS TEXT, LOCATION TEXT, LOCATIONNAME TEXT)";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String event_name, long unixtime, Participants participants, LatLng location, String locationName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Gson gson = new Gson();
        String gsonParticipants = gson.toJson(participants,Participants.class);
        String gsonLocation = gson.toJson(location,LatLng.class);

        contentValues.put(COL2,event_name);
        contentValues.put(COL3, unixtime);
        contentValues.put(COL4, gsonParticipants);
        contentValues.put(COL5, gsonLocation);
        contentValues.put(COL6, locationName);

        long result  = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }
    public Cursor showData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

}
