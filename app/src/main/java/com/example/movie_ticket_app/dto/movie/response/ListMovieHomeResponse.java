package com.example.movie_ticket_app.dto.movie.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListMovieHomeResponse {
    @SerializedName("movies")
    private List<MovieHomeResponse> movieHomeResponse;

    public List<MovieHomeResponse> getMovieHomeResponse() {
        return movieHomeResponse;
    }
}
