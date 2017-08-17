package com.example.android.gatheraround;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.common.data.*;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tamimazmain on 2017/08/16.
 */

public class ContactActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.contact_list);

        myDataHolder dataHolder = new myDataHolder();

        ListView contanctListView = (ListView) findViewById(R.id.contactListView);
        ContactsAdapter contactadapter = new ContactsAdapter(this,dataHolder.getContactList());

        contanctListView.setAdapter(contactadapter);
    }
    @Override
    protected void onResume(){
        super.onResume();

    }

}
