package com.lokesh.whatsappclone;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lokesh.whatsappclone.Models.User;
import com.lokesh.whatsappclone.databinding.ActivitySigninBinding;

import java.util.HashMap;
import java.util.Map;

public class sigin_layout extends AppCompatActivity {

    ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 40;

    private FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    GoogleSignInClient mGoogleSignInClient;

    ActivitySigninBinding binding ;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!= null) {
            startActivity(new Intent(sigin_layout.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(sigin_layout.this);
        progressDialog.setTitle("Account Login");
        progressDialog.setMessage("Please wait...");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://whatsapp-clone-76745-default-rtdb.asia-southeast1.firebasedatabase.app");

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String googleId = preferences.getString("google_id", null);
        // Declare the launcher at the top of your Activity/Fragment:
//        if (googleId != null) {
//
//
//
//
//            String currentUserGoogleId = user.getUid();
//
//            if (googleId.equals(currentUserGoogleId)){
//                Toast.makeText(this, "asdvdnavsd login", Toast.LENGTH_LONG).show();
//            }

//            AuthCredential credential = GoogleAuthProvider.getCredential(googleId, null);
//            mAuth.signInWithCredential(credential)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                               @Override
//                                               public void onComplete(@NonNull Task<AuthResult> task) {
//                                                   if (task.isSuccessful()) {
//                                                       // Sign in success, update UI with the signed-in user's information
//                                                       Log.d(TAG, "signInWithCredential:success");
//                                                       FirebaseUser user = mAuth.getCurrentUser();
//                                                       startActivity(new Intent(sigin_layout.this, MainActivity.class));
//                                                   } else {
//                                                       // If sign in fails, display a message to the user.
//                                                       Log.w(TAG, "signInWithCredential:failure", task.getException());
//                                                       Toast.makeText(sigin_layout.this, "Authentication failed.",
//                                                               Toast.LENGTH_SHORT).show();
//                                                   }
//
//                                                   //...
//                                               }
//                                           }
//                    );
//            // You have user data, proceed to sign in automatically.
//            // You can use the Google ID to verify the user's identity.
//            // Proceed to the main activity or the next screen.
//        } else {
            // No user data found; prompt the user to sign in.
//        }





        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleSignIn();
            }
        });
        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword
                        (binding.inputEmail.getText().toString(),
                                binding.inputPassword.getText().toString())
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(sigin_layout.this,MainActivity.class));
                                } else {
                                    Toast.makeText(sigin_layout.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });

        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sigin_layout.this, sigup_layout.class));
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        ActivityResultCallback callback = new ActivityResultCallback();
//        callback.onActivityResult(signInIntent,RC_SIGN_IN);
        startActivityForResult(signInIntent, RC_SIGN_IN);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // here we are storing the account details in shared preferences storage

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(sigin_layout.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {

                             FirebaseUser user = mAuth.getCurrentUser();

                             HashMap<String,Object> map = new HashMap<>();
                             map.put("name", user.getDisplayName());
                             map.put("email", user.getEmail());
                             map.put("photoUrl", user.getPhotoUrl().toString());
                             map.put("phoneNumber", user.getPhoneNumber());
                             String id = user.getUid();
//                             saveUserData();

//                             String id = user.getUid();
//                             String name = user.getDisplayName();
//                             String email = user.getEmail();
//
//                             User user1 = new User(name,email);
//
//                             mDatabase.getReference().child("Users").child(id).setValue(user1);
                             mDatabase.getReference().child("Users").child(user.getUid()).setValue(map);


                             Toast.makeText(sigin_layout.this, "Login Success", Toast.LENGTH_LONG).show();
                             startActivity(new Intent(sigin_layout.this,MainActivity.class));
                         } else {
                             Toast.makeText(sigin_layout.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                         }
                     }

                });

    }
    private void saveUserData(GoogleSignInAccount account) {
        // Save user data to SharedPreferences or a local database
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("google_id", account.getId());
        editor.putString("email", account.getEmail());
        editor.putString("display_name", account.getDisplayName());
        editor.apply();
    }



}