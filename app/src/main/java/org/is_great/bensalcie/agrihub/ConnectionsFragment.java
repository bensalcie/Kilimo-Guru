package org.is_great.bensalcie.agrihub;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionsFragment extends Fragment {
    private RecyclerView postList;
    private DatabaseReference erickko;
private  View v;
    private AVLoadingIndicatorView iv;


    public ConnectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_connections, container, false);
        iv=v.findViewById(R.id.avi);

        postList=v.findViewById(R.id.latest_list);
       // LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
       // linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.getStackFromEnd();
        GridLayoutManager linearLayoutManager=new GridLayoutManager(getContext(),2);
        //linearLayoutManager.scrollToPosition(4);
        linearLayoutManager.setReverseLayout(true);
        postList.setLayoutManager(linearLayoutManager);
        erickko= FirebaseDatabase.getInstance().getReference().child("Kilimo_Farmer_Products");


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        startAnim();

        FirebaseRecyclerAdapter<Shopping,myViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Shopping, myViewHolder>(Shopping.class,R.layout.single_post,myViewHolder.class,erickko) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final Shopping model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setimage(model.getImage(),getActivity());
                postList.scrollToPosition(4);
                stopAnim();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(getContext(),DynamicActivity.class);
                        in.putExtra("category","products");
                        in.putExtra("title",model.getTitle());
                        in.putExtra("description",model.getDescription());
                        in.putExtra("image",model.getImage());
                        in.putExtra("price",model.getPrice());
                        startActivity(in);

                    }
                });
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
        postList.setVisibility(View.VISIBLE);
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

        }
        public void setTitle(String title)
        {
            TextView tvTtile=mView.findViewById(R.id.tvTitle);
            tvTtile.setText(title);
        }
        public void setDescription(String title)
        {
            TextView tvTtile=mView.findViewById(R.id.tvDescription);
            tvTtile.setText(title);
        }
        public void setimage(String image,Context ctx)
        {
            ImageView tvTtile=mView.findViewById(R.id.postImage);

            Glide.with(ctx).load(image).into(tvTtile);
        }
    }
    void startAnim()
    {
        iv.show();
    }
    void stopAnim()
    {
        iv.hide();
    }
}
