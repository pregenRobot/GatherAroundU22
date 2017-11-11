package com.example.android.gatheraround;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyInfoActivity extends AppCompatActivity {

    TextView userNameTextView, profileTextView;
    ImageView profileImageView;

    boolean isMyProfile;

    public static final String isMyProfile_Intent = "isMyProfile";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    //when start from other Activity, put value to an intent, isMyProfile.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        profileTextView = (TextView)findViewById(R.id.profileTextView);

        profileImageView = (ImageView)findViewById(R.id.profileImageView);

        isMyProfile = getIntent().getBooleanExtra(isMyProfile_Intent, true);

        if (isMyProfile) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user == null){
                Toast.makeText(MyInfoActivity.this, "Not sighed in.", Toast.LENGTH_SHORT).show();
            }else{
                String uid = user.getUid();

                final Firebase firebase = new Firebase(DataSenderToServer.FIREBASE_PROFILE_URL + "/" + uid);
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userNameTextView.setText(dataSnapshot.child("mEmail").getValue().toString());
                        profileTextView.setText(dataSnapshot.child("mName").getValue().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                StorageReference imageReference = storage.getReference().child(DataSenderToServer.IMAGE_REFERENCE_TITLE + "/" + uid);

                Glide.with(this).using(new FirebaseImageLoader()).load(imageReference).into(profileImageView);
            }
        }
    }
}
