package com.example.android.gatheraround;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.gatheraround.R.id.personImage;


public class ContactsAdapter extends ArrayAdapter<People> {

    Intent mainActivityintent;

    public int getcardbackground(){
        int color =  getContext().getResources().getColor(R.color.cardbackground);
        return  color;
    }
    public int getcardbackground2(){
        int color =  getContext().getResources().getColor(R.color.cardbackground2);
        return  color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_individual_list, parent, false);
        }
        LinearLayout card = listItemView.findViewById(R.id.cardItem);



        final People currentPerson = getItem(position);

        TextView personName = listItemView.findViewById(R.id.personName);

        personName.setText(currentPerson.getName());

        TextView personLocation = listItemView.findViewById(R.id.personLocation);

        personLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                mainActivityintent = new Intent(getContext(),MainActivity.class);
                //mainActivityintent.putExtra("location",currentPerson.getLocation());
                mainActivityintent.putExtra("latitude",currentPerson.getLocation().latitude);
                mainActivityintent.putExtra("longitude",currentPerson.getLocation().longitude);
                getContext().startActivity(mainActivityintent);

            }
        });



        de.hdodenhof.circleimageview.CircleImageView personImage = listItemView.findViewById(R.id.personImage);
        if(position%2!=0){
            card.setBackgroundColor(getcardbackground());
            personImage.setBorderColor(getcardbackground2());
        }else{
            card.setBackgroundColor(getcardbackground2());
            personImage.setBorderColor(getcardbackground());
        }

        personImage.setImageResource(currentPerson.getImage());
        return listItemView;

    }

    public ContactsAdapter(Activity context, ArrayList<People> people) {
        super(context,0, people);
    }
}
