package com.example.android.gatheraround.scroll_adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.gatheraround.R;
import com.example.android.gatheraround.custom_classes.Post;
import com.example.android.gatheraround.custom_classes.PostForFragment;
import com.example.android.gatheraround.processes.Calculations;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by tamimazmain on 2017/11/25.
 */

public class MyInfoScrollAdapter extends RecyclerView.Adapter<MyInfoScrollAdapter.MyViewHolder>{

    private ArrayList<PostForFragment> postarray;
    private Context mContext;
    Calculations calculations = new Calculations();

    public MyInfoScrollAdapter(Context Context, ArrayList<PostForFragment> posts){
        postarray = posts;
        mContext = Context;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postcarditem_scroller, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PostForFragment currentpostfrag = postarray.get(position);
        final Post currentpost = postarray.get(position).getPost();
        holder.postDate.setText(calculations.concatenate(currentpost.getPostDate(),false,false)[0]);
        holder.poster.setText(currentpostfrag.getUserName());
        holder.postDetails.setText(currentpost.getPostContent());

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView
                poster,
                postDetails,
                postDate;

        public LinearLayout cardparent;

        public MyViewHolder(View view) {
            super(view);
            poster = view.findViewById(R.id.poster);
            postDetails = view.findViewById(R.id.postdetails);
            postDate = view.findViewById(R.id.postdate);
            cardparent = view.findViewById(R.id.posterparentCard);

        }
    }
}
