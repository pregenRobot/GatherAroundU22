package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

        if (insertData == true) {
            Toast.makeText(InitalBlankActivity.this, "Data Successfully Inserted! " +
                    myinfo.returnName() + myinfo.returnImage()
                    , Toast.LENGTH_LONG).show();


            Log.v("Database " + myinfo.returnName(),"Data Successfully Inserted!");
        } else {
            Toast.makeText(InitalBlankActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
            Log.v("Database","Data Insert Failed!");
        }

        MainActivityIntent = new Intent(InitalBlankActivity.this,MainActivity.class);

        InitalBlankActivity.this.startActivity(MainActivityIntent);

    }


}
