package com.example.hackathon.volunteer.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hackathon.R;
import com.example.hackathon.volunteer.SearchParticpateEvent;

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
        View view = inflater.inflate(R.layout.events_card, null);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final HomePojo homePojo = homePojoArrayList.get(position);

        String pro_img = homePojo.getEventpic();
//        byte[] byteArray= Base64.decode(pro_img,Base64.DEFAULT);
//        Bitmap bmp= BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
//        holder.imgcard.setImageBitmap(bmp);
        holder.datecard.setText(homePojo.getDatecard());
        holder.placecard.setText(homePojo.getPlacecard());
        holder.eventname.setText(homePojo.getEventname());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ctx,homePojo.getEventname(),Toast.LENGTH_LONG).show();
                Intent intent =new Intent(ctx, SearchParticpateEvent.class);
                intent.putExtra("ename",homePojo.getEventname());
                intent.putExtra("oname",homePojo.getOwnername());
                intent.putExtra("eid",homePojo.getEventid());
                intent.putExtra("emobile",homePojo.getEmobile());
                intent.putExtra("edate",homePojo.getDatecard());
                intent.putExtra("eaddress",homePojo.getPlacecard());
                intent.putExtra("eabout",homePojo.getEabout());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homePojoArrayList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView datecard, placecard, typecard, eventname, eventamount;
        CardView cardView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            datecard = itemView.findViewById(R.id.datecard);
            placecard = itemView.findViewById(R.id.placecard);
            eventname = itemView.findViewById(R.id.eventname);
            cardView = itemView.findViewById(R.id.allEventsCard);
        }
    }
}
