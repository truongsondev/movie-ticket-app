package com.example.movie_ticket_app.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.movie_ticket_app.dto.movie.response.MovieDetailResponse;

/**
 * Adapter for managing fragments in the movie details ViewPager
 */
public class MovieFragmentAdapter extends FragmentStateAdapter {

    private MovieDetailResponse movieData;
    private static final int TAB_COUNT = 3;

    public MovieFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MovieFragmentAdapter(@NonNull FragmentActivity fragmentActivity, MovieDetailResponse movieData) {
        super(fragmentActivity);
        this.movieData = movieData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                // Synopsis/Information tab
                SynopsisFragment synopsisFragment = new SynopsisFragment();
                if (movieData != null) {
                    synopsisFragment.setMovieData(movieData);
                }
                return synopsisFragment;

            case 1:
                // Showtimes tab
                ShowTimesFragment showTimesFragment = new ShowTimesFragment();
                if (movieData != null) {
                    showTimesFragment.setShowTimes(movieData.getShows());
                }
                return showTimesFragment;

            case 2:
                // Cast tab
                CastFragment castFragment = CastFragment.newInstance();
                if (movieData != null && movieData.getActors() != null) {
                    castFragment.updateCastList(movieData.getActors());
                }
                return castFragment;

            default:
                return new SynopsisFragment();
        }
    }

    @Override
    public int getItemCount() {
        return TAB_COUNT;
    }

    /**
     * Updates the movie data and notifies the adapter that the data has changed
     * @param movieData The updated movie data
     */
    public void updateMovieData(MovieDetailResponse movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }
}