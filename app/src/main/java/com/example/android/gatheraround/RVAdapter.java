package com.example.android.gatheraround;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{

    Context mContext;
    List<Events> events;
    Calculations calculations = new Calculations();
    RVAdapter(Context context,List<Events> events){
        this.events = events;
        mContext = context;
    }
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
    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventName.setText(events.get(i).getName());
        eventViewHolder.eventLocation.setText(events.get(i).getLocationName());
        eventViewHolder.eventDate.setText(calculations.UnixTimeConverter(events.get(i).getUnixTimeStamp())[0]);
        eventViewHolder.eventTime.setText(calculations.UnixTimeConverter(events.get(i).getUnixTimeStamp())[1]);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        for (String x: events.get(i).getParticipants()) {
            TextView tv=new TextView(mContext);
            params.leftMargin=50;
            tv.setText(x);
            tv.setTextSize((float) 16);
            tv.setPadding(10, 25, 10, 25);
            tv.setLayoutParams(params);
            tv.setTextColor(findResource());
            eventViewHolder.eventParticipants.addView(tv) ;
        }
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
