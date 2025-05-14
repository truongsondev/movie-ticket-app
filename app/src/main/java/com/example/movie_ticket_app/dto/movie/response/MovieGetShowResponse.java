package com.example.movie_ticket_app.dto.movie.response;

public class MovieGetShowResponse {
    private int movieId;
    private String movieName;
    private int movieAge;
    private String movieThumbnail;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getMovieAge() {
        return movieAge;
    }

    public void setMovieAge(int movieAge) {
        this.movieAge = movieAge;
    }

    public String getMovieThumbnail() {
        return movieThumbnail;
    }

    public void setMovieThumbnail(String movieThumbnail) {
        this.movieThumbnail = movieThumbnail;
    }
}
