package com.example.android.gatheraround;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.People;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by tamimazmain on 2017/08/14.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{

    Context mContext;
    List<Events> events;
    Calculations calculations = new Calculations();
    Context Appcontext;

    //Constructor
    RVAdapter(Context context,List<Events> events){

        this.events = events;
        mContext = context;
        Appcontext = context.getApplicationContext();

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
        eventViewHolder.eventSummary.setText(events.get(i).getEventSummary());
        eventViewHolder.eventParticipants.setText(events.get(i).getParticipants()+"");

        //Setting onClickListeners to Location view
        eventViewHolder.eventLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("RV Adapter",events.get(i).getLocationName());

                final CameraPosition location  = CameraPosition.builder().
                        target(events.get(i).getLocation()).zoom(18).build();

                MainActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(location));
                //mView.getBackground().setColorFilter(Color.parseColor(<Color code of your choice>), PorterDuff.Mode.DARKEN);


            }

        });
        eventViewHolder.eventSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater mLayoutInflator;
                mLayoutInflator = LayoutInflater.from(Appcontext);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                View mView = mLayoutInflator.inflate(R.layout.summarydialog,null);
                TextView textView = mView.findViewById(R.id.mainText);
                textView.setText(events.get(i).getEventSummary());

                textView.setMovementMethod(new ScrollingMovementMethod());

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //
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
        TextView eventSummary;
        TextView eventParticipants;


        EventViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.CardViewItem);
            eventName = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDate = itemView.findViewById(R.id.event_date);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventParticipants = itemView.findViewById(R.id.event_participantNum);
            eventSummary = itemView.findViewById(R.id.event_summary);
        }

    }

}
