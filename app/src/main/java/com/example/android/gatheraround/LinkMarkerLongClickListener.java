package com.example.android.gatheraround;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tamimazmain on 2017/08/30.
 */

public abstract class LinkMarkerLongClickListener implements GoogleMap.OnMarkerDragListener {

    private int previousIndex = -1;

    private Marker cachedMarker = null;
    private LatLng cachedDefaultPostion = null;

    private List<Marker> markerList;
    private List<LatLng> defaultPostions;

    public LinkMarkerLongClickListener(List<Marker> markerList){
        this.markerList = new ArrayList<>(markerList);
        defaultPostions = new ArrayList<>(markerList.size());
        for (Marker marker : markerList) {
            defaultPostions.add(marker.getPosition());
            marker.setDraggable(true);
        }
    }

    public abstract void onLongClickListener(Marker marker);

    @Override
    public void onMarkerDragStart(Marker marker) {
        onLongClickListener(marker);
        setDefaultPostion(markerList.indexOf(marker));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        setDefaultPostion(markerList.indexOf(marker));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        setDefaultPostion(markerList.indexOf(marker));
    }


    private void setDefaultPostion(int markerIndex) {
        if(previousIndex == -1 || previousIndex != markerIndex){
            cachedMarker = markerList.get(markerIndex);
            cachedDefaultPostion = defaultPostions.get(markerIndex);
            previousIndex = markerIndex;
        }
        cachedMarker.setPosition(cachedDefaultPostion);
    }
}