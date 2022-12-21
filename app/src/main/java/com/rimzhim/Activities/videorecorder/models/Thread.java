package com.rimzhim.Activities.videorecorder.models;

import androidx.annotation.Nullable;

import java.util.Date;

public class Thread {

    public int id;
    public Date createdAt;
    public Date updatedAt;
    public boolean unread;
    public User user;
    @Nullable
    public Message latest;
}
