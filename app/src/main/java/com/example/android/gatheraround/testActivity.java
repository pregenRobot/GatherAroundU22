package com.example.android.gatheraround;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.gatheraround.data.ContactsDatabaseHelper;

public class testActivity extends AppCompatActivity {

    Context context;
    String[] formFieldNames = new String[]{
            ContactsDatabaseHelper.COL_NAME,
            ContactsDatabaseHelper.COL_IMAGE,
            ContactsDatabaseHelper.COL_LOCATION,
            ContactsDatabaseHelper.COL_USERNAME
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = this;

        ContactsDatabaseHelper mDb = new ContactsDatabaseHelper(context);

        Cursor c = mDb.getAllContacts();

    }
}
