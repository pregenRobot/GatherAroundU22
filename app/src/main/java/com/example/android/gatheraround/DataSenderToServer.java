package com.example.android.gatheraround;

import com.example.android.gatheraround.custom_classes.Events;
import com.firebase.client.Firebase;
import com.firebase.client.core.Context;

/**
 * Created by chiharu_miyoshi on 2017/08/30.
 */

public class DataSenderToServer{

//    Context mContext;
//    DataSenderToServer(Context context){
//        this.mContext = context;
//    }

    public void pushToServer(Events newEvent){

        Firebase firebase = new Firebase("https://u22-project-gather-around.firebaseio.com/eventPostDetails");
        firebase.push().setValue(newEvent);
    }
}
