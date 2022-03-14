package com.example.realeastatepriceprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.realeastatepriceprediction.kotlin.navigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProjInfoActivity extends AppCompatActivity {
    TextView title, featurestext, textbooking;
    String titlename, features, coordinates, lats, longi, booking, address, location;
    LocationManager lm;
    FirebaseAuth fAuth;
    String email, username;
    String shareduid;
    ImageView img;
    TextView textaddress, textlocation;
    public static final String PREFERENCE = "preference";
    FirebaseFirestore fStore;
    AppCompatButton locatebutton;
    //   private DocumentReference documentReference=fStore.collection("users").document(fAuth.getCurrentUser().getUid());
    //private final String fname= fAuth.getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proj_info);
        title = findViewById(R.id.nametitle);
        featurestext = findViewById(R.id.textfeatures);
        textbooking = findViewById(R.id.textbooking);
        img = findViewById(R.id.proj_image);
        textaddress = findViewById(R.id.projaddress);
        textlocation = findViewById(R.id.projlocation);
        locatebutton = findViewById(R.id.locatebutton);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9006DC")));

//        SharedPreferences sharedPreferences =getSharedPreferences(PREFERENCE, MODE_PRIVATE);
//        SharedPreferences.Editor ed = sharedPreferences.edit();
//        sharedPreferences.getString("userid",userID);

        fStore = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        shareduid = sharedPreferences.getString("firestore_uid", null);
        email = sharedPreferences.getString("email", null);
        if (ContextCompat.checkSelfPermission(ProjInfoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProjInfoActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(ProjInfoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(ProjInfoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        DocumentReference documentReference = fStore.collection("users").document(shareduid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username = documentSnapshot.getString("fName");
                Log.i("username", username);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });


        String imagelink = getIntent().getStringExtra("buildingimage");
        if (imagelink != null) {
            try {
                Picasso.get().load(imagelink).into(img);
            } catch (Exception e) {
                Log.i("picasso", e.toString());
            }
        }


        titlename = getIntent().getStringExtra("name");
        if (titlename != null) {
            title.setText(titlename);
        }
        features = getIntent().getStringExtra("features");
        if (features != null) {
            featurestext.setText(features);
        }

        coordinates = getIntent().getStringExtra("coordinates");
        if (coordinates != null) {
            String[] namesList = coordinates.split(",");
            Log.i("coords", coordinates);
            lats = namesList[0];
            longi = namesList[1];
            Log.i("lats", lats + longi);
        } else {
            Toast.makeText(this, "coordinates unnavailable", Toast.LENGTH_LONG).show();
            locatebutton.setEnabled(false);

        }

        booking = getIntent().getStringExtra("booking");
        if (booking != null) {
            textbooking.setText("Booking Availability: " + booking);
        }

        location = getIntent().getStringExtra("location");
        if (location != null) {
            textlocation.setText("Location: " + location);
        }
        address = getIntent().getStringExtra("address");
        if (address != null) {
            textaddress.setText("Address: " + address);
        }

        locationChecker();
        locatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locateproj();
            }
        });


    }

    public void locateproj() {

        if(locationChecker()){
            Intent intent = new Intent(this, maptrial.class);
            intent.putExtra("lats", lats);
            intent.putExtra("longi", longi);
            startActivity(intent);
        }



    }


    public void book(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProjInfoActivity.this);
        bottomSheetDialog.setContentView(R.layout.supportpopup);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        TextInputEditText bookit = bottomSheetDialog.findViewById(R.id.supportquery);
        TextInputLayout queryprojinfo = bottomSheetDialog.findViewById(R.id.supportinputlayout);
        queryprojinfo.setHint("Enter Floor Number");
        bookit.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button sendbook = (Button) bottomSheetDialog.findViewById(R.id.send_query);

        bottomSheetDialog.show();

        sendbook.setOnClickListener(v -> {
            String stringbook = bookit.getText().toString();

            if (stringbook.length() == 1 & !stringbook.isEmpty()) {
                bookit.setError(null);
                String emailclient = "contact.dreamhomesupport@gmail.com";
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailclient});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Request to book a flat");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello, myselt " + username + " I requset to book a flat at floor " + bookit);
                //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "select a sender"));
                bottomSheetDialog.dismiss();
            } else {
                bookit.setError("floor no not available");
                bookit.requestFocus();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(ProjInfoActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public boolean locationChecker() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            new AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            ProjInfoActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Global.showCustomToast(ProjInfoActivity.this,"Please enable location service",0);
                    dialogInterface.dismiss();
                }
            }).show();
            return false;
        }
        return true;
    }
}