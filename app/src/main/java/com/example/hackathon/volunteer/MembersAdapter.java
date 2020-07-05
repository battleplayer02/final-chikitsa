package com.example.hackathon.volunteer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.R;

import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    private Context mCtx;
    ArrayList<ShowMembers> blackboardList;

    public MembersAdapter(FragmentActivity activity, ArrayList<ShowMembers> blackboardArrayList) {
        this.mCtx = activity;
        this.blackboardList = blackboardArrayList;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.members, null);
        return new MembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MembersViewHolder holder, int position) {
        final ShowMembers blackboard = blackboardList.get(position);
        holder.member.setText(blackboard.getPname());
        holder.showassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(mCtx,AssignWork.class);
                intent.putExtra("name",blackboard.getPname());
                intent.putExtra("contact",blackboard.getPmobile());
                intent.putExtra("pid",blackboard.getPid());
                intent.putExtra("work",blackboard.getAssignwork());
                intent.putExtra("address",blackboard.getAddress());

                if((blackboard.getAssignwork().length()==0 && blackboard.getAddress().length()==0) || (blackboard.getAssignwork().equals("null") && blackboard.getAddress().equals("null")))                {
                    new AlertDialog.Builder(mCtx)
                            .setMessage("No Work Assigned...Want to  assign?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mCtx.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
                else {
                    intent.putExtra("btid","1");
                    mCtx.startActivity(intent);
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mCtx,AssignWork.class);
                intent.putExtra("name",blackboard.getPname());
                intent.putExtra("contact",blackboard.getPmobile());
                intent.putExtra("pid",blackboard.getPid());
                intent.putExtra("work",blackboard.getAssignwork());
                intent.putExtra("address",blackboard.getAddress());
                if((blackboard.getAssignwork().length()==0 && blackboard.getAddress().length()==0) || (blackboard.getAssignwork().equals("null") && blackboard.getAddress().equals("null")))
                {
                    intent.putExtra("btid","0");
                    mCtx.startActivity(intent);
                }
                else {
                    new AlertDialog.Builder(mCtx)
                            .setMessage("Work Already Assigned...Want to  change?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent.putExtra("btid","0");
                                    mCtx.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return blackboardList.size();
    }

    class MembersViewHolder extends RecyclerView.ViewHolder {

        public Button showassign;
        public TextView member;
        public CardView cardView;

        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
            member = itemView.findViewById(R.id.textMember);
            cardView=itemView.findViewById(R.id.membercard);
            showassign=itemView.findViewById(R.id.showassign);
        }
    }
}