package org.is_great.bensalcie.agrihub;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminActivity extends AppCompatActivity {
    private static final int GELLERY_REQUEST_CODE = 12345;
    private Button btnUpload;
    private EditText etTitle,etDescription;
    private ImageView kaleli;
    private Uri imageUri=null;
    private StorageReference evansStorage;
    private ProgressDialog pd;
    private DatabaseReference erickko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //getSupportActionBar().show();
        btnUpload=findViewById(R.id.btnUpload);
        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        kaleli=findViewById(R.id.postImage);
        pd=new ProgressDialog(AdminActivity.this);
        pd.setTitle("Just a moment");
        pd.setMessage("Uploading post...");
        evansStorage= FirebaseStorage.getInstance().getReference().child("Kilimo_Post_Images");
erickko= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Latest");

        //kaleli

        kaleli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GELLERY_REQUEST_CODE);
            }
        });
        //validation and upload

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                if (imageUri != null) {

                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {

                        //Toast.makeText(AdminActivity.this, "Data is ready for upload...", Toast.LENGTH_SHORT).show();
                        startUploading(title,description,imageUri);

                    }
                }else{
                    Toast.makeText(AdminActivity.this, "Please select some image...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startUploading(final String title, final String description, Uri imageUri) {

        String imageName=imageUri.getLastPathSegment().toString()+".jpg";
        final StorageReference naomi=evansStorage.child(imageName);
        naomi.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                naomi.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String downloadUrl=uri.toString();
                       // Toast.makeText(AdminActivity.this, "Image Url:"+downloadUrl, Toast.LENGTH_SHORT).show();
//Uploading to database

                        DatabaseReference kalondu=erickko.push();
                        kalondu.child("title").setValue(title);
                        kalondu.child("description").setValue(description);
                        kalondu.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    startActivity(new Intent(AdminActivity.this,MainActivity.class));
                                    finish();
                                    pd.dismiss();

                                    Toast.makeText(AdminActivity.this, "Congratulations...", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AdminActivity.this, "Divorce: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GELLERY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            imageUri=data.getData();
            kaleli.setImageURI(imageUri);


        }else{
            Toast.makeText(this, "An Error occured...", Toast.LENGTH_SHORT).show();
        }
    }



}
