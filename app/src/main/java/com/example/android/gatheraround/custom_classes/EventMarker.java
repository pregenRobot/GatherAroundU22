package com.example.android.gatheraround.custom_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.EventLog;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tamimazmain on 2017/09/26.
 */

public class EventMarker implements ClusterItem {

    private Events events;
    private LatLng mLocation;
    private MarkerOptions mMarkerOptions;
    private Context mContext;
    private Post post;
    private Capsule capsule;
    private int type;

    /**
     * type 0 = Event
     * type 1 = Post
     * type 2 = Capsule
     * **/

    public EventMarker(Post post, Context context){
        this.post = post;

        mLocation = post.getLocation();

        mContext = context;
        type = 1;

        mMarkerOptions = new MarkerOptions().position(mLocation).title(post.getPostContent())
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("post", 75, 75)));
    }
    public EventMarker(Capsule capsule, Context context){
        this.capsule = capsule;
        mContext = context;
        mMarkerOptions = new MarkerOptions().position(mLocation).title(events.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("capsule", 75, 75)));
        type = 2;
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
        type = 0;
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

    public int getType(){
        return type;
    }
}
