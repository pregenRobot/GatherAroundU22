package com.example.android.gatheraround;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/08/17.
 */

public class Participants {

    private ArrayList<People> peopleParticipating;
    private int index;

    public Participants(int Index, ArrayList<People> peopleInvolved){
        peopleParticipating = peopleInvolved;
        index = Index;
    }
    public Participants(ArrayList<People> peopleInvolved){
        peopleParticipating = peopleInvolved;
    }

    public ArrayList<People> getPeopleParticipating(){
        return  peopleParticipating;
    }
    public int getIndex(){
        return  index;
    }
}
