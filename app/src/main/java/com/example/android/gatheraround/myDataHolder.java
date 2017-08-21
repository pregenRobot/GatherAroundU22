package com.example.android.gatheraround;

import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class myDataHolder {

    private static ArrayList<People> peopleInContacts;
    private static ArrayList<Events> eventList;
    private static ArrayList<Participants> eventParticipantManager;

    /**
     * ちはるへのメッセージ
     *
     * これはデータ管理用クラスにしたよ
     * jSonナンチャラカンチャラから習得した情報をここでアップデートしておいてね
     * **/

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


        eventList.add(new Events(1302719286,"Tour de France",new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(1));
            add(peopleInContacts.get(2));
            add(peopleInContacts.get(3));
        }}),new LatLng(48.864716,2.349014),"Paris"));

        eventList.add(new Events(1502419296,"Soccer Tournament",new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(2));
            add(peopleInContacts.get(3));

        }}),new LatLng(-22.970722, -43.182365),"Rio de Janeiro"));

        eventList.add(new Events(1901719266,"Olympics",new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(5));
        }}),new LatLng(49.246292,-123.116226),"Vancouver"));
        eventList.add(new Events(1204219286,"School",new Participants(new ArrayList<People>(){{
            add(peopleInContacts.get(5));
            add(peopleInContacts.get(3));
            add(peopleInContacts.get(1));
        }}),new LatLng(35.654267, 139.722372),"Hiroo"));

    }


    public static void addnewEvent(long unixTime,
                                   String eventName,
                                   LatLng latitudelongitude,
                                   String locationName,
                                   final ArrayList<People> peoplegoing){
        Participants peopleJoining = new Participants(peoplegoing);
        eventList.add(new Events(unixTime,eventName,peopleJoining,latitudelongitude,locationName));
    }

    public static ArrayList<People> getContactList(){
        return peopleInContacts;
    }
    public static ArrayList<Events> getEventList(){
        return eventList;
    }

}
