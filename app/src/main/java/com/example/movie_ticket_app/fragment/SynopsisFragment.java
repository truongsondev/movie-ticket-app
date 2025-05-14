package com.example.movie_ticket_app.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.dto.movie.response.MovieDetailResponse;

public class SynopsisFragment extends Fragment {

    private MovieDetailResponse movieData;
    private TextView synopsisText;
    private TextView directorText;
    private TextView producerText;
    private TextView countryText;
    private TextView genresText;
    private TextView releaseDateText;

    public SynopsisFragment() {
        // Required empty public constructor
    }

    public void setMovieData(MovieDetailResponse movieData) {
        this.movieData = movieData;
        // Update UI if the view has been created
        if (synopsisText != null && movieData != null) {
            updateUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_synopsis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        synopsisText = view.findViewById(R.id.movieSynopsis);
        directorText = view.findViewById(R.id.movieDirectorDetail);
        producerText = view.findViewById(R.id.movieProducer);
        countryText = view.findViewById(R.id.movieCountry);
        genresText = view.findViewById(R.id.movieGenres);
        releaseDateText = view.findViewById(R.id.movieReleaseDateDetail);

        // Update UI if data is available
        if (movieData != null) {
            updateUI();
        }
    }

    private void updateUI() {
        if (synopsisText != null && movieData != null) {
            synopsisText.setText(movieData.getMovieDescription());

            if (directorText != null) {
                directorText.setText(movieData.getMovieDirector());
            }

            if (producerText != null) {
                producerText.setText(movieData.getMovieProducer());
            }

            if (countryText != null) {
                countryText.setText(movieData.getMovieCountry());
            }

            if (genresText != null && movieData.getGenres() != null) {
                genresText.setText(String.join(", ", movieData.getGenres()));
            }

            if (releaseDateText != null) {
                releaseDateText.setText(movieData.getMovieReleaseDate());
            }
        }
    }
}