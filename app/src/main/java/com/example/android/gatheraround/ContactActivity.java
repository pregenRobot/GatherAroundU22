package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.data.ContactsDatabaseHelper;

/**
 * Created by tamimazmain on 2017/08/16.
 */

public class ContactActivity extends AppCompatActivity {
    FloatingActionButton addcontacts;

    private String tempName;
    private String tempUniqueId;
    private int tempImageResource;
    ContactListCursorAdapter customAdapter;

    Cursor contactListCursor;
    Context context = this;
    Intent myInfoIntent;

    @Override
    protected void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.contact_list);

        TextView myInfo = (TextView) findViewById(R.id.myInfo);

        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myInfoIntent = new Intent(ContactActivity.this,myInfoActivity.class);

                ContactActivity.this.startActivity(myInfoIntent);
            }
        });


    }
    @Override
    protected void onResume(){
        super.onResume();
        final ListView contanctListView = (ListView) findViewById(R.id.contactListView);
//        ContactsAdapter contactadapter = new ContactsAdapter(this,dataHolder.getContactList());
//        contanctListView.setAdapter(contactadapter);
        final ContactsDatabaseHelper contactManager = new ContactsDatabaseHelper(context);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                contactListCursor = contactManager.getAllContacts();
                customAdapter = new ContactListCursorAdapter(
                        ContactActivity.this,
                        contactListCursor,
                        0);

                contanctListView.setAdapter(customAdapter);
            }
        });
        thread.start();

        addcontacts = (FloatingActionButton) findViewById(R.id.fabcontacts);

        addcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.inital_contactcommit,null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final EditText uniqueidedit = (EditText) mView.findViewById(R.id.uniquecodeinital);
                TextView cancelbutton = (TextView) mView.findViewById(R.id.contactsCancelInitial);
                TextView doneButton = (TextView) mView.findViewById(R.id.contactsSearch);

                cancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder m2Builder = new AlertDialog.Builder(ContactActivity.this);
                        View m2View = getLayoutInflater().inflate(R.layout.contactadd_dialog,null);

                        m2Builder.setView(m2View);
                        final AlertDialog dialog2 = m2Builder.create();

                        tempUniqueId = uniqueidedit.getText().toString();
                        Log.v("Data Search",tempUniqueId);


                        final EditText nameedit2 = (EditText) m2View.findViewById(R.id.name_edit_text);
                        TextView uniqueidfinal = (TextView) m2View.findViewById(R.id.uniquecodetext);
                        TextView cancelbutton2 = (TextView) m2View.findViewById(R.id.contactsCancel);
                        TextView doneButton2 = (TextView) m2View.findViewById(R.id.contactsDone);

                        uniqueidfinal.setText(tempUniqueId);

                        dialog2.show();
                        cancelbutton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                dialog2.dismiss();
                            }
                        });
                        doneButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                tempName = nameedit2.getText().toString();

//                                boolean insertData = contactManager.addData(tempName,R.drawable.angelinajolie,null,tempUniqueId);

//                                if (insertData == true) {
//                                    Toast.makeText(ContactActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
//                                    Log.v("Database","Data Successfully Inserted!");
//                                } else {
//                                    Toast.makeText(ContactActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
//                                    Log.v("Database","Data Insert Failed!");
//                                }
//                                finish();
//                                startActivity(getIntent());
                            }
                        });
                    }
                });
            }
        });

    }

//    private void populateListView() {
//        Cursor cursor = myDb.getAllContacts();
//
//        int[] toViewIDs = new int[] {R.id.personName, R.id.personLocation};
//        SimpleCursorAdapter myCursorAdapter;
//        myCursorAdapter = new SimpleCursorAdapter(
//                getBaseContext(),
//                R.layout.contact_individual_list,
//                cursor,
//                formFieldNames,
//                toViewIDs,
//                0);
//        ListView myList = (ListView) findViewById(R.id.contactListView);
//        myList.setAdapter(myCursorAdapter);
//    }
//    new Handler().post(new Runnable() {
//
//        @Override
//        public void run() {
//            customAdapter = new MyCursorAdapter(
//                    MainActivity.this,
//                    mCursor,
//                    0);
//
//            listView.setAdapter(customAdapter);
//        }
//
//    });

}

