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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private CatLoadingView mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getSupportActionBar().hide();
        btnLogin=findViewById(R.id.btnLogin);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        mAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(RegisterActivity.this);
        pd.setTitle("Just a moment");
        pd.setMessage(" Creating account...");

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

                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //pd.dismiss();
                                    mView.dismiss();

                                    btnLogin.setText("Loading please wait....");
                                    startActivity(new Intent(RegisterActivity.this,FinishActivity.class));
                                    finish();
                                }else{
                                    //pd.dismiss();
                                    mView.dismiss();

                                    btnLogin.setText("Error....");
                                    btnLogin.setBackgroundColor(getResources().getColor(R.color.colorRed));
                                    Toast.makeText(RegisterActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



                }
            }
        });
    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }
}
