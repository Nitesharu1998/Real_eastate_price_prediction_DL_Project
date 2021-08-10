package com.example.realeastatepriceprediction;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Objects;

public class prediction extends AppCompatActivity {
    Interpreter interpreter;
    public TextView result;
    public TextInputLayout sqft;
    public TextInputLayout bhk;
    public TextInputLayout bath;
    public String s1, s2, s3;
    public Button emi, calculate;

    public float f;



    float[][] output = new float[1][1];
    DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9006DC")));

        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = findViewById(R.id.result);
        emi = findViewById(R.id.toemi);
        calculate = findViewById(R.id.calculate);

        emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), emicalultor.class);
                if (result == null) {
                    startActivity(intent);
                } else {
                    String emitext = df.format(f / 100);
                    intent.putExtra("emitext", emitext);
                }
                startActivity(intent);
            }
        });


    }


    public void calculate(View v) {
        if (!checksqft() | !checkbath() | !checkbhk()) {
            return;
        }
        float SQFT = Float.parseFloat(s1);
        float BATH = Float.parseFloat(s2);
        float BHK = Float.parseFloat(s3);
        float[] doubleArray = {SQFT, BATH, BHK};
        f = prediction.this.doInference(doubleArray);
        result.setText("The Price Of Your House Is " + df.format(f / 100) + " Lakhs");

    }


    private boolean checksqft() {
        sqft = findViewById(R.id.sqfttext);
        s1 = sqft.getEditText().getText().toString().trim();


        if (s1.isEmpty()) {
            sqft.setError("Field can't be empty");
            return false;
        } else if (s1.length() >= 4) {
            sqft.setError("size not available");
            return false;
        } else {
            sqft.setError(null);

            return true;
        }
    }

    private boolean checkbath() {
        bath = findViewById(R.id.bathtext);
        s2 = bath.getEditText().getText().toString().trim();
        if (s2.isEmpty()) {
            bath.setError("Field can't be empty");
            return false;
        }
//
        else {
            bath.setError(null);
            return true;
        }
    }

    private boolean checkbhk() {
        bhk = findViewById(R.id.bhktext);
        s3 = bhk.getEditText().getText().toString().trim();
        if (s3.isEmpty()) {
            bhk.setError("Field can't be empty");
            return false;
        } else {
            bhk.setError(null);
            return true;
        }
    }

    public float doInference(float[] doubleArray) {
        interpreter.run(doubleArray, output);
        return output[0][0];
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("ml_model.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }

}



