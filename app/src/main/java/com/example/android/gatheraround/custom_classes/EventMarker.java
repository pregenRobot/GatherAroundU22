package com.example.android.gatheraround.custom_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by tamimazmain on 2017/09/26.
 */

public class EventMarker implements ClusterItem {

    private Events events;
    private LatLng mLocation;
    private MarkerOptions mMarkerOptions;
    private Context mContext;

    public EventMarker(Events event){
        events = event;
        mLocation = event.getLocation();
    }
    public EventMarker(Events event,Context context){
        events = event;
        mLocation = event.getLocation();
        mContext = context;
        switch (event.getCategory()){
            case "INDIVIDUAL":
                mMarkerOptions = new MarkerOptions().position(mLocation).title(events.getName())
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("individual", 75, 75)));
                break;
            case "NPO":
                mMarkerOptions = new MarkerOptions().position(mLocation).title(events.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("npo", 75, 75)));
                break;
            case "CORPORATE":
                mMarkerOptions = new MarkerOptions().position(mLocation).title(events.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("corporate", 75, 75)));
                break;

        }
    }
    @Override
    public LatLng getPosition() {
        return mLocation;
    }

    public Events getTag(){
        Log.v("itemClicked","returning event");
        return events;
    }

    public MarkerOptions getMarker() {
        return mMarkerOptions;
    }
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(),mContext.getResources().getIdentifier(iconName, "drawable", mContext.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}
