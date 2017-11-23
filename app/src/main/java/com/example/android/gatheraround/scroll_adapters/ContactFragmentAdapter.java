package com.example.android.gatheraround.scroll_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gatheraround.activities.MyInfoActivity;
import com.example.android.gatheraround.fragments.ContactFragmentTab;
import com.example.android.gatheraround.R;
import com.example.android.gatheraround.custom_classes.UserProfileForFragment;

import java.util.List;

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

        Toast.makeText(mContext, "Temp. Message: Showing " + list.size() + " contacts", Toast.LENGTH_SHORT).show();
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
                goToMyInfo(profile.getUid());

            }
        });
    }
    public void goToMyInfo(String uid){
        final Intent intent = new Intent(mContext, MyInfoActivity.class);
        intent.putExtra(MyInfoActivity.isMyProfile_Intent, false);
        intent.putExtra(MyInfoActivity.userId_Intent, uid);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount(){

        Log.v("ContactAdapter","" + profileList.size() + "");

        return profileList.size();
    }
}
