package com.example.android.gatheraround.custom_classes;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tamimazmain on 2017/11/13.
 */

public class Post {
    private String Information;
    private LatLng Location;

    public Post(LatLng place, String info){
        Location = place;
        Information = info;
    }

    public String getInformation(){
        return Information;
    }
    public LatLng getLocation(){
        return Location;
    }
}
