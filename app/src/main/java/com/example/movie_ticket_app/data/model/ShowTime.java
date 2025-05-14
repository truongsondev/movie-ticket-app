package com.example.movie_ticket_app.data.model;

public class ShowTime {
    private int showId;
    private String startTime;
    private String endTime;
    private String hallName;
    private String moviePosterUrl;
    private String date;
    private double price;
    private String room;

    // Add a no-argument constructor
    public ShowTime() {
        // Initialize with default values
        this.showId = 0;
        this.startTime = "";
        this.endTime = "";
        this.hallName = "";
        this.moviePosterUrl = "";
    }

    public ShowTime(int showId, String startTime, String endTime, String hallName, String moviePosterUrl) {
        this.showId = showId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hallName = hallName;
        this.moviePosterUrl = moviePosterUrl;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public int getId() {
        return showId;
    }

    public void setId(int id) {
        this.showId = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
