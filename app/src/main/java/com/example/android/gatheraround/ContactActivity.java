package com.example.android.gatheraround;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tamimazmain on 2017/08/16.
 */

public class ContactActivity extends AppCompatActivity {

    public ArrayList<People> people;
    @Override
    protected void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.contact_list);

        people = new ArrayList<People>();

        people.add(new People("Tamim Azmain",R.drawable.angelinajolie,new LatLng(48.964716,2.449014)));
        people.add(new People("Chiharu Miyoshi",R.drawable.stevejobs,new LatLng(40.730610, -73.935242)));
        people.add(new People("Steve Jobs",R.drawable.tedzukaosamu,new LatLng(-36.848461, 174.763336)));

        ListView contanctListView = (ListView) findViewById(R.id.contactListView);
        ContactsAdapter contactadapter = new ContactsAdapter(this,people);

        contanctListView.setAdapter(contactadapter);
    }
    @Override
    protected void onResume(){
        super.onResume();

    }

}
