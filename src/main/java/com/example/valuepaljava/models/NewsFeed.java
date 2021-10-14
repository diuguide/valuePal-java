package com.example.valuepaljava.models;

public class NewsFeed {

    private String newsFeed;

    public String getNewsFeed() {
        return newsFeed;
    }

    public void setNewsFeed(String newsFeed) {
        this.newsFeed = newsFeed;
    }

    @Override
    public String toString() {
        return "NewsFeed{" +
                "newsFeed='" + newsFeed + '\'' +
                '}';
    }
}
