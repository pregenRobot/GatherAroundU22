package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.EventLog;
import android.util.Log;

import com.example.android.gatheraround.DataSenderToServer;
import com.example.android.gatheraround.custom_classes.Participants;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import static com.example.android.gatheraround.R.id.d;
import static com.example.android.gatheraround.data.myInfoDatabase.COL1;
import com.example.android.gatheraround.custom_classes.Events;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventList.db";
    public static final String TABLE_NAME = "event_table";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "EVENTNAME";
    public static final String COL_UNIXTIME = "UNIXTIMESTAMP";
    public static final String COL_PARTICIPANTS = "PARTICIPANTS";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_LOCATIONNAME = "LOCATIONNAME";
    public static final String COL_SUMMARY = "SUMMARY";
    DataSenderToServer dataSenderToServer = new DataSenderToServer();
    private Context context;

    private static final String[] ALL_COLUMNS = new String[]{
            COL_ID,COL_NAME,COL_UNIXTIME,COL_PARTICIPANTS,COL_LOCATION,COL_LOCATIONNAME,COL_SUMMARY
    };

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1/**version**/);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT," +
                COL_UNIXTIME + " INTEGER," +
                COL_PARTICIPANTS + " INTEGER," +
                COL_LOCATION + " TEXT," +
                COL_LOCATIONNAME + " TEXT," +
                COL_SUMMARY + " TEXT)";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String event_name, long unixtime, int participants, LatLng location, String locationName,String summary){
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

        Events newEvents = new Events(unixtime,event_name,participants,location,locationName,summary);
        dataSenderToServer.pushToServer(newEvents);
        long result  = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }
    public Cursor getAllEvents(){
        SQLiteDatabase db = this.getReadableDatabase();

        String where = null;

        Cursor c = db.query(true, TABLE_NAME, ALL_COLUMNS,where,null,null,null,/** COL_NAME + " ASC"**/null,null);
        return c;

    }
    public boolean deleteEvent(long rowId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] row = new String[]{rowId+""};
        Log.v("Deleting event",rowId+"");
        boolean tru = db.delete(this.TABLE_NAME, this.COL_NAME+"=?", row) > 0;
        return tru;
    }


}
