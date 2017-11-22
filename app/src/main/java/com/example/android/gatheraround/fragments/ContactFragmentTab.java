package com.example.android.gatheraround.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.gatheraround.DataSenderToServer;
import com.example.android.gatheraround.activities.MyInfoActivity;
import com.example.android.gatheraround.R;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.example.android.gatheraround.custom_classes.UserProfileForFragment;
import com.example.android.gatheraround.data.DatabaseHelper;
import com.example.android.gatheraround.scroll_adapters.ContactFragmentAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/11/11.
 */

public class ContactFragmentTab extends Fragment {

    View rootView;

    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.contact_recycler,container,false);

        final EditText uidSearchEditText = rootView.findViewById(R.id.uidSearchEditText);
        Button searchButton = rootView.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableStringBuilder builder = (SpannableStringBuilder)uidSearchEditText.getText();
                String uid = builder.toString();

                Intent intent = new Intent();
                intent.setClass(getContext(), MyInfoActivity.class);
                intent.putExtra(MyInfoActivity.userId_Intent, uid);
                intent.putExtra(MyInfoActivity.isMyProfile_Intent, false);
                startActivity(intent);
            }
        });

        Button flyToMyProfileButton = rootView.findViewById(R.id.flytomyprofile);
        final Intent intent = new Intent(getContext(),MyInfoActivity.class);

        flyToMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);

            }
        });

        DatabaseHelper helper = new DatabaseHelper(getActivity());
        ArrayList<UserProfile> profiles = helper.getAllContacts();

        for(UserProfile profile:profiles){
            Log.v("Youhavenofriends",profile.toString());
        }

        Toast.makeText(getActivity(), "Temp. Message: There are " + profiles.size() + " users in contact", Toast.LENGTH_SHORT).show();

        ArrayList<UserProfileForFragment> contactContents = new ArrayList<>();

        for (UserProfile profile : profiles){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference().child(DataSenderToServer.IMAGE_REFERENCE_TITLE + "/" + profile.getUid());

            Glide.with(getActivity())
                    .using(new FirebaseImageLoader())
                    .load(reference)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            bitmap = resource;
                        }
                    });

            contactContents.add(new UserProfileForFragment(profile.getUid(), profile.getName(), bitmap, profile.getProfileText()));
        }

        Toast.makeText(getActivity(), "Tem. Message: Showing " + contactContents.size() + " contacts", Toast.LENGTH_SHORT).show();


        final RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.contactRecyclerView);
        ContactFragmentAdapter contactFragmentAdapter = new ContactFragmentAdapter(contactContents,getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactFragmentAdapter);


        return rootView;
    }

    public void goToMyInfo(String uid){
        final Intent intent = new Intent();
        intent.setClass(getActivity(), MyInfoActivity.class);
        intent.putExtra(MyInfoActivity.isMyProfile_Intent, false);
        intent.putExtra(MyInfoActivity.userId_Intent, uid);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
