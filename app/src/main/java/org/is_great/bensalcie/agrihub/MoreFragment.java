package org.is_great.bensalcie.agrihub;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roger.catloadinglibrary.CatLoadingView;


public class MoreFragment extends Fragment {
    private View v;
   private DatabaseReference walletDatabase;
   private FirebaseAuth mAuth;
   private TextView tvPoints,tvKsh,tvRatings;
    private CatLoadingView mView;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_more, container, false);
        tvPoints=v.findViewById(R.id.tvPoints);
        tvKsh=v.findViewById(R.id.tvKsh);
        tvRatings=v.findViewById(R.id.tvRatings);
        tvRatings.setText("20-50 Points          Ksh: 5 Redeemable\n50-70 Points          Ksh: 10 Redeemable\n70-100 Points         Ksh: 15 Redeemable\n100-200 Points        Ksh: 50 Redeamable\n200-500 Points        Ksh: 100 Redeamable");
        mView = new CatLoadingView();
        mView.setText("...");
        mAuth=FirebaseAuth.getInstance();
        String user_id=mAuth.getUid();
        walletDatabase= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users").child(user_id);
        mView.show(getActivity().getSupportFragmentManager(), "");

        walletDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("points"))
                {
                    String data=dataSnapshot.child("points").getValue().toString();
                    tvPoints.setText(data);
                    int ksh=Integer.parseInt(data);
                    int money=0;
                    if (ksh>20 && ksh<=50)
                    {
                       money=5;
                       tvKsh.setText("Ksh:"+money);
                    }else if (ksh>50 && ksh<=70)
                    {
                        money=10;
                        tvKsh.setText("Ksh:"+money);

                    }else if (ksh>70 && ksh<=100)
                    {
                        money=15;
                        tvKsh.setText("Ksh:"+money);

                    }
                    else if (ksh>100 && ksh<=200)
                    {
                        money=50;
                        tvKsh.setText("Ksh:"+money);

                    }
                    else if (ksh>200 && ksh<=500)
                    {
                        money=100;
                        tvKsh.setText("Ksh:"+money);

                    }
                    mView.dismiss();

                }else{

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
