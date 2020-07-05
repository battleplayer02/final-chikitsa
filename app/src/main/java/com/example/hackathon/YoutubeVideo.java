package com.example.hackathon;

public class YoutubeVideo {

    String Videourl;

    public YoutubeVideo() {
//app not installed aa
    }

    public YoutubeVideo(String videourl) {
        Videourl = videourl;
    }

    public String getVideourl() {
        return Videourl;
    }

    public void setVideourl(String videourl) {
        Videourl = videourl;
    }
}
