package com.example.realeastatepriceprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText username, password, contact, email;
    Button regbutton;
    TextView tologintext;
     String uname,pass,phone,mail;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    CoordinatorLayout clayout;
    public static final String PREFERENCE= "preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9006DC")));
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.email);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress);
        clayout=findViewById(R.id.clayout);
    }

    public void onLoginText(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }


    public void to_login(View view) {
        if (checkname() | checkpass() | checkcontact() | checkemail()) {
            progressBar.setVisibility(View.VISIBLE);


            fAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        // send verification link

                        FirebaseUser fuser = fAuth.getCurrentUser();
                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                Snackbar.make(clayout,"Registration done",Snackbar.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("email failed", "onFailure: Email not sent " + e.getMessage());
                            }
                        });

                        Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fName", uname);
                        user.put("email", mail);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SharedPreferences sharedPreferences =getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                                SharedPreferences.Editor ed = sharedPreferences.edit();
                                ed.putString("useruid",userID);
                                ed.apply();ed.commit();

                                Log.d("success: ", "onSuccess: user Profile is created for " + userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("failed", "onFailure: " + e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "something isnt right", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }





    public boolean checkname() {
         uname = username.getText().toString().trim();
        if (!uname.isEmpty()) {
            username.setError(null);
            Log.i("uname", uname);
            return true;
        } else
            username.setError("username cant be empty!");
            return false;
    }

    public boolean checkpass() {
         pass = password.getText().toString().trim();
        if (!pass.isEmpty() & pass.length() > 8) {
            password.setError(null);
            Log.i("pass", pass);
            return true;
        } else
            password.setError("password should be 8 letter");
            return false;
    }

    public boolean checkcontact() {
         phone = contact.getText().toString().trim();
        if ( phone.length()==10) {
            Log.i("phone", phone);
            contact.setError(null);
            return true;
        } else
            contact.setError("please enter valid number");
            return false;
    }

    public boolean checkemail() {
         mail = email.getText().toString().trim();
        if (Patterns.EMAIL_ADDRESS.matcher(mail).matches() && !mail.isEmpty()) {

            Log.i("mail", mail);
            email.setError(null);
            return true;
        } else
            email.setError("email is not valid");
            return false;
    }


}