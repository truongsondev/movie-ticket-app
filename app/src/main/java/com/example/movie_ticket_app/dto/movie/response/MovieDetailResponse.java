package com.example.movie_ticket_app.dto.movie.response;

import com.example.movie_ticket_app.data.model.ShowCase;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;



public class MovieDetailResponse {
    int movieId;
    String movieName;
    String movieDescription;
    int movieAge;
    String movieThumbnail;
    String movieTrailer;
    BigDecimal movieRating;
    int movieDuration;
    String movieLanguage;
    String movieCountry;
    String movieReleaseDate;
    String movieDirector;
    String movieProducer;
    List<String> genres;
    List<String> actors;
    List<ShowCase> shows;

    public int getMovieId() {
        return movieId;
    }

    public List<ShowCase> getShows() {
        return shows;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public int getMovieAge() {
        return movieAge;
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

    public String getMovieLanguage() {
        return movieLanguage;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public String getMovieCountry() {
        return movieCountry;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public String getMovieProducer() {
        return movieProducer;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String> getGenres() {
        return genres;
    }
}
