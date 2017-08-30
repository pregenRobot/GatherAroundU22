package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

    private Context context;
    private static final String DATABASE_NAME = "eventList.db";
    public static final String TABLE_NAME = "event_table";
    public static final String TABLE_NAME_2 = "event_table_2";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "EVENTNAME";
    public static final String COL_UNIXTIME = "UNIXTIMESTAMP";
    public static final String COL_PARTICIPANTS = "PARTICIPANTS";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_LOCATIONNAME = "LOCATIONNAME";
    public static final String COL_SUMMARY = "SUMMARY";
    public static final String COL_CATEGORY = "CATEGORY";
    DataSenderToServer dataSenderToServer = new DataSenderToServer();
    public static final int DB_VERSION = 2;

    public static final String CATEGORY_DEFAULT = "individual";

    private static final String[] ALL_COLUMNS = new String[]{
            COL_ID,COL_NAME,COL_UNIXTIME,COL_PARTICIPANTS,COL_LOCATION,COL_LOCATIONNAME,COL_SUMMARY,COL_CATEGORY
    };

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION/**version**/);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME_2 + "( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT," +
                COL_UNIXTIME + " INTEGER," +
                COL_PARTICIPANTS + " INTEGER," +
                COL_LOCATION + " TEXT," +
                COL_LOCATIONNAME + " TEXT," +
                COL_SUMMARY + " TEXT" +
                COL_CATEGORY + " TEXT)";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        SQLiteDatabase database = this.getWritableDatabase();
//
//        if (oldVersion == 1){
//
//            int oldDbCounts = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
//
//            for (int a = 0; a < oldDbCounts; a++){
//                Cursor cursor = null;
//
//                try{
//                    cursor = database.query(DATABASE_NAME, ALL_COLUMNS, COL_ID + " = ?", new String[]{String.valueOf(a)}, null, null, null);
//
//                    int indexName = cursor.getColumnIndex(COL_NAME);
//                    int indexTime = cursor.getColumnIndex(COL_UNIXTIME);
//                    int indexParticipants = cursor.getColumnIndex(COL_PARTICIPANTS);
//                    int indexLocation = cursor.getColumnIndex(COL_LOCATION);
//                    int indexLocationName = cursor.getColumnIndex(COL_LOCATIONNAME);
//                    int indexSummary = cursor.getColumnIndex(COL_SUMMARY);
//
//                    ContentValues contentValues = new ContentValues();
//
//                    contentValues.put(COL_NAME, cursor.getString(indexName));
//                    contentValues.put(COL_UNIXTIME, cursor.getLong(indexTime));
//                    contentValues.put(COL_PARTICIPANTS, cursor.getLong(indexParticipants));
//
////                    Gson gson = new Gson();
////                    final LatLng location = gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LOCATION)),LatLng.class);
//
//                    contentValues.put(COL_LOCATION, cursor.getString(indexLocation));
//                    contentValues.put(COL_LOCATIONNAME, cursor.getString(indexLocationName));
//                    contentValues.put(COL_SUMMARY, cursor.getString(indexSummary));
//                    contentValues.put(COL_CATEGORY, CATEGORY_DEFAULT);
//
//
//                }finally {
//                    if(cursor != null){
//                        cursor.close();
//                    }
//                }
//            }
//        }
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

        Events newEvents = new Events(unixtime,event_name,participants,location,locationName,summary, category);
        dataSenderToServer.pushToServer(newEvents);
        long result  = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
//
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
