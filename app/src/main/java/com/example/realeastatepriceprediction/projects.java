package com.example.realeastatepriceprediction;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class projects extends AppCompatActivity {
    RecyclerView recyclerView;
    recycleradapter recycleradapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9006DC")));


            recyclerView = findViewById(R.id.rclview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("projects"), model.class)
                        .build();

        recycleradapter=new recycleradapter(options,getApplicationContext());
        recyclerView.setAdapter(recycleradapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        recycleradapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recycleradapter.stopListening();
    }
}