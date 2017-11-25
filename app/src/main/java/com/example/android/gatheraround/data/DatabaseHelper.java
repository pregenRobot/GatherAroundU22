package com.example.android.gatheraround.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.android.gatheraround.DataSenderToServer;
import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.Post;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.example.android.gatheraround.custom_classes.Events;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "eventList.db";

    public static final String TABLE_NAME = "event_table_2";
    // id for identifying within database
    public static final String COL_LOCALID = "_id";
    public static final String COL_NAME = "EVENTNAME";
    public static final String COL_DATE = "DATE";
    private static final String COL_PARTICIPANTS = "PARTICIPANTS";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_LOCATIONNAME = "LOCATIONNAME";
    public static final String COL_SUMMARY = "SUMMARY";
    public static final String COL_CATEGORY = "CATEGORY";
    // id for identifying on the server
    public static final String COL_GLOBALID = "GLOBALID";

    public static final String USERS_TABLE_NAME = "users_table";
    public static final String COL_USER_NAME = "USER_NAME";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_USER_PROFILE_TEXT = "PROFILE_TEXT";

    public static final String POST_TABLE_NAME = "POST_TABLE";
    public static final String COL_POST_POSTER = "POSTER";
    public static final String COL_POST_CONTENT = "CONTENT";
    public static final String COL_POST_DATE = "DATE";
    public static final String COL_POST_LOCATION = "LOCATION";
    public static final String COL_POST_LOCATION_NAME = "LOCATION_NAME";
    public static final String COL_POST_ID = "POST_ID";

    public static final String COL_DOESEXISTSONSERVER = "DOESEXISTSONSERVER";
    public static final int BOOLEAN_TRUE = 0;
    public static final int BOOLEAN_FALSE = 1;
    private static final int DB_VERSION = 2;

    // TODO: 2017/10/15 delete these user_info_table related codes if not needed
//    public static final String USER_INFO_TABLE = "user_info_table";
//    public static final String COL_USER_INFO_ID = "id";
//    public static final String COL_USERID = "user_id";
//    public static final String COL_LOGINID = "login_id";
//    public static final String COL_LOGIN_DATE = "login_date";

    private DataSenderToServer dataSenderToServer = new DataSenderToServer();

    private static final String[] ALL_COLUMNS = new String[]{COL_LOCALID,COL_NAME,COL_DATE,COL_PARTICIPANTS,COL_LOCATION,COL_LOCATIONNAME,COL_SUMMARY,COL_CATEGORY,COL_GLOBALID, COL_DOESEXISTSONSERVER};

//    private static final String[] USER_INFO_ALL_COLUMNS = new String[]{COL_USER_INFO_ID, COL_USERID, COL_LOGINID, COL_LOGIN_DATE};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_LOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT," +
                COL_DATE + " TEXT," +
                COL_PARTICIPANTS + " INTEGER," +
                COL_LOCATION + " TEXT," +
                COL_LOCATIONNAME + " TEXT," +
                COL_SUMMARY + " TEXT," +
                COL_CATEGORY + " TEXT," +
                COL_GLOBALID + " TEXT," +
                COL_DOESEXISTSONSERVER + " INTEGER)";
        sqLiteDatabase.execSQL(createTableSQL);

//        String createUserInfoTable = "CREATE TABLE " + USER_INFO_TABLE + " (" + COL_USER_INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USERID + " TEXT, " + COL_LOGINID + " TEXT" + COL_LOGIN_DATE + " INTEGER)";

        String createUsersTableSQL = "CREATE TABLE " + USERS_TABLE_NAME + " (" + COL_USER_ID + " TEXT," + COL_USER_NAME + " TEXT," + COL_USER_PROFILE_TEXT + " TEXT)";
        sqLiteDatabase.execSQL(createUsersTableSQL);

        String createPostTableSQL = "CREATE TABLE " + POST_TABLE_NAME + " (" + COL_POST_POSTER + " TEXT," + COL_POST_CONTENT + " TEXT," + COL_POST_DATE + " TEXT," + COL_POST_LOCATION_NAME + " TEXT," + COL_POST_ID + " TEXT)";
        sqLiteDatabase.execSQL(createPostTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        if (oldVersion == 1 && newVersion == 2){
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_DOESEXISTSONSERVER + " INTEGER DEFAULT " + BOOLEAN_TRUE);
        }
    }

    public boolean addData(String event_name, EventDate date, int participants, LatLng location, String locationName, String summary, String category, boolean doesExistsOnServer){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Gson gson = new Gson();

        String gsonLocation = gson.toJson(location,LatLng.class);

        String dateGson = gson.toJson(date, EventDate.class);

        contentValues.put(COL_NAME,event_name);
        contentValues.put(COL_DATE, dateGson);
        contentValues.put(COL_PARTICIPANTS, participants);
        contentValues.put(COL_LOCATION, gsonLocation);
        contentValues.put(COL_LOCATIONNAME, locationName);
        contentValues.put(COL_SUMMARY, summary);
        contentValues.put(COL_CATEGORY, category);
        if (doesExistsOnServer){
            contentValues.put(COL_DOESEXISTSONSERVER, BOOLEAN_TRUE);
        } else{
            contentValues.put(COL_DOESEXISTSONSERVER, BOOLEAN_FALSE);
        }

        Events newEvents = new Events(date,event_name,participants,location,locationName,summary, category, "contemporary", doesExistsOnServer);
        String key = dataSenderToServer.pushToServer(newEvents);

        contentValues.put(COL_GLOBALID, key);
        Log.v("AddedData",contentValues.toString());

        dataSenderToServer.addOneParticipants(key);
        MyEventsDatabaseHelper myevents = new MyEventsDatabaseHelper(context);

        boolean truf = myevents.addData(key);
        if(truf){
            Log.v("Local Events","Added Global ID");
        }else{
            Log.v("Local Events","Failed to Add Global ID");
        }

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean addParticipant(Events events){

        long result = -1;
        if(!this.checkForExistingEvent(events.getGlobalId())){

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            Gson gson = new Gson();
            String gsonLocation = gson.toJson(events.getLocation(),LatLng.class);
            String dateGson = gson.toJson(events.getDate(), EventDate.class);

            contentValues.put(COL_NAME,events.getName());
            contentValues.put(COL_DATE, dateGson);
            contentValues.put(COL_PARTICIPANTS, events.getParticipants());
            contentValues.put(COL_LOCATION, gsonLocation);
            contentValues.put(COL_LOCATIONNAME, events.getLocationName());
            contentValues.put(COL_SUMMARY, events.getEventSummary());
            contentValues.put(COL_CATEGORY, events.getCategory());
            contentValues.put(COL_GLOBALID, events.getGlobalId());
            if(events.getDoesExitsOnServer()){
                contentValues.put(COL_DOESEXISTSONSERVER, BOOLEAN_TRUE);
            }else{
                contentValues.put(COL_DOESEXISTSONSERVER, BOOLEAN_FALSE);
            }

            Log.i("does", "" + events.getDoesExitsOnServer());

            dataSenderToServer.addOneParticipants(events.getGlobalId());

            result = db.insert(TABLE_NAME, null, contentValues);
        }
        return result != -1;
    }

    public Cursor getAllEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db == null){
            Log.v("SQLITE DATABASE","NULL!");
        }else{
            Log.v("SQLITE DATABASE","NOT NULL!");
        }
        assert db != null;
        return db.query(true, TABLE_NAME, ALL_COLUMNS,null,null,null,null,null,null);
    }

    public boolean checkForExistingEvent(String toCheck){
        ArrayList<String> returner = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, TABLE_NAME, ALL_COLUMNS, null, null, null, null, null, null);
        Log.v("EventCursor",mCursor.toString());

        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            returner.add(mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)));
            Log.v("Database","Searching Through registered IDs" + mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)));
        }
        mCursor.close();
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

    public ArrayList<String> getAllIds(){

        ArrayList<String> result = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor mCursor = database.query(TABLE_NAME, new String[]{COL_GLOBALID}, null, null, null, null, null);

        while (mCursor.moveToNext()){
            result.add(mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)));
        }

        mCursor.close();

        return result;
    }

    public void updateDoesExitsOnServer(String key, boolean doesExitsOnServer){
        //key = globalId

        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        if (doesExitsOnServer){
            contentValues.put(COL_DOESEXISTSONSERVER, BOOLEAN_TRUE);
        } else {
            contentValues.put(COL_DOESEXISTSONSERVER, BOOLEAN_FALSE);
        }

        database.update(TABLE_NAME, contentValues, COL_GLOBALID + "=?", new String[]{key});
    }

    public boolean addNewUserToContactList(UserProfile profile){

        boolean doesExist = false;

        for (UserProfile profileFromDatabase : getAllContacts()) {
            if (profileFromDatabase.getUid().equals(profile.getUid())) {
                doesExist = true;
            }
        }
        Log.v("Adding user",profile.toString());

        if (!doesExist){
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(COL_USER_ID, profile.getUid());
            contentValues.put(COL_USER_NAME, profile.getName());
            contentValues.put(COL_USER_PROFILE_TEXT, profile.getProfileText());

            database.insert(USERS_TABLE_NAME, null, contentValues);

            Toast.makeText(context, "Added new user", Toast.LENGTH_SHORT).show();
        }

        return doesExist;
    }

    public ArrayList<UserProfile> getAllContacts(){

        ArrayList<UserProfile> result = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(USERS_TABLE_NAME, new String[]{COL_USER_ID, COL_USER_NAME}, null, null, null, null, null);

        while(cursor != null && cursor.moveToNext()){

            String uid = cursor.getString(cursor.getColumnIndex(COL_USER_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_USER_NAME));
            String profileText = "プロフィールなし";
            try{
                 profileText = cursor.getString(cursor.getColumnIndex(COL_USER_PROFILE_TEXT));
            }catch (Exception e){
                Log.e("ProfileError", e.toString());
            }

            UserProfile profile = new UserProfile(uid,"NotPublic", name, profileText);
            result.add(profile);

//            try{
//                UserProfile profile = new UserProfile(cursor.getString(cursor.getColumnIndex(COL_USER_ID)),"NotPublic", cursor.getString(cursor.getColumnIndex(COL_USER_NAME)), cursor.getString(cursor.getColumnIndex(COL_USER_PROFILE_TEXT)));
//                result.add(profile);
//            }catch (Exception e){
//                Log.e("UserProfileError", e.toString());
//            }
        }

        cursor.close();

        return result;
    }

    public int checkoutID(String findString){

        int returner =  0;

        Cursor mCursor = getAllEvents();

        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position

            if(mCursor.getString(mCursor.getColumnIndex(COL_GLOBALID)).equals(findString)){
                returner = mCursor.getInt(mCursor.getColumnIndex(COL_LOCALID));
            }

        }

        return  returner;
    }

    public void addPost(Post post){

        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        Gson gson = new Gson();

        String locationGson = gson.toJson(post.getLocation(), LatLng.class);

        String dateGson = gson.toJson(post.getPostDate(), EventDate.class);

        values.put(COL_POST_POSTER, post.getPosterUid());
        values.put(COL_POST_CONTENT, post.getPostContent());
        values.put(COL_POST_DATE, dateGson);
        values.put(COL_POST_LOCATION, locationGson);
        values.put(COL_POST_LOCATION_NAME, post.getLocationName());
        values.put(COL_POST_ID, post.getPostId());

        String key = dataSenderToServer.sendNewPost(post);

        values.put(COL_POST_ID, key);

        database.insert(POST_TABLE_NAME, null, values);
    }
}
