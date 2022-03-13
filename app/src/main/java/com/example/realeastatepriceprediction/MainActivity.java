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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE = "preference";
    private static final int RC_SIGN_IN = 101;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    StorageReference storageReference;
    FirebaseUser user;
    ProgressBar pbar;
    TextInputEditText email, pass;
    CircularProgressButton googlebutton;
    String mail, password;
    String resetmail;
    CoordinatorLayout coordinatorlogin;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9006DC")));
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        pbar = findViewById(R.id.pbarlogin);
        coordinatorlogin = findViewById(R.id.coordinatorlogin);
    }

    public void loginbutton(View view) {
        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginpass);
        mail = email.getText().toString().trim();
        password = pass.getText().toString().trim();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        if (Patterns.EMAIL_ADDRESS.matcher(mail).matches() && password.length() > 8) {
            email.setError(null);
            pass.setError(null);

            pbar.setVisibility(View.VISIBLE);
            // authenticating now
            fAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    FirebaseUser currentUser = fAuth.getCurrentUser();
                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    //Toast.makeText(getApplicationContext(), "" + currentFirebaseUser.getUid().toString(), Toast.LENGTH_SHORT).show();
                                    if (currentUser == null) {
                                        // No user is signed in
                                    } else {
                                        userId = currentFirebaseUser.getUid();
                                        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                                        SharedPreferences.Editor ed = sharedPreferences.edit();
                                        ed.putString("email", mail);

                                        ed.putString("firestore_uid", userId);

                                        ed.apply();
                                        ed.commit();

                                        Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, homepage.class));
                                        pbar.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                } catch (Exception e) {
                                    Log.i("e", e.toString());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(MainActivity.this, "Errors : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            Log.i("Error message:", e.getLocalizedMessage());
                            pbar.setVisibility(View.INVISIBLE);

                        }
                    });


        } else if (mail.isEmpty()) {
            email.setError("please enter credentials");
            email.requestFocus();
        } else if (password.isEmpty()) {
            pass.setError("please enter credentials");
            pass.requestFocus();
        }
    }

    public void toRegister(View View) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }


    public void resetpass(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.supportpopup);
        overridePendingTransition(android.R.style.Widget_DeviceDefault_Light_PopupWindow, android.R.id.accessibilityActionScrollDown);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        TextInputEditText query = bottomSheetDialog.findViewById(R.id.supportquery);
        TextInputLayout queryinput = bottomSheetDialog.findViewById(R.id.supportinputlayout);
        Button resetpass = bottomSheetDialog.findViewById(R.id.send_query);
        queryinput.setHint("Enter Your Email Here");
        bottomSheetDialog.show();

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetmail = query.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(resetmail).matches()) {
                    queryinput.setError(null);
                    fAuth.sendPasswordResetEmail(resetmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            bottomSheetDialog.dismiss();
                            Snackbar.make(coordinatorlogin, "Check Mail for Password Reset Link", Snackbar.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            bottomSheetDialog.dismiss();
                            Snackbar.make(coordinatorlogin, "Something Went Wrong!", Snackbar.LENGTH_SHORT).show();


                        }
                    });

                } else queryinput.setError("Wrong Mail Format");
                queryinput.requestFocus();
            }
        });


    }
}
