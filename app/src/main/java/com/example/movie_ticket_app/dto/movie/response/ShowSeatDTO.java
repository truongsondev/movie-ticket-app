package com.example.movie_ticket_app.dto.movie.response;

public class ShowSeatDTO {
    private int showId;
    private int cinemaSeatsNumberAvailable;
    private String showStartTime;
    private String showEndTime;

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getCinemaSeatsNumberAvailable() {
        return cinemaSeatsNumberAvailable;
    }

    public void setCinemaSeatsNumberAvailable(int cinemaSeatsNumberAvailable) {
        this.cinemaSeatsNumberAvailable = cinemaSeatsNumberAvailable;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }

    public String getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(String showEndTime) {
        this.showEndTime = showEndTime;
    }
}
