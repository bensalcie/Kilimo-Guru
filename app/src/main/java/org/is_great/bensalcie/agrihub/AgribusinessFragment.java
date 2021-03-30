package org.is_great.bensalcie.agrihub;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.roger.catloadinglibrary.CatLoadingView;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgribusinessFragment extends Fragment {
    private View v;
    private RecyclerView postList;
    private Query erickko;
    private CatLoadingView mView;


    public AgribusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_agribusiness, container, false);

        postList=v.findViewById(R.id.latest_list);
        //tvLoad=v.findViewById(R.id.tvLoad);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        mView = new CatLoadingView();
        mView.setText("...");

        erickko= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Explore").child("opportunity").limitToLast(6);
        erickko.keepSynced(true);


        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        mView.show(getActivity().getSupportFragmentManager(), "");

        FirebaseRecyclerAdapter<Calendar,myViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Calendar, myViewHolder>(Calendar.class,R.layout.single_explore,myViewHolder.class,erickko) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final Calendar model, int position) {
                mView.dismiss();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setDate(model.getDate());
                viewHolder.setBac();
            }
        };

        postList.setAdapter(recyclerAdapter);
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
            TextView tvTtile=mView.findViewById(R.id.tvETitle);
            tvTtile.setText(title);
        }
        public void setDescription(String title)
        {
            TextView tvTtile=mView.findViewById(R.id.tvEDesc);
            tvTtile.setText(title);
        }
        public void setDate(String title)
        {
            TextView tvTtile=mView.findViewById(R.id.tvEdATE);
            tvTtile.setText("  "+title+"  ");
        }
        public void setBac()
        {
            ImageView ivImg=mView.findViewById(R.id.ivNotify);
            ivImg.setImageDrawable(mView.getResources().getDrawable(R.drawable.ic_notifications_active_black_24dp));

        }


    }
}
