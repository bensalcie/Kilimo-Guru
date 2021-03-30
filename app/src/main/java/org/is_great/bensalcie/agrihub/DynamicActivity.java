package org.is_great.bensalcie.agrihub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DynamicActivity extends AppCompatActivity {
    private TextView tvTitle,tvDetails,tvPrice,tvAddCart;
    private ImageView details_image;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private LinearLayout holder;
    private ProgressDialog pd;
    private  FloatingActionButton fab;
    private DatabaseReference newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        mAuth= FirebaseAuth.getInstance();
pd=new ProgressDialog(DynamicActivity.this);
pd.setTitle("Proccessing Your Order");
pd.setMessage("Adding your item to cart....");
        String user_id=mAuth.getUid();
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users").child(user_id).child("products");
        mUsersDatabase.keepSynced(true);
         newUser = mUsersDatabase.push();
        tvTitle=findViewById(R.id.tv_title);
        tvDetails=findViewById(R.id.tv_details);
        tvPrice=findViewById(R.id.tvPrice);
        tvAddCart=findViewById(R.id.tvAddCart);
        details_image=findViewById(R.id.details_image);
        String category=getIntent().getStringExtra("category");
        holder=findViewById(R.id.holder);
        setUpViews(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpViews(String category) {
        final String title=getIntent().getStringExtra("title");
        final String description=getIntent().getStringExtra("description");
        final String image=getIntent().getStringExtra("image");
        tvTitle.setText(title);
        tvDetails.setText(description);
        Glide.with(DynamicActivity.this).load(image).into(details_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toast("Sharing this item");



                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Please check up on this product,i found it amazing on Kilimo Guru App  .\n"+""+title+"\n \n About Product: "+description+" \n Find it at: \n" +
                        "https://play.google.com/store/apps/details?id=org.is_great.bensalcie.agrihub");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,title);
                startActivity(Intent.createChooser(shareIntent,"Share..."));
            }
        });
        if (category.equals("latest"))
        {


        }else{
            holder.setVisibility(View.VISIBLE);
            final String price=getIntent().getStringExtra("price");
            tvPrice.setText(" "+price+" :KSH");
            tvAddCart.setText("ADD TO CART");
            tvAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //addCart
                    pd.show();
                    addCart(title,price,image,description);
                   // attatchFb();


                }
            });

        }
    }


    private void addCart(String title, String price, String image, String description) {

        newUser.child("title").setValue(title);
        newUser.child("description").setValue(description);
        newUser.child("image").setValue(image);
        newUser.child("price").setValue(price).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    pd.dismiss();
                    toast("Added to cart Successfully");
                }else {
                    pd.dismiss();
                    toast("Error Adding to cart: "+task.getException().getMessage());
                }
            }
        });



    }

    private void toast(String s) {
        Toast.makeText(this, "Message: "+s, Toast.LENGTH_SHORT).show();
    }

}
