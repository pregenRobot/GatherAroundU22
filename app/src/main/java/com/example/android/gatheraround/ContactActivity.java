package com.example.android.gatheraround;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.data.*;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tamimazmain on 2017/08/16.
 */

public class ContactActivity extends AppCompatActivity {
    FloatingActionButton addcontacts;


    @Override
    protected void onCreate(Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.contact_list);

        myDataHolder dataHolder = new myDataHolder();

        ListView contanctListView = (ListView) findViewById(R.id.contactListView);
        ContactsAdapter contactadapter = new ContactsAdapter(this,dataHolder.getContactList());

        contanctListView.setAdapter(contactadapter);
        addcontacts = (FloatingActionButton) findViewById(R.id.fabcontacts);



        addcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.inital_contactcommit,null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                final EditText nameedit = (EditText) mView.findViewById(R.id.nameinitial);
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
                        dialog2.show();

                        final EditText nameedit2 = (EditText) m2View.findViewById(R.id.name_edit_text);
                        final TextView uniqueidfinal = (TextView) m2View.findViewById(R.id.uniquecodetext);
                        TextView cancelbutton2 = (TextView) m2View.findViewById(R.id.contactsCancel);
                        TextView doneButton2 = (TextView) m2View.findViewById(R.id.contactsDone);

                        cancelbutton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });


                    }
                });
            }
        });

    }
    @Override
    protected void onResume(){
        super.onResume();

    }

}
