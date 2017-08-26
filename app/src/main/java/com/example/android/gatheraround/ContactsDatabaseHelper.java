package com.example.android.gatheraround;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import static android.R.attr.bitmap;
import static android.R.attr.id;
import static com.example.android.gatheraround.R.id.event_name;

/**
 * Created by tamimazmain on 2017/08/21.
 */

public class ContactsDatabaseHelper extends SQLiteOpenHelper {

private static final String DATABASE_NAME = "contactList.db";
    private static final String TABLE_NAME = "contact_table";
    public static final String COL1 = "ID";
    private static final String COL2 = "PERSON_NAME";
    private static final String COL3 = "IMAGESOURCE";
    private static final String COL4 = "LOCATION";
    private static final String COL5 = "UNIQUE_ID";
    private Context context;

    public ContactsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1/**version**/);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " PERSON_NAME TEXT, IMAGESOURCE INTEGER, LOCATION TEXT, UNIQUE_ID TEXT)";
        sqLiteDatabase.execSQL(createTable);

        Log.v("onCreate","Database Created!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

        Log.v("onUpgrade","Database Created!");
    }

    public boolean addData(String person_name,int imageSource, LatLng location, String unique_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Log.v("addData","Database Created!");

        Gson gson = new Gson();
        String gsonLocation = "";
        if(location != null) {
            gsonLocation = gson.toJson(location, LatLng.class);
        }

        contentValues.put(COL2, person_name);
        contentValues.put(COL3, imageSource);
        contentValues.put(COL4, gsonLocation);
        contentValues.put(COL5, unique_id);

        long result  = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Cursor showData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
    

}
