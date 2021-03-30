package org.is_great.bensalcie.agrihub;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.roger.catloadinglibrary.CatLoadingView;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment {

    private RecyclerView postList;
    private DatabaseReference erickko;
    private  View v;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference newUser;
    private CatLoadingView mView;
    private Button btnCheckout;



    public MarketFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_market, container, false);

        postList=v.findViewById(R.id.latest_list);
        mView = new CatLoadingView();
        mView.setText("...");
        btnCheckout=v.findViewById(R.id.btnCheckout);
        mAuth=FirebaseAuth.getInstance();
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users");

        String user_id=mAuth.getUid();
        newUser = mUsersDatabase.child(user_id).child("products");


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Service is under maintainance for now, we will update you once its functional..Thank you", Toast.LENGTH_LONG).show();
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //startAnim();
        mView.show(getActivity().getSupportFragmentManager(), "");

        attatch(newUser);
        //stopAnim();
    }

    private void attatch(Query newUser) {
        FirebaseRecyclerAdapter<Cart,myViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Cart, myViewHolder>(Cart.class,R.layout.cart_post,myViewHolder.class,newUser) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final Cart model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
               // viewHolder.setimage(model.getImage(),getActivity());
               viewHolder.setPrice(model.getPrice());
                postList.scrollToPosition(4);
                mView.dismiss();

                //stopAnim();
                /*viewHolder.mView.setOnClickListener(new View.OnClickListener() {
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
                });*/
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


        public void setPrice(String price) {
            TextView tvTtile=mView.findViewById(R.id.tvPrice);
            tvTtile.setText("Ksh: "+price);
        }
    }


}
