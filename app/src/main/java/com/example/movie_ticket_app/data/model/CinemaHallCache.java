package com.example.movie_ticket_app.data.model;

public class CinemaHallCache {
    int cinemaHallId;
    String cinemaHallName;
    CinemaCache cinema;

    public int getCinemaHallId() {
        return cinemaHallId;
    }

    public String getCinemaHallName() {
        return cinemaHallName;
    }

    public CinemaCache getCinema() {
        return cinema;
    }
}
