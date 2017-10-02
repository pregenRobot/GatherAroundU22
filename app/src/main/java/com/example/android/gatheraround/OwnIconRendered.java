package com.example.android.gatheraround;

import android.content.Context;
import android.graphics.Color;

import com.example.android.gatheraround.custom_classes.EventMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by tamimazmain on 2017/10/02.
 */

public class OwnIconRendered extends DefaultClusterRenderer<EventMarker> {

    Context context;

    public OwnIconRendered(Context context, GoogleMap map, ClusterManager<EventMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(EventMarker item,
                                               MarkerOptions markerOptions) {
        markerOptions.icon(item.getMarker().getIcon());
    }
    @Override
    protected int getColor(int clusterSize) {

        int returner = Color.CYAN;

        if(clusterSize>0&&clusterSize<9){
            returner = context.getResources().getColor(R.color.onedigit);
        }else if(clusterSize>=9&&clusterSize<99){
            returner = context.getResources().getColor(R.color.twodigit);
        }else if(clusterSize>=99&&clusterSize<999){
            returner = context.getResources().getColor(R.color.threedigit);
        }else if(clusterSize>=999){
            returner = context.getResources().getColor(R.color.fourdigit);
        }
        return returner;
    }
}
