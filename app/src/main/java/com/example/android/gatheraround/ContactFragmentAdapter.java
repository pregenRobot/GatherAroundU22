package com.example.android.gatheraround;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.gatheraround.custom_classes.UserProfileForFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chiharu_miyoshi on 2017/11/11.
 */

public class ContactFragmentAdapter extends RecyclerView.Adapter<ContactFragmentAdapter.MyViewHolder> {

    private List<UserProfileForFragment> profileList;

    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView userNameTextView;
        public ImageView imageView;
        public CardView cardView;

        public MyViewHolder(View view){
            super(view);
            userNameTextView = view.findViewById(R.id.userNameTextView);
            imageView = view.findViewById(R.id.imageCardItem);
            cardView = view.findViewById(R.id.contactCard);
        }
    }

    public ContactFragmentAdapter(List<UserProfileForFragment> list, Context context){
        this.profileList = list;
        this.mContext = context;
        Log.v("Contact Adapter","Creating Constructor");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        final UserProfileForFragment profile = profileList.get(position);

        holder.userNameTextView.setText(profile.getUserName());
        holder.imageView.setImageBitmap(profile.getBitmap());

        final ContactFragmentTab contactFragmentTab = new ContactFragmentTab();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactFragmentTab.goToMyInfo(profile.getUid());
            }
        });
    }

    @Override
    public int getItemCount(){

        Log.v("ContactAdapter","" + profileList.size() + "");

        return profileList.size();
    }
}
