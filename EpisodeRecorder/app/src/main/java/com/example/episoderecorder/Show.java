package com.example.episoderecorder;

public class Show {

    String showId;
    String title;
    int currentEp;
    int totalEp;

    public Show() {

    }

    public Show(String showId, String title, int currentEp, int totalEp) {
        this.showId = showId;
        this.title = title;
        this.currentEp = currentEp;
        this.totalEp = totalEp;
    }

    public String getShowId() {
        return showId;
    }

    public String getTitle() {
        return title;
    }

    public int getCurrentEp() {
        return currentEp;
    }

    public int getTotalEp() {
        return totalEp;
    }
}
