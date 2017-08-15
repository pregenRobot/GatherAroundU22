package com.example.android.gatheraround;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{

    List<Events> events;
    Calculations calculations = new Calculations();
    RVAdapter(List<Events> events){
        this.events = events;
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
    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventName.setText(events.get(i).getName());
        eventViewHolder.eventLocation.setText(events.get(i).getLocationName());
        eventViewHolder.eventDate.setText(calculations.UnixTimeConverter(events.get(i).getUnixTimeStamp())[0]);
        eventViewHolder.eventTime.setText(calculations.UnixTimeConverter(events.get(i).getUnixTimeStamp())[1]);
        eventViewHolder.eventParticipants.setText(calculations.ParticipantConcatenation(events.get(i).getParticipants()));

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
        TextView eventParticipants;


        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CardViewItem);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventTime = (TextView)itemView.findViewById(R.id.event_name);
            eventDate = (TextView)itemView.findViewById(R.id.event_date);
            eventLocation = (TextView)itemView.findViewById(R.id.event_location);

        }

    }

}
