package com.example.android.gatheraround;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.gatheraround.custom_classes.Events;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/11/07.
 */

public class ScrollFeedAdapter extends RecyclerView.Adapter<ScrollFeedAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Events> serverEvents = new ArrayList<>();
    Calculations calculations = new Calculations();

    public ScrollFeedAdapter(ArrayList<Events> receivedEvents, Context context){
        serverEvents = receivedEvents;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.firebasecards, parent, false);

        return new ScrollFeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Events event = serverEvents.get(position);
        String date1 = calculations.concatenate(event.getDate(),false,true)[0];
        String date2 = calculations.concatenate(event.getDate(),false,true)[1];

        holder.eventName.setText(event.getName());
        holder.date1.setText(date1);
        holder.date2.setText(date2);
        holder.summaryText.setText(event.getEventSummary());

    }

    @Override
    public int getItemCount() {
        return serverEvents.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public TextView eventName;
        public TextView date1;
        public TextView date2;
        public TextView summaryText;

        public MyViewHolder (View view){
            super(view);
            eventName = view.findViewById(R.id.eventNamefirebase);
            date1 = view.findViewById(R.id.eventDateFirebase);
            date2 = view.findViewById(R.id.eventTimeFirebase);
            summaryText = view.findViewById(R.id.eventSmmaryFirebase);

        }
    }

}
