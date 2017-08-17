package com.example.android.gatheraround;

import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class myDataHolder {

    private static ArrayList<People> peopleInContacts;
    private static ArrayList<Events> eventList;
    private ArrayList<Participants> eventParticipantManager;

    public myDataHolder(){
        eventList = new ArrayList<Events>();
        peopleInContacts = new ArrayList<People>();
        eventParticipantManager = new ArrayList<Participants>();
        peopleInContacts.add(new People("Tamim Azmain",R.drawable.angelinajolie,new LatLng(48.964716,2.449014)));
        peopleInContacts.add(new People("Chiharu Miyoshi",R.drawable.stevejobs,new LatLng(40.730610, -73.935242)));
        peopleInContacts.add(new People("Steve Jobs",R.drawable.tedzukaosamu,new LatLng(-36.848461, 174.763336)));
        peopleInContacts.add(new People("MichaelJackson",R.drawable.unpressedbuttoncontacts,new LatLng(-34,45)));
        peopleInContacts.add(new People("Mory",R.drawable.unpressedbuttoncontacts,new LatLng(-37,45)));
        peopleInContacts.add(new People("手塚",R.drawable.unpressedbuttoncontacts,new LatLng(-34,45)));

        eventParticipantManager.add(new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(1));
            add(peopleInContacts.get(2));
            add(peopleInContacts.get(3));
        }}));
        eventParticipantManager.add(new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(2));
            add(peopleInContacts.get(3));

        }}));
        eventParticipantManager.add(new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(5));
        }}));
        eventParticipantManager.add(new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(5));
            add(peopleInContacts.get(3));
            add(peopleInContacts.get(1));
        }}));


        eventList.add(new Events(1302719286,"Tour de France",eventParticipantManager,new LatLng(48.864716,2.349014),"Paris"));
        eventList.add(new Events(1502419296,"Soccer Tournament",eventParticipantManager,new LatLng(-22.970722, -43.182365),"Rio de Janeiro"));
        eventList.add(new Events(1901719266,"Olympics",eventParticipantManager,new LatLng(49.246292,-123.116226),"Vancouver"));
        eventList.add(new Events(1204219286,"School",eventParticipantManager,new LatLng(35.654267, 139.722372),"Hiroo"));

    }

    public static ArrayList<People> getContactList(){
        return peopleInContacts;
    }
    public static ArrayList<Events> getEventList(){
        return eventList;
    }

}
