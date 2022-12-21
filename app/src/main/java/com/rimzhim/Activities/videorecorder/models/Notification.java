package com.rimzhim.Activities.videorecorder.models;

import androidx.annotation.Nullable;

import java.util.Date;

public class Notification {

    public String id;
    public String type;
    @Nullable
    public Date readAt;
    public Date createdAt;
    public Date updatedAt;
    @Nullable public User user;
    @Nullable public Clip clip;
    @Nullable public Comment comment;
}
