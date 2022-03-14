package com.example.realeastatepriceprediction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class maptrial extends AppCompatActivity {
    String sourcelat, sourcelongi, destlat, destlongi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        destlat = getIntent().getStringExtra("lats");
        destlongi = getIntent().getStringExtra("longi");

        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation) {
            gps.getLocation();
            sourcelat = String.valueOf(gps.getLatitude());
            sourcelongi = String.valueOf(gps.getLongitude());
        } else {
           requestPermission();
        }

        //request location permission.
        requestPermission();

        WebView webview = (WebView) findViewById(R.id.webView1);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://www.google.com/maps/?saddr=" + sourcelat + "," + sourcelongi + "&daddr=" + destlat + "," + destlongi);

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(maptrial.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(maptrial.this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(maptrial.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
            {
                ActivityCompat.requestPermissions(maptrial.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(maptrial.this,
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


}