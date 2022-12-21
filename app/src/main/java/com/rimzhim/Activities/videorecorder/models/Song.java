package com.rimzhim.Activities.videorecorder.models;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class Song {

    public int id;
    public String title;
    @Nullable
    public String artist;
    @Nullable
    public String album;
    public String audio;
    @Nullable
    public String cover;
    public int duration;
    public Date createdAt;
    public Date updatedAt;
    public List<SongSection> sections;
    public int clipsCount;
}
