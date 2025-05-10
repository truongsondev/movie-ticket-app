package com.example.movie_ticket_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;
import com.example.movie_ticket_app.dto.movie.response.ListMovieHomeResponse;
import com.example.movie_ticket_app.dto.movie.response.MovieHomeResponse;
import com.example.movie_ticket_app.view.MovieAdapter;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerMovies = findViewById(R.id.recyclerMovies);
        recyclerMovies.setLayoutManager(new GridLayoutManager(this, 2));

        fetchMovieList();
    }

    private void fetchMovieList() {
        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<ListMovieHomeResponse>> call = apiServices.getAllMovie();


        call.enqueue(new Callback<APIResponse<ListMovieHomeResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<ListMovieHomeResponse>> call, Response<APIResponse<ListMovieHomeResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieHomeResponse> movies = response.body().getMetadata().getMovieHomeResponse();
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getMetadata());
                    Log.d("API RESPONSE", json);

                    displayMovies(movies);
                } else {
                    Log.e("API", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ListMovieHomeResponse>> call, Throwable t) {
                Log.e("API", "Request failed: " + t.getMessage());
            }
        });
    }

    private void displayMovies(List<MovieHomeResponse> movies) {
        MovieAdapter adapter = new MovieAdapter(this, movies);
        recyclerMovies.setAdapter(adapter);
    }
}