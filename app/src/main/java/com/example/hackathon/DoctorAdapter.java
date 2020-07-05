package com.example.hackathon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    Context ctx;
    ArrayList<DoctorPojo> doctorPojoArrayList;

    public DoctorAdapter(Context ctx, ArrayList<DoctorPojo> doctorPojoArrayList) {
        this.ctx = ctx;
        this.doctorPojoArrayList = doctorPojoArrayList;
        System.out.println(doctorPojoArrayList + "docadd");
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.doctors_card, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position) {
        final DoctorPojo doctorPojo = doctorPojoArrayList.get(position);
//        Toast.makeText(ctx, position + "", Toast.LENGTH_LONG).show();
        System.out.println(position + "position");
        holder.specialization.setText(doctorPojo.getSpecialization());
        holder.mobileno.setText(doctorPojo.getMobile_no());
        holder.blood_group.setText(doctorPojo.getBlood_group());
        holder.department.setText(doctorPojo.getDepartment());
        holder.experience.setText(doctorPojo.getExperience());
        holder.docname.setText(doctorPojo.getName());


        holder.bookApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, Appointment.class).putExtra("docid", doctorPojo.getDocid()));
            }
        });


        holder.mobileno.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + doctorPojo.getMobile_no()));
                ctx.startActivity(intent);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/getDocid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("error")){
                            Picasso.get()
                                    .load("https://fullstackers.000webhostapp.com/PatientAppointment/profileImages/" + response + ".png")
                                    .into((ImageView) holder.imageView);
                            System.out.println("456456   = "+response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("docid", doctorPojo.getDocid());
                return params;
            }
        };
        Volley.newRequestQueue(ctx).add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return doctorPojoArrayList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView specialization, docname, mobileno, department, experience, blood_group;
        Button bookApp;
        ImageView imageView;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            specialization = (TextView) itemView.findViewById(R.id.specialization);
            docname = (TextView) itemView.findViewById(R.id.docname);
            mobileno = (TextView) itemView.findViewById(R.id.mobile_no);
            department = (TextView) itemView.findViewById(R.id.department);
            experience = (TextView) itemView.findViewById(R.id.experience);
            blood_group = (TextView) itemView.findViewById(R.id.blood_group);
            bookApp = (Button) itemView.findViewById(R.id.bookApp);
            imageView= itemView.findViewById(R.id.docpic);
        }
    }
}