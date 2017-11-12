package com.example.android.gatheraround;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.gatheraround.custom_classes.UserProfile;
import com.example.android.gatheraround.custom_classes.UserProfileForFragment;
import com.example.android.gatheraround.data.DatabaseHelper;
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

        Button flyToMyProfileButton = rootView.findViewById(R.id.flytomyprofile);
        final Intent intent = new Intent(getContext(),MyInfoActivity.class);

        flyToMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);

            }
        });

        DatabaseHelper helper = new DatabaseHelper(getActivity());
        ArrayList<UserProfile> profiles = helper.getAllUsers();

        ArrayList<UserProfileForFragment> arrayList = new ArrayList<>();
        for (UserProfile profile : profiles){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference().child(DataSenderToServer.IMAGE_REFERENCE_TITLE + "/" + profile.getUid());

            Glide.with(getActivity())
                    .load(reference)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            bitmap = resource;
                        }
                    });

            arrayList.add(new UserProfileForFragment(profile.getUid(), profile.getName(), bitmap, profile.getProfileText()));
        }

        final RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.contactRecyclerView);
        recyclerView.setAdapter(new ContactFragmentAdapter(arrayList, getActivity()));

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
