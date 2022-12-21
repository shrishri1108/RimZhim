package com.rimzhim.Activities.videorecorder.models;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class Article {

    public int id;
    public String title;
    @Nullable
    public String snippet;
    @Nullable
    public String image;
    public String link;
    @Nullable
    public String source;
    public Date publishedAt;
    public Date createdAt;
    public Date updatedAt;
    public List<ArticleSection> sections;
}
