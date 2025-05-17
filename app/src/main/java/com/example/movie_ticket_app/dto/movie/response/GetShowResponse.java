package com.example.movie_ticket_app.dto.movie.response;

import java.io.Serializable;
import java.util.List;

public class GetShowResponse implements Serializable {
    private MovieGetShowResponse movie;
    private int showId;
    private String showStartTime;
    private String cinemaName;
    private String cinemaHallName;
    private List<ShowDTO> otherShows;
    private List<ShowSeatDTO> seats;

    public MovieGetShowResponse getMovie() {
        return movie;
    }

    public void setMovie(MovieGetShowResponse movie) {
        this.movie = movie;
    }

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

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaHallName() {
        return cinemaHallName;
    }

    public void setCinemaHallName(String cinemaHallName) {
        this.cinemaHallName = cinemaHallName;
    }

    public List<ShowDTO> getOtherShows() {
        return otherShows;
    }

    public void setOtherShows(List<ShowDTO> otherShows) {
        this.otherShows = otherShows;
    }

    public List<ShowSeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<ShowSeatDTO> seats) {
        this.seats = seats;
    }
}
