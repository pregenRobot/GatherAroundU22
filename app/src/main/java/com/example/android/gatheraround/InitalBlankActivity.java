package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.gatheraround.data.ContactsDatabaseHelper;
import com.example.android.gatheraround.data.myInfoDatabase;

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

        boolean insertData = myinfo.initialize("Tamim Azmain",R.drawable.tedzukaosamu,"tamimAzmain");

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

        boolean insertData1 = contactManager.addData("Tamim Azman",R.drawable.tedzukaosamu,null,"ta1130");
        boolean insertData2 = contactManager.addData("Chiharu Miyoshi",R.drawable.stevejobs,null,"chiharum");
        boolean insertDat3 = contactManager.addData("手塚治虫",R.drawable.callpressed,null,"TedzukaOsamu");
        boolean insertDat4 = contactManager.addData("Angelina Jolie",R.drawable.angelinajolie,null,"AngelinaJolie");

        if(insertDat4){
            Log.v("ContactData Inserted!",
                    DatabaseUtils.queryNumEntries(contactManager.getReadableDatabase(), contactManager.TABLE_NAME) + "");
        }else{
            Log.v("ContactData Failed!",
                    DatabaseUtils.queryNumEntries(contactManager.getReadableDatabase(), contactManager.TABLE_NAME) + " hello");
        }

        MainActivityIntent = new Intent(InitalBlankActivity.this,MainActivity.class);

        InitalBlankActivity.this.startActivity(MainActivityIntent);

    }


}
