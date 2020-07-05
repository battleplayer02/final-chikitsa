package com.example.hackathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.DoctorViewHolder> {
    Context ctx;
    ArrayList<HospitalPojo> hospitalPojos;
    FragmentManager activity;

    public HospitalAdapter(Context ctx, ArrayList<HospitalPojo> hospitalPojos, FragmentManager activity) {
        this.ctx = ctx;
        this.activity = activity;
        this.hospitalPojos = hospitalPojos;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hospital_card, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position) {
        final HospitalPojo hospitalPojo = hospitalPojos.get(position);
        holder.hospname.setText(hospitalPojo.getHospname());
        holder.address.setText(hospitalPojo.getAddress());
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map = "http://maps.google.co.in/maps?q=" + hospitalPojo.getAddress();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                ctx.startActivity(intent);
            }
        });
        holder.corona_treatment.setText(hospitalPojo.getCorona_treatment());

        holder.bookApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListOfDoctors nextFrag = new ListOfDoctors(hospitalPojo.getHospid());
                activity.beginTransaction()
                        .replace(R.id.framelayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        String url="https://fullstackers.000webhostapp.com/PatientAppointment/profileImages/"+hospitalPojo.getHospid()+ ".png";
        Picasso.get().load(url).into(((ImageView)holder.imageView));
        System.out.println(url);

    }

    @Override
    public int getItemCount() {
        return hospitalPojos.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView hospname, address, corona_treatment;
        Button bookApp;

        ImageView imageView;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            hospname = itemView.findViewById(R.id.hospname);
            address = itemView.findViewById(R.id.address);
            corona_treatment = itemView.findViewById(R.id.corona_treatment);
            bookApp = (Button) itemView.findViewById(R.id.bookApp);
            imageView = (ImageView) itemView.findViewById(R.id.docpic);
        }
    }
}