package com.example.android.gatheraround;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.gatheraround.data.ContactsDatabaseHelper;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.w3c.dom.Text;

/**
 * Created by tamimazmain on 2017/08/29.
 */

public class EventListCursorAdapter extends CursorAdapter {

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter; may
     *                be any combination of {@link #FLAG_AUTO_REQUERY} and
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     *
     */

    private LayoutInflater cursorInflater;
    Calculations calculations = new Calculations();
    Gson gson = new Gson();
    Context AppContext;

    public EventListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AppContext = context.getApplicationContext();
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.card_view, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView timeText = (TextView) view.findViewById(R.id.event_time);
        TextView nameText = (TextView) view.findViewById(R.id.event_name);
        TextView dateText = (TextView) view.findViewById(R.id.event_date);
        TextView summaryText = (TextView) view.findViewById(R.id.event_summary);
        TextView participantsText = (TextView) view.findViewById(R.id.event_participantNum);
        TextView locationText = (TextView) view.findViewById(R.id.event_location);

        final Cursor mCursor = cursor;

        String date = calculations.UnixTimeConverter(
                mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COL_UNIXTIME)
        ))[0];
        String time = calculations.UnixTimeConverter(
                mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COL_UNIXTIME))
        )[1];
        final LatLng location = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATION)),LatLng.class);

        nameText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_NAME)));
        dateText.setText(date);
        timeText.setText(time);
        participantsText.setText(mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.COL_PARTICIPANTS)));
        summaryText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY)));
        locationText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COL_LOCATIONNAME)));

        locationText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final CameraPosition camLocation  = CameraPosition.builder().
                        target(location).zoom(18).build();
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
                dialog.show();
            }
        });

    }
}
