package com.rimzhim.ModelClasses;

public class leaderBordContestantModel {
    private int img;
    private String contestantName;
    private String vote;
    private String rank;

    public leaderBordContestantModel(int img, String contestantName, String vote, String rank) {
        this.img = img;
        this.contestantName = contestantName;
        this.vote = vote;
        this.rank = rank;
    }


    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContestantName() {
        return contestantName;
    }

    public void setContestantName(String contestantName) {
        this.contestantName = contestantName;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
