package com.example.android.gatheraround;

import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.Participants;
import com.example.android.gatheraround.custom_classes.People;
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
        peopleInContacts.add(new People("Tamim Azmain",R.drawable.angelinajolie,new LatLng(48.964716,2.449014),"ta1130"));
        peopleInContacts.add(new People("Chiharu Miyoshi",R.drawable.stevejobs,new LatLng(40.730610, -73.935242),"chiharum"));
        peopleInContacts.add(new People("Steve Jobs",R.drawable.tedzukaosamu,new LatLng(-36.848461, 174.763336),"steve_jobs"));
        peopleInContacts.add(new People("MichaelJackson",R.drawable.unpressedbuttoncontacts,new LatLng(-34,45),"michael"));
        peopleInContacts.add(new People("Mory",R.drawable.unpressedbuttoncontacts,new LatLng(-37,45),"mooory"));
        peopleInContacts.add(new People("手塚",R.drawable.unpressedbuttoncontacts,new LatLng(-34,45),"atom"));

        String eventSummary = "I had tried using three different versions of Odin, 3.07, 3.09, 3.106. I also used multiple cords and multiple versions of stock roms. And still, no success. It won't even stay off, every time it juices up, or is plugged in, it goes straight to the Samsung Galaxy S6 Edge boot screen and stays there. \n" +
                "\n" +
                "Mind you, I am not too familiar with anything outside of stock roms, some custom roms and rooting. Have not tested out much more than that. Any noob-friendly advice would be welcomed.";

        eventList.add(new Events(1302719286,"Tour de France",12,new LatLng(48.864716,2.349014),"Paris",""));

        eventList.add(new Events(1502419296,"Soccer Tournament",34,new LatLng(-22.970722, -43.182365),"Rio de Janeiro",eventSummary));

        eventList.add(new Events(1901719266,"Olympics",31,new LatLng(49.246292,-123.116226),"Vancouver",eventSummary));
        eventList.add(new Events(1204219286,"School",19,new LatLng(35.654267, 139.722372),"Hiroo",""));

    }

    public static ArrayList<People> getContactList(){
        return peopleInContacts;
    }
    public static ArrayList<Events> getEventList(){
        return eventList;
    }

}
