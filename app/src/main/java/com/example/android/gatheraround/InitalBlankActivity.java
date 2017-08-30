package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.gatheraround.data.ContactsDatabaseHelper;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.example.android.gatheraround.data.myInfoDatabase;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/08/26.
 */

public class InitalBlankActivity extends AppCompatActivity {

    Context context = this;
    Intent MainActivityIntent;
    @Override
    protected void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.blank_activity_initial);

        myInfoDatabase myinfo = new myInfoDatabase(context);

//        boolean insertData = myinfo.initialize("Tamim Azmain",R.drawable.tedzukaosamu,"tamimAzmain");

//        if (insertData == true) {
//            Toast.makeText(InitalBlankActivity.this, "Data Successfully Inserted! " +
//                    myinfo.returnName() + myinfo.returnImage()
//                    , Toast.LENGTH_LONG).show();
//
//
//            Log.v("Database " + myinfo.returnName(),"Data Successfully Inserted!");
//        } else {
//            Toast.makeText(InitalBlankActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
//            Log.v("Database","Data Insert Failed!");
//        }

        final ContactsDatabaseHelper contactManager = new ContactsDatabaseHelper(context);
        final DatabaseHelper eventDatabaseHelper = new DatabaseHelper(context);
//
//        boolean insertEventData = eventDatabaseHelper.addData(
//                "Hello internet",
//                1704419236, 20,
//                new LatLng(23,3),
//                "Memes",
//                "Lorem Ipsum"
//        );
//        boolean insertEventData2 = eventDatabaseHelper.addData(
//                "Hello internet My name is Tamim Azmain!",
//                1704419236, 20,
//                new LatLng(23,3),
//                "Memes",
//                "Lorem Ipsum"
//        );

//        if (insertEventData == true) {
//            Toast.makeText(InitalBlankActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
//            Log.v("Database","Data Successfully Inserted!"
//            +DatabaseUtils.queryNumEntries(eventDatabaseHelper.getReadableDatabase(), eventDatabaseHelper.TABLE_NAME) + ""
//            );
//        } else {
//            Toast.makeText(InitalBlankActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
//            Log.v("Database","Data Insert Failed!");
//        }

//        if(insertData){
//            Log.v("ContactData Inserted!",
//                    DatabaseUtils.queryNumEntries(contactManager.getReadableDatabase(), contactManager.TABLE_NAME) + "");
//        }else{
//            Log.v("ContactData Failed!",
//                    DatabaseUtils.queryNumEntries(contactManager.getReadableDatabase(), contactManager.TABLE_NAME) + " hello");
//        }

        MainActivityIntent = new Intent(InitalBlankActivity.this,MainActivity.class);

        InitalBlankActivity.this.startActivity(MainActivityIntent);

    }


}
