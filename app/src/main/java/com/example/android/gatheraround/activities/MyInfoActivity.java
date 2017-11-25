package com.example.android.gatheraround.activities;

import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.android.gatheraround.MainActivity;
import com.example.android.gatheraround.R;
import com.example.android.gatheraround.custom_classes.EventDate;
import com.example.android.gatheraround.custom_classes.EventMarker;
import com.example.android.gatheraround.custom_classes.Post;
import com.example.android.gatheraround.custom_classes.PostForFragment;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.example.android.gatheraround.fragments.MapFragmenttab;
import com.example.android.gatheraround.scroll_adapters.ContactFragmentAdapter;
import com.example.android.gatheraround.scroll_adapters.MyInfoScrollAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class MyInfoActivity extends AppCompatActivity {

    TextView userNameTextView, profileTextView;
    CircularImageView profileImageView;
    ImageView backgroundImageView;
    FloatingActionButton addToContactButton;
    Button logoutButton;
    ArrayList<String> myPostsString;
    ArrayList<Post> myPostArray;

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
        myPostArray = MapFragmenttab.serverPosts;
        myPostsString = new ArrayList<String>();

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
                                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myinforecyclerview);

                                ArrayList<PostForFragment> postForFragments = new ArrayList<>();
                                for(Post myPost:myPostArray){
                                    postForFragments.add(new PostForFragment(myPost,userId));
                                }
                                MyInfoScrollAdapter contactFragmentAdapter = new MyInfoScrollAdapter(MyInfoActivity.this,postForFragments);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyInfoActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(contactFragmentAdapter);

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

    private void showProfile(String uid){

        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);


//                profileImageView.setMaxWidth(width/10);
//                profileImageView.setMaxHeight(width/10);
        final Firebase firebase1 = new Firebase(DataSenderToServer.FIREBASE_PROFILE_URL).child(uid).child("myposts");
        firebase1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    myPostsString.add(snapshot.getValue().toString());
                }

                for (String x:myPostsString){
                    Log.v("Posts1", x);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
