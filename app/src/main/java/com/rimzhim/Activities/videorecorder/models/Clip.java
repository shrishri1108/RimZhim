package com.rimzhim.Activities.videorecorder.models;

import androidx.annotation.Nullable;



import java.util.Date;
import java.util.List;

public class Clip {

    public int id;
    public String video;
    public String screenshot;
    public String preview;
    @Nullable
    public String description;
    public String language;

    public boolean _private;
    public boolean comments;
    public int duration;
    public Date createdAt;
    public Date updatedAt;
    public User user;
    @Nullable public Song song;
    public List<ClipSection> sections;
    public int viewsCount;
    public int likesCount;
    public int commentsCount;
    public boolean liked;
    public boolean saved;
    public List<String> hashtags;
    public List<User> mentions;
}
