package com.example.android.gatheraround.activities;

import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.gatheraround.DataGetterFromServer;
import com.example.android.gatheraround.DataSenderToServer;
import com.example.android.gatheraround.R;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;


public class MyInfoActivity extends AppCompatActivity {

    TextView userNameTextView, profileTextView;
    CircularImageView profileImageView;
    ImageView backgroundImageView;
    FloatingActionButton addToContactButton;
    Button logoutButton;

    boolean isMyProfile;
    String userId;

    public static final String isMyProfile_Intent = "isMyProfile";
    public static final String userId_Intent = "userId";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    //when start from other Activity, put value to an intent, isMyProfile and uid.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        profileTextView = (TextView)findViewById(R.id.profileTextView);

        profileImageView = findViewById(R.id.profileImageView);
        backgroundImageView = findViewById(R.id.backgroundImageView);

        addToContactButton = findViewById(R.id.addToContactButton);

        logoutButton = findViewById(R.id.logoutButton);

        userId = getIntent().getStringExtra(userId_Intent);

        isMyProfile = getIntent().getBooleanExtra(isMyProfile_Intent, true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){

            Toast.makeText(MyInfoActivity.this, "Not signed in.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(MyInfoActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {

            String myUid = user.getUid();

            if (isMyProfile){
                addToContactButton.setVisibility(View.INVISIBLE);
                userId = myUid;
            } else {
                logoutButton.setVisibility(View.INVISIBLE);
            }

            showProfile(userId);

            if (isMyProfile){

                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent();
                        intent.setClass(MyInfoActivity.this, LoginActivity.class);
                        startActivity(intent);

//                    MyInfoActivity.this.finish();

                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
            } else {

                addToContactButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DatabaseHelper helper = new DatabaseHelper(MyInfoActivity.this);


                        Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_PROFILE_URL).child(userId);
                        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            String name;
                            String profile;

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                name = dataSnapshot.child(UserProfile.TITLE_NAME).getValue().toString();
                                profile = dataSnapshot.child(UserProfile.TITLE_PROFILE).getValue().toString();

                                UserProfile userProfile = new UserProfile(userId,"notPublic",name,profile);
                                boolean doesExist = helper.addNewUserToContactList(userProfile);
                                if (doesExist){
                                    Toast.makeText(MyInfoActivity.this, getResources().getString(R.string.contactsAlreadyExists), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MyInfoActivity.this, getResources().getString(R.string.contactsAddedToList), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });
            }
        }
    }

    public void showProfile(String uid){

        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;

        userNameTextView.setTextSize((float)(width/20));
        profileTextView.setTextSize((float)(width/20));

//                profileImageView.setMaxWidth(width/10);
//                profileImageView.setMaxHeight(width/10);

        final Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_PROFILE_URL).child(uid);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    userNameTextView.setText(dataSnapshot.child("mName").getValue().toString());
                    profileTextView.setText(dataSnapshot.child("mProfileText").getValue().toString());
                }catch (NullPointerException e){
                    Toast.makeText(MyInfoActivity.this,"Cannot Display info",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        StorageReference imageReference = storage.getReference().child(DataSenderToServer.IMAGE_REFERENCE_TITLE).child(uid).child(DataSenderToServer.IMAGE_REFERENCE_PROFILE);
        Glide.with(this).using(new FirebaseImageLoader()).load(imageReference).into(profileImageView);

        imageReference = storage.getReference().child(DataSenderToServer.IMAGE_REFERENCE_TITLE).child(uid).child(DataSenderToServer.IMAGE_REFERENCE_BACKGROUND);
        Glide.with(this).using(new FirebaseImageLoader()).load(imageReference).into(backgroundImageView);
    }
}
