package com.example.realeastatepriceprediction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String PREFERENCE = "preference";
    TextInputEditText username, password, contact, email;
    Button regbutton;
    TextView tologintext;
    String uname, pass, phone, mail;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    CoordinatorLayout clayout;

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
        clayout = findViewById(R.id.clayout);
        regbutton=findViewById(R.id.reglogin);

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateUser();
            }
        });

    }

    private void CreateUser() {
        if (validate()) {
            mail=email.getText().toString();
            pass=password.getText().toString();

            progressBar.setVisibility(View.VISIBLE);
            fAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        // send verification link

                        FirebaseUser fuser = fAuth.getCurrentUser();
                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                Snackbar.make(clayout, "Registration done", Snackbar.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
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
                        uname=username.getText().toString();
                        mail=email.getText().toString();
                        phone=contact.getText().toString();
                        pass=password.getText().toString();
                        user.put("fName", uname);
                        user.put("email", mail);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                                SharedPreferences.Editor ed = sharedPreferences.edit();
                                ed.putString("useruid", userID);
                                ed.apply();
                                ed.commit();

                                Log.d("success: ", "onSuccess: user Profile is created for " + userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("failed", "onFailure: " + e.toString());
                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onLoginText(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }


    private boolean validate() {
        if (!Validator.emailValidation(RegisterActivity.this, email)) {
            return false;
        }
        if (!Validator.inchargeName(RegisterActivity.this, username)) {
            return false;
        }
        if (!Validator.passwordValidation(RegisterActivity.this, password))
            return false;
        if (!Validator.mobileNoValidation(RegisterActivity.this, contact)) {
            return false;
        }
        return true;
    }

}