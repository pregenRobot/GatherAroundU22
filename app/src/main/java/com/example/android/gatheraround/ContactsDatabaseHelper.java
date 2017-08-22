package com.example.android.gatheraround;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import static com.example.android.gatheraround.R.id.event_name;

/**
 * Created by tamimazmain on 2017/08/21.
 */

public class ContactsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventList.db";
    private static final String TABLE_NAME = "event_table";
    public static final String COL1 = "ID";
    private static final String COL2 = "PERSONNAME";
    private static final String COL3 = "LOCATION";
    private static final String COL4 = "IMAGE";

    private Context context;

    public ContactsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1/**version**/);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " PERSONNAME TEXT, LOCATION TEXT, IMAGESOURCE INTEGER)";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String person_name, LatLng location, int imageResource){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Gson gson = new Gson();
        String gsonLocation = gson.toJson(location,LatLng.class);

        contentValues.put(COL2,person_name);
        contentValues.put(COL3, gsonLocation);
        contentValues.put(COL4, imageResource);


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
