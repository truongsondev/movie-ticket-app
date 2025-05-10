package com.example.movie_ticket_app.dto.movie.response;

import java.math.BigDecimal;

public class MovieHomeResponse {
    private int movieId;

    private String movieName;

    private int movieAge;

    private String movieThumbnail;

    private String movieTrailer;

    private BigDecimal movieRating;

    public int getMovieId() {
        return movieId;
    }

    public int getMovieAge() {
        return movieAge;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieThumbnail() {
        return movieThumbnail;
    }

    public String getMovieTrailer() {
        return movieTrailer;
    }

    public BigDecimal getMovieRating() {
        return movieRating;
    }
}
