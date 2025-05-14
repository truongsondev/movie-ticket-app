package com.example.movie_ticket_app.data.model;

import java.util.List;

public class ShowCase {
    int showId;
    String showStartTime;
    String showEndTime;
    CinemaHallCache cinemaHall;

    public int getShowId() {
        return showId;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public String getShowEndTime() {
        return showEndTime;
    }

    public CinemaHallCache getCinemaHall() {
        return cinemaHall;
    }
}
