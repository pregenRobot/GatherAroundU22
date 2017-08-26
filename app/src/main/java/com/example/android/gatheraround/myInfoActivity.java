package com.example.android.gatheraround;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import static com.example.android.gatheraround.R.layout.myinfo;

/**
 * Created by tamimazmain on 2017/08/26.
 */

public class myInfoActivity extends AppCompatActivity {

    Context context = this;
    @Override
    protected void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(myinfo);

        Switch locationSwitch = (Switch) findViewById(R.id.switchLocation);
        TextView myName = (TextView) findViewById(R.id.myName);
        TextView myUsername = (TextView) findViewById(R.id.myUsername);
        TextView selectImage = (TextView) findViewById(R.id.selectmyImage);
        ImageView myImage = (ImageView) findViewById(R.id.myImage);

        myInfoDatabase myinfo = new myInfoDatabase(context);

        myName.setText(myinfo.returnName());
        myUsername.setText(myinfo.returnUserName());

        Drawable myIcon = getResources().getDrawable(myinfo.returnImage());
        myImage.setImageDrawable(myIcon);

    }

}
