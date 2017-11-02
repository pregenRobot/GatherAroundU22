package com.example.android.gatheraround;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyInfoActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    TextView userNameTextView, profileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        profileTextView = (TextView)findViewById(R.id.profileTextView);

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
        }
    }
}
