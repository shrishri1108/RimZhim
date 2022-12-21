package com.rimzhim.Activities.videorecorder.models;

import android.os.Parcel;

import androidx.annotation.Nullable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.Date;

public class User implements SearchSuggestion {

    public int id;
    public String name;
    @Nullable
    public String photo;
    public String username;
    @Nullable
    public String email;
    @Nullable
    public String phone;
    @Nullable
    public String bio;
    public boolean verified;
    public Date createdAt;
    public Date updatedAt;
    public int followersCount;
    public int followedCount;
    public int clipsCount;
    public int likesCount;
    public int viewsCount;
    public boolean me;
    public boolean follower;
    public boolean followed;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        photo = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {

        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String getBody() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(photo);
        dest.writeString(username);
    }
}
