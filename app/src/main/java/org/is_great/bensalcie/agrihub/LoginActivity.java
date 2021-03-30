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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private TextView tvForgot;
    private CatLoadingView mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        pd=new ProgressDialog(LoginActivity.this);
        pd.setTitle("Just a moment");
        pd.setMessage("Logging in...");
tvForgot=findViewById(R.id.tv_forgot);
        btnLogin=findViewById(R.id.btnLogin);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        mAuth=FirebaseAuth.getInstance();
        mView = new CatLoadingView();
        mView.setText("...");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pd.show();
                mView.show(getSupportFragmentManager(), "");
               String email=etEmail.getText().toString().trim();
               String pass=etPassword.getText().toString().trim();
               if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
               {
                       mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   //pd.dismiss();
                                   mView.dismiss();
                                   btnLogin.setText("Loading please wait....");
                                   startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                   finish();
                               }else{
                                   //pd.dismiss();
mView.dismiss();
                                   btnLogin.setText("Error....");
                                   btnLogin.setBackgroundColor(getResources().getColor(R.color.colorRed));

                               }
                           }
                       });



               }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logic to forget pssword
                etPassword.setVisibility(View.GONE);
                //etEmail.setVisibility(View.GONE);
                //etReset.setVisibility(View.VISIBLE);
                btnLogin.setText("RESET PASSWORD");
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd.setMessage("Sending mail...");
                        pd.show();
                        String mail=etEmail.getText().toString().trim();
                        if (!TextUtils.isEmpty(mail)) {
                            mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        btnLogin.setText("CHECK YOUR EMAIL...");
                                    }else {
                                        pd.dismiss();

                                        btnLogin.setText("Email does not exist...");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();

                                    btnLogin.setText("An Error occured...");
                                }
                            });
                        }else{
                            pd.dismiss();

                            Toast.makeText(getApplicationContext(), "Please enter your email...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    public void forgot(View view) {
        //Sorry we can not recover your password right now,please check your details again
    }

    public void create(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }

}
