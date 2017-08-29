package com.example.android.gatheraround;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.android.gatheraround.data.ContactsDatabaseHelper;
import com.example.android.gatheraround.data.DatabaseHelper;

public class testActivity extends AppCompatActivity {

    Context context;
    String[] formFieldNames = new String[]{
            ContactsDatabaseHelper.COL_NAME,
            ContactsDatabaseHelper.COL_IMAGE,
            ContactsDatabaseHelper.COL_LOCATION,
            ContactsDatabaseHelper.COL_USERNAME
    };
    Cursor eventCursor;
    EventListCursorAdapter eventListCursorAdapter;
    DatabaseHelper eventDatbaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = testActivity.this;

        ContactsDatabaseHelper mDb = new ContactsDatabaseHelper(context);

        ListView listView = (ListView) findViewById(R.id.testListView);

        eventDatbaseHelper = new DatabaseHelper(context);

        eventCursor = eventDatbaseHelper.getAllEvents();

        eventListCursorAdapter = new EventListCursorAdapter(
                testActivity.this,
                eventCursor,
                0);
        listView.setAdapter(eventListCursorAdapter);

    }
}
