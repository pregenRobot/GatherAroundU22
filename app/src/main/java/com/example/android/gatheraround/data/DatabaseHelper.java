package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.gatheraround.DataSenderToServer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import com.example.android.gatheraround.custom_classes.Events;

import java.util.ArrayList;

import static com.example.android.gatheraround.data.MyEventsDatabaseHelper.COL_GLOBAL_ID;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "eventList.db";
    public static final String TABLE_NAME = "event_table";
    public static final String COL_LOCALID = "_id";
    // id for identifying within database
    public static final String COL_NAME = "EVENTNAME";
    public static final String COL_UNIXTIME = "UNIXTIMESTAMP";
    public static final String COL_PARTICIPANTS = "PARTICIPANTS";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_LOCATIONNAME = "LOCATIONNAME";
    public static final String COL_SUMMARY = "SUMMARY";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_GLOBALID = "GLOBALID";
    // id for identifying on the server
    DataSenderToServer dataSenderToServer = new DataSenderToServer();
    public static final int DB_VERSION = 1;
    MyEventsDatabaseHelper myevents = new MyEventsDatabaseHelper(context);


    private static final String[] ALL_COLUMNS = new String[]{
            COL_LOCALID,COL_NAME,COL_UNIXTIME,COL_PARTICIPANTS,COL_LOCATION,COL_LOCATIONNAME,COL_SUMMARY,COL_CATEGORY,COL_GLOBALID
    };

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION/**version**/);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( " +
                COL_LOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT," +
                COL_UNIXTIME + " INTEGER," +
                COL_PARTICIPANTS + " INTEGER," +
                COL_LOCATION + " TEXT," +
                COL_LOCATIONNAME + " TEXT," +
                COL_SUMMARY + " TEXT," +
                COL_CATEGORY + " TEXT," +
                COL_GLOBALID + ")";
        sqLiteDatabase.execSQL(createTable);
        Log.v("DatabaseHelper","Database Created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String event_name, long unixtime, int participants, LatLng location, String locationName,String summary, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Gson gson = new Gson();

        String gsonLocation = gson.toJson(location,LatLng.class);

        contentValues.put(COL_NAME,event_name);
        contentValues.put(COL_UNIXTIME, unixtime);
        contentValues.put(COL_PARTICIPANTS, participants);
        contentValues.put(COL_LOCATION, gsonLocation);
        contentValues.put(COL_LOCATIONNAME, locationName);
        contentValues.put(COL_SUMMARY, summary);
        contentValues.put(COL_CATEGORY, category);

        Events newEvents = new Events(unixtime,event_name,participants,location,locationName,summary, category, "contemporary");
        String key = dataSenderToServer.pushToServer(newEvents);

        contentValues.put(COL_GLOBALID, key);
        Log.v("AddedData",contentValues.toString());

        dataSenderToServer.addOneParticipants(key);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean addParticipant(Events events){
        long result=-1;
        if(!this.checkforExistingEvent(events.getGlobalId())){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            Gson gson = new Gson();
            String gsonLocation = gson.toJson(events.getLocation(),LatLng.class);
            contentValues.put(COL_NAME,events.getName());
            contentValues.put(COL_UNIXTIME, events.getUnixTimeStamp());
            contentValues.put(COL_PARTICIPANTS, events.getParticipants());
            contentValues.put(COL_LOCATION, gsonLocation);
            contentValues.put(COL_LOCATIONNAME, events.getLocationName());
            contentValues.put(COL_SUMMARY, events.getEventSummary());
            contentValues.put(COL_CATEGORY, events.getCategory());
            contentValues.put(COL_GLOBALID, events.getGlobalId());
            dataSenderToServer.addOneParticipants(events.getGlobalId());
            result = db.insert(TABLE_NAME, null, contentValues);
        }
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db == null){
            Log.v("SQLITE DATABASE","NULL!");
        }else{
            Log.v("SQLITE DATABASE","NOT NULL!");
        }
        String where = null;
        Cursor c = db.query(true, TABLE_NAME, ALL_COLUMNS,where,null,null,null,/** COL_NAME + " ASC"**/null,null);
        return c;

    }
    public boolean checkforExistingEvent(String toCheck){
        ArrayList<String> returner = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, TABLE_NAME, ALL_COLUMNS,null,null,null,null,/** COL_NAME + " ASC"**/null,null);
        Log.v("EventCursor",mCursor.toString());

        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            returner.add(mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)));
            Log.v("Database","Searching Through registered IDs" + mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)));
        }
        boolean checker = false;
        for(String x:returner){
            Log.v("Database","Now Checking: " + x);
            if(x.equals(toCheck)){
                checker = true;
                Log.v("Checker","True!");
            }else{
                Log.v("Checker","False!");
            }
        }
        return checker;
    }


    public ArrayList<String> getAllGlobalId(){
        ArrayList<String> returner = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String where = null;
        Cursor mCursor = db.query(true, TABLE_NAME, new String[]{COL_GLOBALID},where,null,null,null,/** COL_NAME + " ASC"**/null,null);

        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            returner.add(mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)));
        }
        return returner;
    }
    public boolean deleteEvent(long rowId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] row = new String[]{rowId+""};
        Log.v("Deleting event",rowId+"");
        boolean tru = db.delete(this.TABLE_NAME, this.COL_NAME+"=?", row) > 0;
        return tru;
    }
}
