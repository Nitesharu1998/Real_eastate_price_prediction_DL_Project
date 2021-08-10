package com.example.realeastatepriceprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.InputStream;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class homepage extends TabActivity {
    FirebaseAuth fauth;

    AlertDialog.Builder dialogueBuilder;
    AlertDialog alertDialog;
    FirebaseFirestore fStore;

    public String stringquery;
    Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        fauth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); // initiate TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("projects"); // Create a new TabSpec using tab host
        spec.setIndicator("Projects"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, projects.class);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        spec.setContent(intent);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Predict"); // Create a new TabSpec using tab host
        spec.setIndicator("Price Predictor"); // set the “HOME” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, prediction.class);
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        spec.setContent(intent);
        tabHost.addTab(spec);


        spec = tabHost.newTabSpec("EMI"); // Create a new TabSpec using tab host
        spec.setIndicator("EMI");// set the “CONTACT” as an indicator

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, emicalultor.class);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        spec.setContent(intent);
        tabHost.addTab(spec);


        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(1);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                //Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:

                final String PREFERENCE = "preference";
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.remove("email").apply();

                ed.clear();
                ed.commit();
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(homepage.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;

            case R.id.recents:

                break;


            case R.id.support:
//

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(homepage.this);
                bottomSheetDialog.setContentView(R.layout.supportpopup);
                overridePendingTransition(android.R.style.Widget_DeviceDefault_Light_PopupWindow,android.R.id.accessibilityActionScrollDown);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                TextInputEditText query=bottomSheetDialog.findViewById(R.id.supportquery);
                TextInputLayout queryinput=bottomSheetDialog.findViewById(R.id.supportinputlayout);
                queryinput.setHint("Enter Your Query");


                Button dialogButton = (Button) bottomSheetDialog.findViewById(R.id.send_query);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        stringquery= query.getText().toString().trim();
                        if (stringquery == null){
                            query.setError("Please enter your query");
                        }else query.setError(null);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:contact.dreamhomes@gmail.com"));
                        intent.putExtra(Intent.EXTRA_TEXT, stringquery);
                        intent.setType("message/rfc822");
                        startActivity(Intent.createChooser(intent, "select a sender"));


                    }
                });

                bottomSheetDialog.show();


        break;
        default:
        throw new IllegalStateException("Unexpected value: " + item.getItemId());
    }

        return super.onOptionsItemSelected(item);
}


//    public void sendquery(View view) {
//        EditText supportquery;
//
//        supportquery=(EditText)findViewById(R.id.supportquery);//null
//        stringquery = supportquery.getText().toString().trim();//null
//        if (stringquery==null) {
//            supportquery.setError("Enter query");
//            supportquery.requestFocus();
//
//
//        } else {
//            supportquery.setError(null);
//            Log.i("query",stringquery);
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:contact.dreamhomes@gmail.com"));
//            intent.putExtra(Intent.EXTRA_TEXT, stringquery);
//            intent.setType("message/rfc822");
//            startActivity(Intent.createChooser(intent, "select a sender"));
//        }
//    }
}




