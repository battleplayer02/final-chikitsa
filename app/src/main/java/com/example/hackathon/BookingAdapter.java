package com.example.hackathon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.DoctorViewHolder> {
    Context ctx;
    ArrayList<BookingPojo> bookingPojoArrayList;

    public BookingAdapter(Context ctx, ArrayList<BookingPojo> bookingPojoArrayList) {
        this.ctx = ctx;
        this.bookingPojoArrayList = bookingPojoArrayList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.booking_card, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position) {

        final BookingPojo bookingPojo = bookingPojoArrayList.get(position);
        holder.docname.setText(bookingPojo.getName());
        holder.date.setText(bookingPojo.getDate());
        holder.time.setText(bookingPojo.getTime());
        holder.mobileno.setText(bookingPojo.getMobile());
        holder.age.setText(bookingPojo.getAge());
        holder.temp.setText(bookingPojo.getTemp());
        holder.sympt.setText(bookingPojo.getSympt());
        holder.pd.setText(bookingPojo.getPd());
        holder.cd.setText(bookingPojo.getCd());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctx.startActivity(
                        new Intent(
                                ctx, Consultation.class)
                                .putExtra("did", bookingPojo.getDid())
                                .putExtra("pid", bookingPojo.getPid())
                );
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/getDocid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("error")){
                            Picasso.get()
                                    .load("https://fullstackers.000webhostapp.com/PatientAppointment/profileImages/" + response + ".png")
                                    .into(holder.docpic);
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

                params.put("docid", bookingPojo.getDid());
                return params;
            }
        };
        Volley.newRequestQueue(ctx).add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return bookingPojoArrayList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, temp, sympt, pd, cd, docname, mobileno, age;
        CardView cardView;
        ImageView docpic;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.aptdate);
            time = (TextView) itemView.findViewById(R.id.apttime);
            mobileno = (TextView) itemView.findViewById(R.id.mobile_no);
            docname = (TextView) itemView.findViewById(R.id.docname);
            temp = (TextView) itemView.findViewById(R.id.temp);
            sympt = (TextView) itemView.findViewById(R.id.sympt);
            age = (TextView) itemView.findViewById(R.id.aptage);
            pd = (TextView) itemView.findViewById(R.id.pd);
            cd = (TextView) itemView.findViewById(R.id.cd);
            cardView = (CardView) itemView.findViewById(R.id.cardBooking);
            docpic = (ImageView) itemView.findViewById(R.id.docpic);
        }
    }
}