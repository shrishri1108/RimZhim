package com.rimzhim.ModelClasses.videoListModel;

import java.util.ArrayList;

public class videos {
    String current_page;
    ArrayList<data> data = new ArrayList<>();
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    ArrayList<links> links = new ArrayList<>();
    private String next_page_url;
    private String path;
    private String per_page;
    private String prev_page_url;
    private String to;
    private String total;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public ArrayList<com.rimzhim.ModelClasses.videoListModel.data> getData() {
        return data;
    }

    public void setData(ArrayList<com.rimzhim.ModelClasses.videoListModel.data> data) {
        this.data = data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public ArrayList<com.rimzhim.ModelClasses.videoListModel.links> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<com.rimzhim.ModelClasses.videoListModel.links> links) {
        this.links = links;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
