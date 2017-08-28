package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.android.gatheraround.custom_classes.People;

import java.io.ByteArrayOutputStream;

/**
 * Created by tamimazmain on 2017/08/26.
 */

public class myInfoDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myInfo.db";
    private static final String TABLE_NAME = "my_Information";
    public static final String COL1 = "ID";
    private static final String COL2 = "MY_NAME";
    private static final String COL3 = "MY_IMAGESOURCE";
    private static final String COL4 = "MY_LOCATION";
    private static final String COL5 = "MY_UNIQUE_ID";
    private static final String COL6 = "LOCATION_STATE";

    Context context;

    public myInfoDatabase(Context context){
        super(context, DATABASE_NAME, null, 1/**version**/);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " MY_NAME TEXT, MY_IMAGESOURCE INT, MY_LOCATION TEXT, MY_UNIQUE_ID TEXT, LOCATION_STATE INTEGER)";
        sqLiteDatabase.execSQL(createTable);

        Log.v("onCreate","Database Created!");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

        Log.v("onUpgrade","Database Created!");
    }
    public boolean initialize(String myName, int myImage, String myUsername){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String defaultLocation = "";

        contentValues.put(COL2, myName);
        contentValues.put(COL3, myImage);
        contentValues.put(COL4, defaultLocation);
        contentValues.put(COL5, myUsername);
        contentValues.put(COL6,0);

        db.insert(TABLE_NAME, null, contentValues);

        db.close();

        db.close();

        return true;
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
    public String returnName(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COL1,
                        COL2, COL3, COL4, COL5, COL6}, COL1 + "=?",
                new String[] { String.valueOf(1) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        People contact = new People(cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), cursor.getString(4));
// return shop
        return contact.getName();
    }
    public String returnUserName(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COL1,
                        COL2, COL3, COL4, COL5, COL6}, COL1 + "=?",
                new String[] { String.valueOf(1) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        People contact = new People(cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), cursor.getString(4));
// return shop
        return contact.getUniqueId();
    }
    public int returnImage(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COL1,
                        COL2, COL3, COL4, COL5, COL6}, COL1 + "=?",
                new String[] { String.valueOf(1) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        People contact = new People(cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), cursor.getString(4));
// return shop
        return Integer.parseInt(cursor.getString(2));
    }


}
