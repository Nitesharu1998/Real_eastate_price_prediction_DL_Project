package com.example.realeastatepriceprediction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.Executor;

import br.com.simplepass.loading_button_lib.animatedDrawables.CircularRevealAnimatedDrawable;
import de.hdodenhof.circleimageview.CircleImageView;

public class recycleradapter extends FirebaseRecyclerAdapter<model, recycleradapter.reclviewholder> {
    Context context;


    public recycleradapter(@NonNull FirebaseRecyclerOptions<model> options, Context context) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull recycleradapter.reclviewholder holder, int position, @NonNull model model) {




//
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.location.setText(model.getLocation());
        Glide.with(context).load(model.getImageurl()).into(holder.buildingimage);

        try {
            Picasso.get().load(model.getImageurl()).into(holder.buildingimage);
        }catch (Exception e){
            Log.i("picasso",e.toString());
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(v.getContext(),ProjInfoActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("features",model.getFeatures());
                intent.putExtra("coordinates",model.getCoordinates());
                intent.putExtra("booking",model.getBooking());
                intent.putExtra("buildingimage",model.getImageurl());
                intent.putExtra("location",model.getLocation());
                intent.putExtra("address",model.getAddress());


                v.getContext().startActivity(intent);
            }
        });




    }

    @NonNull

    @Override
    public reclviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclecontent, parent, false);
        return new reclviewholder(view);


    }

    class reclviewholder extends RecyclerView.ViewHolder {

        ImageView buildingimage;
        TextView name, location, address;
        LinearLayout relativeLayout;


        public reclviewholder(@NonNull View itemView) {
            super(itemView);


            buildingimage = itemView.findViewById(R.id.buildingimage);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            address = itemView.findViewById(R.id.address);
            relativeLayout=itemView.findViewById(R.id.relativelayout);


        }
    }
}
