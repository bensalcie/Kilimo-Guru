package org.is_great.bensalcie.agrihub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int GALLERY_REQUEST_CODE = 2345;
    private FirebaseAuth mAuth;
    private String category="";
    private DatabaseReference mUsersDatabase;
    private FloatingActionButton fab;
    private Uri imageUri=null;
    private  ImageView photo;
    private StorageReference mProductsStorage;
    private ProgressDialog pd;
    private DatabaseReference erickko;
    private DatabaseReference newUser;
    private int val=0;
    private DatabaseReference exploreDb;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header));
mAuth=FirebaseAuth.getInstance();
mProductsStorage= FirebaseStorage.getInstance().getReference().child("Kilimo");
 erickko= FirebaseDatabase.getInstance().getReference().child("Kilimo_Farmer_Products");
        exploreDb= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Explore");

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users");

        fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setVisibility(View.GONE);
         attatchLatest();
         pd=new ProgressDialog(MainActivity.this);
         pd.setTitle("Just a moment");
         pd.setMessage("Selling product...");

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        FirebaseUser mCurrentUser=mAuth.getCurrentUser();
                if (mCurrentUser!=null)
                {
        String adminUser=mAuth.getCurrentUser().getEmail().toString();
            if (adminUser.equals("admin@gmail.com")) {
                getMenuInflater().inflate(R.menu.main_admin, menu);

            } else {
                getMenuInflater().inflate(R.menu.main, menu);

            }
                }else{
                   // toast("Something went wrong");
                }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String adminUser=mAuth.getCurrentUser().getEmail().toString();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
           builder.setTitle("Warning");
            builder.setMessage("Do you want to Logout? ");
           builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                  mAuth.signOut();
                  sendToLogin();               }
           });
           builder.setNegativeButton("LATER",null);
           builder.setNeutralButton("Learn More",null);
           builder.show();
            return true;
        }else if (id==R.id.action_points)
        {
            PointsFragment notificationFragment=new PointsFragment();
            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
            ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
            ft2.replace(R.id.main_home,notificationFragment);
            ft2.commit();
            String tit="My Points";
            setActionTitle(tit);
        }
        if (adminUser.equals("admin@gmail.com"))
        {
            if(id==R.id.action_admin)
            {
                startActivity(new Intent(MainActivity.this,AdminActivity.class));

            }else if (id==R.id.action_calender)
            {
                String explore1="calender";
                inputExplore(explore1);

            }else if (id==R.id.action_opportunities)
            {
                String explore2="opportunity";
                inputExplore(explore2);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendToLogin() {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            attatchLatest();
            String tit="Latest";
            setActionTitle(tit);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            ConnectionsFragment notificationFragment=new ConnectionsFragment();
            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
            ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
            ft2.replace(R.id.main_home,notificationFragment);
            ft2.commit();
            String tit="Shopping";
            setActionTitle(tit);

        } else if (id == R.id.nav_slideshow) {

            MarketFragment notificationFragment=new MarketFragment();
            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
            ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
            ft2.replace(R.id.main_home,notificationFragment);
            ft2.commit();
            String tit="My Cart";
            setActionTitle(tit);

        } else if (id == R.id.nav_manage) {
            MoreFragment notificationFragment=new MoreFragment();
            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
            ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
            ft2.replace(R.id.main_home,notificationFragment);
            ft2.commit();
            String tit="My Wallet";
            setActionTitle(tit);

        } else if (id == R.id.nav_share) {
            CalenderOppotunityFragment notificationFragment=new CalenderOppotunityFragment();
            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
            ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
            ft2.replace(R.id.main_home,notificationFragment);
            ft2.commit();
            String tit="Latest On Our Calendar";
            setActionTitle(tit);

        } else if (id == R.id.nav_send) {
            AgribusinessFragment notificationFragment=new AgribusinessFragment();
            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
            ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
            ft2.replace(R.id.main_home,notificationFragment);
            ft2.commit();
            String tit="Opportunities for You";
            setActionTitle(tit);
        }
        else if (id==R.id.friend)
        {

            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Amazing App"+"\n Download this app to get many opportunities at:     \n\n" +
                    "https://play.google.com/store/apps/details?id=org.is_great.bensalcie.agrihub ");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Developed by ben salcie");
            startActivity(Intent.createChooser(shareIntent,"Share..."));

        }else if (id==R.id.share)
        {

            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Amazing App"+"\n Download this app to get many opportunities  at : \n" +
                    "https://play.google.com/store/apps/details?id=org.is_great.bensalcie.agrihub");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Developed by ben salcie");
            startActivity(Intent.createChooser(shareIntent,"Share..."));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionTitle(String tit) {
        getSupportActionBar().setTitle(tit);
    }

    private void attatchLatest() {
        LatestFragment notificationFragment=new LatestFragment();
        FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
        ft2.setCustomAnimations(R.anim.exit_left,R.anim.exit_left);
        ft2.replace(R.id.main_home,notificationFragment);
        ft2.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser==null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }else{
            String user_id=mAuth.getUid();
            if (!TextUtils.isEmpty(user_id)) {
                mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users");
                DatabaseReference newUser = mUsersDatabase.child(user_id);
                mUsersDatabase.keepSynced(true);
                erickko.keepSynced(true);

                newUser.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("category"))
                        {

                            String name=dataSnapshot.child("name").getValue().toString();
                            category=dataSnapshot.child("category").getValue().toString();
                            getSupportActionBar().setTitle(name);
                            //Toast.makeText(MainActivity.this, "Category: "+category, Toast.LENGTH_SHORT).show();
                           setFloatButton(category);
                            appendData(category);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else{
                Toast.makeText(this, "Error with getting user_id", Toast.LENGTH_SHORT).show();
            }


            //Nothing stay here
            attatchLatest();

        }
    }

    @SuppressLint("RestrictedApi")
    private void setFloatButton(String category) {
        attatchFb();
        if (category.equals("Farmer"))
        {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//Posting here
                    Button btnPost;
                    final EditText etTitle,etDesctiption,etPrice;
                    final Dialog d=new Dialog(MainActivity.this);
                    d.setTitle("Sell your Product here...");
                    d.setContentView(R.layout.post_dialog);
                    btnPost=d.findViewById(R.id.btnSell);
                    etTitle=d.findViewById(R.id.etTitle);
                    etDesctiption=d.findViewById(R.id.etDescription);
                    etPrice=d.findViewById(R.id.etAmount);
                    photo=d.findViewById(R.id.product_photo);

                    photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
                        }
                    });
                    btnPost.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            final String title = etTitle.getText().toString().trim();
                            final String desc = etDesctiption.getText().toString().trim();
                            final String price = etPrice.getText().toString().trim();
                            if (imageUri != null) {

                                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(price)) {
                                    pd.show();

                                    //toast("Data is ready for upload...");
                                    String imageName=imageUri.getLastPathSegment().toString()+".jpg";
                                   final StorageReference file_path=mProductsStorage.child("product_images").child(imageName);

                                   file_path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                           file_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                               @Override
                                               public void onSuccess(Uri uri) {

                                                   String download_url=uri.toString();
                                                   DatabaseReference newProduct=erickko.push();
                                                   newProduct.child("title").setValue(title);
                                                   newProduct.child("description").setValue(desc);
                                                   newProduct.child("price").setValue(price);
                                                   newProduct.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {

                                                           if (task.isSuccessful())
                                                           {
                                                               pd.dismiss();
                                                               toast("Success... Wait for Customers");
                                                               d.dismiss();
                                                           }else{
                                                               pd.dismiss();
                                                               toast("Error: "+task.getException().getMessage());
                                                           }
                                                       }
                                                   });

                                                  // toast("Download: "+download_url);
                                               }
                                           });
                                       }
                                   });

                                }else{
                                    pd.dismiss();
                                    toast("Please check your inputs....");
                                }
                            }else{
                                pd.dismiss();
                                toast("Please tap the avatar to pick your product image...");
                            }
                        }
                    });
                    d.show();
                }
            });
        }

    }


    private void appendData(String category) {
        //our data comes here
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            imageUri=data.getData();
            photo.setImageURI(imageUri);


        }else{

            toast("Something went wrong...while choosing image");

        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    private int attatchFb() {

        String user_id = mAuth.getUid();
        if (!TextUtils.isEmpty(user_id)) {
            newUser = mUsersDatabase.child(user_id).child("products");

            newUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    //toast("Count: "+dataSnapshot.getChildrenCount());
                    val = (int) dataSnapshot.getChildrenCount();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return val;

        }
        return 0;
    }
    private void inputExplore(final String explore1) {
         final CatLoadingView mView = new CatLoadingView();
        mView.setText("...");
        final Dialog d=new Dialog(this);
        d.setContentView(R.layout.explore_dialog);

        final EditText etTitle,etDesc,etDateTime;
        etTitle=d.findViewById(R.id.etTitle);
        etDesc=d.findViewById(R.id.etDescription);
        etDateTime=d.findViewById(R.id.etDateTime);


        Button btnPost=d.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.show(getSupportFragmentManager(), "");

                //Toast.makeText(MainActivity.this, "Ready to go...", Toast.LENGTH_SHORT).show();


                String title=etTitle.getText().toString().trim();
                String desc=etDesc.getText().toString().trim();
                String dateTime=etDateTime.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(dateTime))
                {
                    DatabaseReference newExplore=exploreDb.child(explore1).push();
                    newExplore.child("title").setValue(title);
                    newExplore.child("description").setValue(desc);
                    newExplore.child("date").setValue(dateTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mView.dismiss();
                                d.dismiss();

                            }else{
                                mView.dismiss();
                                toast("Error: "+task.getException().getMessage());
                            }
                        }
                    });


                }else{
                    toast("An Error occured with inputs...");
                }
            }
        });
        d.show();

    }
}
