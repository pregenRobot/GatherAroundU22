package com.example.android.gatheraround;

import com.example.android.gatheraround.custom_classes.Events;
import com.firebase.client.Firebase;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class DataSenderToServer {

    public void pushToServer(Events newEvent){

        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/eventPostDetails");
        firebase.push().setValue(newEvent);
    }
}
