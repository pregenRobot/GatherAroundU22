package com.example.android.gatheraround.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by tamimazmain on 2017/11/11.
 */

public class ContactFragmentTab extends Fragment {

    View rootView;

    Bitmap bitmap;
    ArrayList<UserProfileForFragment> contactContents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactContents = new ArrayList<>();

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



        for (final UserProfile profile : profiles){

            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference reference = storage.getReference().child(DataSenderToServer.IMAGE_REFERENCE_TITLE + "/" + profile.getUid());


            final RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.contactRecyclerView);
            final ContactFragmentAdapter contactFragmentAdapter = new ContactFragmentAdapter(contactContents,getContext());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(contactFragmentAdapter);

            StorageReference imageReference = storage.getReference().child(
                    DataSenderToServer.IMAGE_REFERENCE_TITLE).child(profile.getUid()).child(DataSenderToServer.IMAGE_REFERENCE_PROFILE);

            //Test

            Glide.with(getActivity())
                    .using(new FirebaseImageLoader())
                    .load(imageReference)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.v("Glider1","imageReady");
                            bitmap = resource;
                            contactContents.add(new UserProfileForFragment(profile.getUid(), profile.getName(), getCroppedBitmap(bitmap), profile.getProfileText()));
                            contactFragmentAdapter.notifyDataSetChanged();

                        }
                    });

            contactFragmentAdapter.notifyDataSetChanged();
        }

        Toast.makeText(getActivity(), "Tem. Message: Showing " + contactContents.size() + " contacts", Toast.LENGTH_SHORT).show();
        return rootView;
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
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
