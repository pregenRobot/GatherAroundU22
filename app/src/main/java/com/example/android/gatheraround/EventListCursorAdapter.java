package com.example.android.gatheraround;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.gatheraround.data.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

/**
 * Created by tamimazmain on 2017/08/29.
 */

public class EventListCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    Calculations calculations = new Calculations();
    Gson gson = new Gson();
    Context AppContext;
    DatabaseHelper dbHelper;
    Context mContext;
    String[] whereClause;
    Intent mainActivityIntent;

    public EventListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppContext = context.getApplicationContext();
        mContext = context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.card_view, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView timeText = (TextView) view.findViewById(R.id.event_time);
        TextView nameText = (TextView) view.findViewById(R.id.event_name);
        TextView dateText = (TextView) view.findViewById(R.id.event_date);
        TextView summaryText = (TextView) view.findViewById(R.id.event_summary);
        TextView participantsText = (TextView) view.findViewById(R.id.event_participantNum);
        TextView locationText = (TextView) view.findViewById(R.id.event_location);
        TextView categoryText = (TextView) view.findViewById(R.id.event_category);
        CardView card = (CardView) view.findViewById(R.id.CardViewItem);

        final Cursor mCursor = cursor;

        String date = calculations.UnixTimeConverter(
                mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COL_UNIXTIME)
        ))[0];
        String time = calculations.UnixTimeConverter(
                mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COL_UNIXTIME))
        )[1];
        final LatLng location = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATION)),LatLng.class);

        nameText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));

        Log.i("bindView cursor check", "name=" + mCursor.getString((mCursor.getColumnIndex(DatabaseHelper.COL_NAME))));

        dateText.setText(date);
        timeText.setText(time);
        participantsText.setText(mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COL_PARTICIPANTS))+"");
        summaryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY)));
        locationText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATIONNAME)));
        categoryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_CATEGORY)));

        locationText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final CameraPosition camLocation  = CameraPosition.builder().target(location).zoom(18).build();
                MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camLocation));
            }
        });

        summaryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater mLayoutInflator;
                mLayoutInflator = LayoutInflater.from(AppContext);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = mLayoutInflator.inflate(R.layout.summarydialog,null);

                TextView textView = mView.findViewById(R.id.mainText);
                textView.setText(
                        mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY))
                );

                textView.setMovementMethod(new ScrollingMovementMethod());

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notifyDataSetChanged();
                whereClause = new String[] {String.valueOf(mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)))};
                Log.v("Where clause:",whereClause[0]);

                Log.i("Event onLongClick", "name=" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));

                LayoutInflater mLayoutInflator;
                mLayoutInflator = LayoutInflater.from(AppContext);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = mLayoutInflator.inflate(R.layout.canceldelete_editor,null);

                final TextView cancelButton = (TextView) mView.findViewById(R.id.cancelAction);
                final TextView deleteButton = (TextView) mView.findViewById(R.id.deleteEntryAction);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                dialog.show();
                dbHelper = new DatabaseHelper(mContext);
                final SQLiteDatabase db = dbHelper.getWritableDatabase();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        notifyDataSetChanged();
                        Log.v("Where clause changed:",whereClause[0]);
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int tru = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_LOCALID + " = " + mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COL_LOCALID)), null);

                        if(tru == 1){
                            Log.v("Delete: ", "SuccessFull!" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                        }else{
                            Log.v("Delete: ", "Failed!" + mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                        }

                        DataSenderToServer dataSenderToServer = new DataSenderToServer();
                        dataSenderToServer.eraseEntry(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)));

                        dialog.dismiss();

                        mainActivityIntent = new Intent(mContext,MainActivity.class);
                        mContext.startActivity(mainActivityIntent);
                    }
                });
                return true;
            }
        });

    }
}
