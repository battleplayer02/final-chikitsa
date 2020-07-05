package com.example.hackathon.volunteer.ui.helpline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hackathon.R;

import java.util.ArrayList;

public class HomeAdatper extends RecyclerView.Adapter<HomeAdatper.HomeViewHolder> {
    Context ctx;
    ArrayList<HomePojo> homePojoArrayList;

    public HomeAdatper(FragmentActivity ctx, ArrayList<HomePojo> homePojoArrayList) {
        this.ctx = ctx;
        this.homePojoArrayList = homePojoArrayList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.helpline_card, parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final HomePojo homePojo = homePojoArrayList.get(position);

        holder.name.setText(homePojo.getName());
        holder.number.setText(homePojo.getNumber());
        holder.helpcall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +homePojo.getNumber()));
                ctx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return homePojoArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView name,number;
        Button helpcall;
        CardView cardView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.allEventsCard);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            helpcall = itemView.findViewById(R.id.helpcall);

        }
    }
}
