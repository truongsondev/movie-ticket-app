package com.example.movie_ticket_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_app.activity.LoginActivity;
import com.example.movie_ticket_app.activity.ProfileActivity;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;
import com.example.movie_ticket_app.dto.movie.response.ListMovieHomeResponse;
import com.example.movie_ticket_app.dto.movie.response.MovieHomeResponse;
import com.example.movie_ticket_app.utils.SessionManager;
import com.example.movie_ticket_app.view.MovieAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerMovies;
    private Button btnLogin;
    private SessionManager sessionManager;
    private static final String TAG = "MainActivity";

    // Define color constants to use instead of resource references
    private static final int COLOR_BLUE = Color.parseColor("#2196F3");  // Material Blue 500
    private static final int COLOR_GRAY = Color.parseColor("#9E9E9E");  // Material Gray 500

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        // Initialize RecyclerView
        recyclerMovies = findViewById(R.id.recyclerMovies);
        recyclerMovies.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize login button
        btnLogin = findViewById(R.id.btnLogin);
        setupLoginButton();

        // Initialize tab navigation
        setupTabNavigation();

        // Fetch movie data
        fetchMovieList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update login button text and behavior when returning to this activity
        updateLoginButtonState();
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    // Navigate to profile screen if logged in
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    // Navigate to login screen if not logged in
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Set initial state based on login status
        updateLoginButtonState();
    }

    private void updateLoginButtonState() {
        if (sessionManager.isLoggedIn()) {
            // User is logged in, show username
            String username = sessionManager.getUserName();
            if (username != null && !username.isEmpty()) {
                btnLogin.setText(username);
            } else {
                btnLogin.setText("Profile");
            }

            // Optionally change button appearance for logged-in state
            btnLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
            btnLogin.setPadding(12, 8, 12, 8);
        } else {
            // User is not logged in, show login text
            btnLogin.setText("Login");
            btnLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnLogin.setPadding(12, 8, 12, 8);
        }

        // Ensure button is visible
        btnLogin.setVisibility(View.VISIBLE);
        btnLogin.bringToFront();
    }

    private void setupTabNavigation() {
        TextView tvDangChieu = findViewById(R.id.tvDangChieu);
        TextView tvSapChieu = findViewById(R.id.tvSapChieu);

        tvDangChieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDangChieu.setTextColor(COLOR_BLUE);
                tvDangChieu.setTextSize(16);
                tvDangChieu.setTextAppearance(android.R.style.TextAppearance_Medium);
                tvSapChieu.setTextColor(COLOR_GRAY);
                tvSapChieu.setTextSize(16);
                // Fetch current movies
                fetchMovieList();
            }
        });

        tvSapChieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSapChieu.setTextColor(COLOR_BLUE);
                tvSapChieu.setTextSize(16);
                tvSapChieu.setTextAppearance(android.R.style.TextAppearance_Medium);
                tvDangChieu.setTextColor(COLOR_GRAY);
                tvDangChieu.setTextSize(16);
                // Fetch upcoming movies
                fetchUpcomingMovies();
            }
        });
    }

    private void fetchMovieList() {
        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<ListMovieHomeResponse>> call = apiServices.getAllMovie();
        call.enqueue(new Callback<APIResponse<ListMovieHomeResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<ListMovieHomeResponse>> call, Response<APIResponse<ListMovieHomeResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieHomeResponse> movies = response.body().getMetadata().getMovieHomeResponse();
                    displayMovies(movies);
                } else {
                    Log.e(TAG, "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ListMovieHomeResponse>> call, Throwable t) {
                Log.e(TAG, "Request failed: " + t.getMessage());
            }
        });
    }

    private void fetchUpcomingMovies() {
        // This would be implemented to fetch upcoming movies
        // For now, we'll just log that it's not implemented
        Log.d(TAG, "fetchUpcomingMovies: Not implemented yet");
        // You would implement similar API call as fetchMovieList but with different endpoint
    }

    private void displayMovies(List<MovieHomeResponse> movies) {
        MovieAdapter adapter = new MovieAdapter(this, movies);
        recyclerMovies.setAdapter(adapter);
    }
}
