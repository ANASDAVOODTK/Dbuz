package com.anasdavood.dbuz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Flow;

public class Login extends AppCompatActivity {

    EditText email,password;
    ProgressBar progressBar;
    FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        email=findViewById(R.id.edtmail);
        password=findViewById(R.id.edtpassword);

        TextView txtlogin =  findViewById(R.id.txtsingup);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
            }
        });


        mfirebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.loading);

         progressBar.setVisibility(View.GONE);

        TextView forhet = findViewById(R.id.txtforgget);
        forhet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText sendmail = new EditText(view.getContext());
                final AlertDialog.Builder resetdailog = new AlertDialog.Builder(view.getContext());
                resetdailog.setTitle("Reset Password");
                resetdailog.setMessage("Enter your E-mail to Received Link");
                resetdailog.setView(sendmail);
                resetdailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = sendmail.getText().toString();
                        mfirebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset link send your email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error! Reset link not send"+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                resetdailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                resetdailog.create().show();

            }
        });



        View vvlogin =  findViewById(R.id.vvlogin);
        vvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(memail)){
                    email.setError("E-mail is Required");

                }
                else if (TextUtils.isEmpty(mpassword)){
                    password.setError("Password is Required");
                }

                else  if (password.length() < 8){
                    password.setError("Paasword is Short to 8");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    mfirebaseAuth.signInWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Logged is Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LocationComponentActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Eorror !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            }
        });
    }
}