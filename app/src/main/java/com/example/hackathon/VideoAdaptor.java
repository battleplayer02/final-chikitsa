package com.example.hackathon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdaptor extends RecyclerView.Adapter<VideoAdaptor.VideoViewHolder> {


    List<YoutubeVideo> youtubeVideoList;

    public VideoAdaptor(List<YoutubeVideo> youtubeVideoList) {
        this.youtubeVideoList = youtubeVideoList;
    }

    public VideoAdaptor() {
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtubecard, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.videoWeb.loadData(youtubeVideoList.get(position).getVideourl(), "text/html", "utf-8");

    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {

        WebView videoWeb;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoWeb = itemView.findViewById(R.id.youtubecard);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {


            });


        }
    }

}
