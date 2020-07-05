package com.example.hackathon;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.DoctorViewHolder> {
    Context ctx;
    ArrayList<LabPojo> labPojoArrayList;
    FragmentManager activity;
    DownloadManager downloadManager;
    long queueid;

    public LabAdapter(Context ctx, ArrayList<LabPojo> labPojos) {
        this.ctx = ctx;
        this.labPojoArrayList = labPojos;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.labcard, parent, false);
        return new DoctorViewHolder(view);
    }

    public String url;
    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        final LabPojo labPojo = labPojoArrayList.get(position);
        holder.name.setText(labPojo.getName());
        holder.corona_treatment.setText(labPojo.getCovidtest());
        holder.n.setText(labPojo.getTestname());
        holder.downloadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = labPojo.getResult();
                downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                queueid = downloadManager.enqueue(request);
            }
        });

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(queueid);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int colIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(colIndex)) {
                            new AlertDialog.Builder(ctx)
                                    .setMessage("Download is completed....check report in your downloads!!!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        };
        ctx.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int getItemCount() {
        return labPojoArrayList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView name, corona_treatment,n;
        Button downloadReport;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.hospname);
            corona_treatment = itemView.findViewById(R.id.corona_treatment);
            downloadReport = itemView.findViewById(R.id.downloadReport);
            n = itemView.findViewById(R.id.n);
        }
    }
}
