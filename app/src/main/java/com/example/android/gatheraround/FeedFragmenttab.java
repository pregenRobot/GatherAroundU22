package com.example.android.gatheraround;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/11/01.
 */

public class FeedFragmenttab extends Fragment {

    DatabaseHelper eventsDBHelper;
    Cursor eventCursor;

    Gson gson = new Gson();
    Calculations calculations =  new Calculations();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.tabfeedfragment,container,false);

        eventsDBHelper = new DatabaseHelper(getContext());
        boolean insertData = eventsDBHelper.addData(
                                            "Hello test",
                                            new EventDate("2017","11","4","-1","-1","-1",
                                                    "-1","-1","-1","-1"),
                                            0,
                                            new LatLng(23,23),
                                            "Test",
                                            "Summary Test",
                                            Events.CATEGORY_INDIVIDUAL,
                                            true
                                    );


        eventCursor = eventsDBHelper.getAllEvents();

        ArrayList<Events> temp = new ArrayList<>();
        for(eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
            // The Cursor is now set to the right position
            final EventDate eventDate = gson.fromJson(eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_DATE)), EventDate.class);

            String startDate = calculations.concatenate(eventDate, false, true)[0];
            String finishDate = calculations.concatenate(eventDate, false, true)[1];

            final LatLng location = gson.fromJson(eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_LOCATION)),LatLng.class);

            temp.add(new Events(
                    eventDate,
                    eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_NAME)),
                    0,
                    location,
                    eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_LOCATIONNAME)),
                    eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_SUMMARY)),
                    eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_CATEGORY)),
                    eventCursor.getString(eventCursor.getColumnIndex(DatabaseHelper.COL_GLOBALID)),
                    true
                    ));


        }


        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, false);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new FeedAdapter(temp,getContext()));
        recyclerView.addOnScrollListener(new CenterScrollListener());
        //


        return view;
    }
    @Override
    public void onResume(){
        super.onResume();

    }

}
