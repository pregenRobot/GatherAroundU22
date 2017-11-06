package com.example.android.gatheraround;

import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/11/01.
 */

public class FeedFragmenttab extends Fragment {
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.tabfeedfragment,container,false);
        ArrayList<Events> temp = new ArrayList<>();

        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));
        temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));temp.add(new Events(new EventDate("2000","11","30","-1","-1","-1","-1","-1",
                "-1","-1"),"Test",1,new LatLng(1.0,23.0),"Hello",
                "Hello",Events.CATEGORY_CORPORATE,"HEllo",true));

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
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
