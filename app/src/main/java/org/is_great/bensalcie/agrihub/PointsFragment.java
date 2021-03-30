package org.is_great.bensalcie.agrihub;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roger.catloadinglibrary.CatLoadingView;

import org.w3c.dom.Text;


public class PointsFragment extends Fragment {
    private View v;
    private TextView tvPoints;
    private DatabaseReference pointsDatabase;
    private FirebaseAuth mAuth;
    private CatLoadingView mView;



    public PointsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_points, container, false);
        tvPoints=v.findViewById(R.id.tvPoints);
        mView = new CatLoadingView();
        mView.setText("...");
        mAuth=FirebaseAuth.getInstance();
        String userId=mAuth.getUid();
        pointsDatabase= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users").child(userId);
        mView.show(getActivity().getSupportFragmentManager(), "");

        pointsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("points"))
                {
                    String data=dataSnapshot.child("points").getValue().toString();
                    tvPoints.setText(data);
                    mView.dismiss();

                }else{
                    pointsDatabase.child("points").setValue(50);
                    mView.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.dismiss();

            }
        });

        return v;
    }



}
