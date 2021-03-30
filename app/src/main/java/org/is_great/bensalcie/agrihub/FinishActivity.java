package org.is_great.bensalcie.agrihub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FinishActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;
    private Button btnFinish;
    private EditText etNAME,etPhone,etLocation;
    private RadioButton rbFarmer,rbCustomer,rbOther;
    private String category="";
    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        //getSupportActionBar().hide();
        btnFinish=findViewById(R.id.btnFinish);
        etNAME=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etLocation=findViewById(R.id.etLocation);


        rbCustomer=findViewById(R.id.rbCustomer);
        rbFarmer=findViewById(R.id.rbFarmer);
        rbOther=findViewById(R.id.rbOther);
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(FinishActivity.this);
        pd.setTitle("Just a moment");
        pd.setMessage("Finishing account setup...");
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Kilimo_App_Users");
        btnFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pd.show();
                String name=etNAME.getText().toString().trim();
                String phone=etPhone.getText().toString().trim();
                String location=etLocation.getText().toString().trim();
                if (rbCustomer.isChecked())
                {
                    category="Customer";

                }else if (rbFarmer.isChecked())
                {
                    category="Farmer";
                }else {
                    category="Other";
                }

                if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(category) &&!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(location))
                {
                    final String user_id=mAuth.getUid();
                    if (!TextUtils.isEmpty(user_id)) {
                        DatabaseReference newUser = mUsersDatabase.child(user_id);
                        newUser.child("name").setValue(name);
                        newUser.child("phone").setValue(phone);
                        newUser.child("location").setValue(location);
                        newUser.child("points").setValue(50);
                        newUser.child("category").setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    pd.dismiss();
                                    startActivity(new Intent(FinishActivity.this,MainActivity.class));
                                    finish();

                                }else{
                                    pd.dismiss();
                                    toast("Error: "+task.getException().getMessage());
                                }
                            }
                        });
                    }else{
                        pd.dismiss();
                        Toast.makeText(FinishActivity.this, "An error occured during database creation...", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    pd.dismiss();
                    Toast.makeText(FinishActivity.this, "You left some inputs blank...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
