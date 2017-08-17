package com.example.android.gatheraround;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import java.util.List;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{

    Context mContext;
    List<Events> events;
    Calculations calculations = new Calculations();

    //Constructor
    RVAdapter(Context context,List<Events> events){

        this.events = events;
        mContext = context;
    }
    //Gets resource ID_white
    public int findResource(){
        int color =  mContext.getResources().getColor(R.color.white);
        return  color;
    }


    @Override
    public int getItemCount() {
        return events.size();
    }
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    //Method for setting independent info about each card from events Array List
    @Override
    public void onBindViewHolder(final EventViewHolder eventViewHolder, final int i) {
        //Gets refernce to each view in CardView
        eventViewHolder.eventName.setText(events.get(i).getName());
        eventViewHolder.eventLocation.setText(events.get(i).getLocationName());
        eventViewHolder.eventDate.setText(calculations.UnixTimeConverter(events.get(i).getUnixTimeStamp())[0]);
        eventViewHolder.eventTime.setText(calculations.UnixTimeConverter(events.get(i).getUnixTimeStamp())[1]);

        //Creates parameters for Linear Layout holding Participants
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (People x: events.get(i).getParticipants().get(i).getPeopleParticipating()) {
            TextView tv=new TextView(mContext);
            params.leftMargin=50;
            tv.setText(x.getName());
            tv.setTextSize((float) 16);
            tv.setPadding(10, 25, 10, 25);
            tv.setLayoutParams(params);
            tv.setTextColor(findResource());
            eventViewHolder.eventParticipants.addView(tv) ;
        }

        //Setting onClickListeners to Location view
        eventViewHolder.eventLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("RV Adapter",events.get(i).getLocationName());

                final CameraPosition location  = CameraPosition.builder().
                        target(events.get(i).getLocation()).zoom(14).build();

                MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(location));
                //mView.getBackground().setColorFilter(Color.parseColor(<Color code of your choice>), PorterDuff.Mode.DARKEN);


            }

        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView eventName;
        TextView eventTime;
        TextView eventLocation;
        TextView eventDate;
        LinearLayout eventParticipants;


        EventViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.CardViewItem);
            eventName = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDate = itemView.findViewById(R.id.event_date);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventParticipants = itemView.findViewById(R.id.event_participants);

        }

    }

}
