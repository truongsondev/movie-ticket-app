package com.example.movie_ticket_app.dto.movie.response;

public class ShowDTO {
    private int showId;
    private String showStartTime;

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }
}
