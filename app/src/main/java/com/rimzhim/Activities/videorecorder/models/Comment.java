package com.rimzhim.Activities.videorecorder.models;

import java.util.Date;
import java.util.List;

public class Comment {

    public int id;
    public String text;
    public Date createdAt;
    public Date updatedAt;
    public User user;
    public List<String> hashtags;
    public List<User> mentions;
}
