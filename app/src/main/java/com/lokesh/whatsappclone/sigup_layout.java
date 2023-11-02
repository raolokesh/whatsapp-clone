package com.lokesh.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.lokesh.whatsappclone.Models.User;
import com.lokesh.whatsappclone.databinding.ActivityMainBinding;
import com.lokesh.whatsappclone.databinding.ActivitySigninBinding;
import com.lokesh.whatsappclone.databinding.ActivitySignupBinding;

public class sigup_layout extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://whatsapp-clone-76745-default-rtdb.asia-southeast1.firebasedatabase.app");


        progressDialog = new ProgressDialog(sigup_layout.this);
        progressDialog.setTitle("Account creation");
        progressDialog.setMessage("Please wait...");
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword
                        (binding.inputEmail.getText().toString(),
                                binding.inputPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(
                                            binding.inputName.getText().toString(),
                                            binding.inputEmail.getText().toString(),
                                            binding.inputPassword.getText().toString()
                                    );

                                    String id = task.getResult().getUser().getUid();
                                    mDatabase.getReference().child("Users").child(id).setValue(user);
                                    progressDialog.dismiss();
                                    Toast.makeText(sigup_layout.this, "user created successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(sigup_layout.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sigup_layout.this, sigin_layout.class));
            }
        });


    }
}