package com.example.hackathon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.HomeViewHolder> {
    Context ctx;
    ArrayList<StorePojo> homePojoArrayList;

    public StoreAdapter(Context ctx, ArrayList<StorePojo> homePojoArrayList) {
        this.ctx = ctx;
        this.homePojoArrayList = homePojoArrayList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.stores, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final StorePojo homePojo = homePojoArrayList.get(position);

        holder.name.setText(homePojo.getName());
        holder.address.setText(homePojo.getAddress());
    }

    @Override
    public int getItemCount() {
        return homePojoArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medname);
            address = itemView.findViewById(R.id.medaddress);

        }
    }
}
