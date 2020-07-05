package com.example.hackathon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

public class Motivational_videos extends Fragment {

    public Motivational_videos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_motivational_videos, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.youtuberecycler);
        recyclerView.setHasFixedSize(true);
        Vector<YoutubeVideo> youtubeVideoVector = new Vector<YoutubeVideo>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        youtubeVideoVector.add(new YoutubeVideo("<iframe width=\"327\" height=\"240\" src=\"https://www.youtube.com/embed/WM_0NrlF1lg\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideoVector.add(new YoutubeVideo("<iframe width=\"327\" height=\"409\" src=\"https://www.youtube.com/embed/vR1xXJoVCTM\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideoVector.add(new YoutubeVideo("<iframe width=\"327\" height=\"409\" src=\"https://www.youtube.com/embed/Fqw-9yMV0sI\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youtubeVideoVector.add(new YoutubeVideo("<iframe width=\"327\" height=\"409\" src=\"https://www.youtube.com/embed/Ry7T59rOJGA\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        VideoAdaptor videoAdaptor = new VideoAdaptor(youtubeVideoVector);
        recyclerView.setAdapter(videoAdaptor);
        return v;
    }
}