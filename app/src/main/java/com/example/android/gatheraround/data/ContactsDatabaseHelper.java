package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ListView;

import com.example.android.gatheraround.R;
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

    private static final String DATABASE_NAME = "gatheraround.db";
    public static final String TABLE_NAME = "contact_table";

    public static final String COL_ID = "ID";
    public static final String COL_NAME = "PERSON_NAME";
    public static final String COL_IMAGE = "IMAGESOURCE";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_USERNAME = "UNIQUE_ID";

    private static final String[] ALL_COLUMNS = {
            "_id",COL_NAME,COL_IMAGE,COL_LOCATION,COL_USERNAME
    };

    private Context context;

    public ContactsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1/**version**/);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT,"
                + COL_IMAGE + " INTEGER,"
                + COL_LOCATION + " TEXT,"
                + COL_USERNAME + " TEXT)";
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

        contentValues.put(COL_NAME, person_name);
        contentValues.put(COL_IMAGE, imageSource);
        contentValues.put(COL_LOCATION, gsonLocation);
        contentValues.put(COL_USERNAME, unique_id);

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


    public Cursor returnContactList(){
        SQLiteDatabase db = this.getReadableDatabase();
        return null;
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_NAME);

        Cursor cursor = builder.query(this.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();

        String where = null;

        Cursor c = db.query(true, TABLE_NAME, ALL_COLUMNS,where,null,null,null,/** COL_NAME + " ASC"**/null,null);


        return c;
    }

//    private void populateListView() {
//        Cursor cursor = myDb.getAllRows();
//        String[] fromFieldNames = new String[] {DBAdapter.KEY_ROWID,DBAdapter.KEY_TASK};
//        int[] toViewIDs = new int[] {R.id.textViewItemNumber, R.id.textViewItemTask};
//        SimpleCursorAdapter myCursorAdapter;
//        myCursorAdapter = new SimpleCursorAdapter(
//                getBaseContext(),
//                R.layout.item_layout,
//                cursor,
//                fromFieldNames,
//                toViewIDs,
//                0);
//        ListView myList = (ListView) findViewById(R.id.listViewTasks); myList.setAdapter(myCursorAdapter);
//    }

}
