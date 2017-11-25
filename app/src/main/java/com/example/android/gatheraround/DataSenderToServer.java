package com.example.android.gatheraround;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.Capsule;
import com.example.android.gatheraround.custom_classes.Events;
import com.example.android.gatheraround.custom_classes.Post;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DataSenderToServer{

    public static final String FIREBASE_EVENT_URL = "https://u22-project-gather-around.firebaseio.com/eventPostDetails";
    public static final String FIREBASE_POST_URL = "https://u22-project-gather-around.firebaseio.com/postDetails";
    public static final String FIREBASE_CAPSULE_URL = "https://u22-project-gather-around.firebaseio.com/capsuleDetails";
    public static final String FIREBASE_PROFILE_URL = "https://u22-project-gather-around.firebaseio.com/users";

    public static final String USERS_REFERENCE_TITLE = "users";
    public static final String IMAGE_REFERENCE_TITLE = "images";
    public static final String IMAGE_REFERENCE_PROFILE = "profile";
    public static final String IMAGE_REFERENCE_BACKGROUND = "background";

    public String pushToServer(Events newEvent){

        Firebase firebase = new Firebase(FIREBASE_EVENT_URL);
        Firebase push = firebase.push();
        push.setValue(newEvent);
        String key = push.getKey();
        firebase.child(key).child("key").setValue(key);

        return key;
    }

    public void eraseEntry(String key){

        Firebase firebase = new Firebase(FIREBASE_EVENT_URL + "/" + key);
        firebase.removeValue();
    }

    public void addOneParticipants(String key){
        Firebase firebase = new Firebase(FIREBASE_EVENT_URL + "/" + key + "/participants");
        firebase.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                if (mutableData.getValue() == null){
                    mutableData.setValue(1);
                }else{
                    int count = mutableData.getValue(Integer.class);
                    count++;
                    mutableData.setValue(count);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                if (!b){
                    Log.e("Failed to add", firebaseError.toString());
                }
            }
        });
    }

    // send profile to server
    public void addNewUser(UserProfile profile, Uri profileImageUri, Uri backgroundImageUri){

        //Upload profile
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(USERS_REFERENCE_TITLE).child(profile.getUid()).setValue(profile);

        //upload image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference();
        //for profile
        StorageReference imageReference = reference.child(IMAGE_REFERENCE_TITLE).child(profile.getUid()).child(IMAGE_REFERENCE_PROFILE);
        imageReference.putFile(profileImageUri);
        //for background
        imageReference = reference.child(IMAGE_REFERENCE_TITLE).child(profile.getUid()).child(IMAGE_REFERENCE_BACKGROUND);
        imageReference.putFile(backgroundImageUri);
    }

    public String sendNewPost(Post post){

        Firebase firebase = new Firebase(FIREBASE_POST_URL);

        Firebase push = firebase.push();
        push.setValue(post);
        String key = push.getKey();
        firebase.child(key).child("postId").setValue(key);

        return key;
    }

    public String sendNewCapsule(Capsule capsule){

        Firebase firebase = new Firebase(FIREBASE_CAPSULE_URL);

        Firebase push = firebase.push();
        push.setValue(capsule);
        String key = push.getKey();
        firebase.child(key).child("capsuleId").setValue(key);

        return key;
    }

    public void addLikeToPost(String postId){

        Log.i("likes_sender", "id: " + postId);

        final Firebase firebase = new Firebase(FIREBASE_POST_URL);
        firebase.child(postId).child("likes");
//        firebase.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//
//                Long count = mutableData.getValue(Long.class);
//
//                Log.i("likes_sender_ongoing", "Likes: " + String.valueOf(count));
//
//                int countInt = mutableData.getValue(Integer.class);
//
//                Log.i("likes_sender_ongoing", "Likes_int: " + String.valueOf(countInt));
//
//                count++;
//                mutableData.setValue(count);
//
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
//                if (!b){
//                    Log.i("likes_sender", "Failed to add likes");
//                }
//            }
//        });

        final Firebase firebase1 = new Firebase(FIREBASE_POST_URL).child(postId).child("likes");
        firebase1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = Integer.parseInt(dataSnapshot.getValue().toString());
                count++;
                firebase1.setValue(count);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
