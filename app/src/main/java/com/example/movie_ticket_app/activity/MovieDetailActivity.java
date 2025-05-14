package com.example.movie_ticket_app.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;
import com.example.movie_ticket_app.dto.movie.response.MovieDetailResponse;
import com.example.movie_ticket_app.fragment.MovieFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        int movieId = getIntent().getIntExtra("movieId", -1); // -1 là giá trị mặc định nếu không có

        if (movieId != -1) {
            fetchMovieDetail(movieId);
        } else {
            Log.e("MovieDetail", "Không tìm thấy movieId trong Intent");
        }
    }

    private void fetchMovieDetail(int movieId) {
        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<MovieDetailResponse>> call = apiServices.getDetailMovie(movieId);

        call.enqueue(new Callback<APIResponse<MovieDetailResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<MovieDetailResponse>> call, Response<APIResponse<MovieDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetailResponse movie = response.body().getMetadata();


                    // Hiển thị thông tin cơ bản của phim
                    setupBasicMovieInfo(movie);

                    // Hiển thị nội dung phim
                    setupMovieSynopsis(movie);

                    // Hiển thị thông tin sản xuất
                    setupProductionInfo(movie);

                    // Thiết lập spinner và tabs
                    setupSpinnersAndTabs(movie);

                    // Thiết lập trailer
                    setupTrailer(movie);

                } else {
                    Log.e("MovieDetail", "Failed to load movie detail: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<MovieDetailResponse>> call, Throwable t) {
                Log.e("MovieDetail", "API call failed: " + t.getMessage());
            }
        });
    }

    private void setupBasicMovieInfo(MovieDetailResponse movie) {
        // Khởi tạo các view
        ImageView movieBanner = findViewById(R.id.movieBanner);
        ImageView moviePoster = findViewById(R.id.moviePoster);
        TextView movieTitle = findViewById(R.id.movieTitle);
        TextView movieGenre = findViewById(R.id.movieGenre);
        TextView movieDuration = findViewById(R.id.movieDuration);
        TextView movieLanguage = findViewById(R.id.movieLanguage);
        TextView movieDirector = findViewById(R.id.movieDirector);
        TextView movieReleaseDate = findViewById(R.id.movieReleaseDate);

        // Set dữ liệu cho TextView
        movieTitle.setText(movie.getMovieName());

        // Định dạng thể loại từ danh sách
        String genresText = "Thể loại: " + String.join(", ", movie.getGenres());
        movieGenre.setText(genresText);

        // Định dạng thời lượng phim (phút)
        movieDuration.setText("Thời lượng: " + movie.getMovieDuration() + " phút");
        movieLanguage.setText("Ngôn ngữ: " + movie.getMovieLanguage());
        movieDirector.setText("Đạo diễn: " + movie.getMovieDirector());

        // Định dạng ngày khởi chiếu
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(movie.getMovieReleaseDate());
            String formattedDate = outputFormat.format(date);
            movieReleaseDate.setText("Khởi chiếu: " + formattedDate);
        } catch (Exception e) {
            movieReleaseDate.setText("Khởi chiếu: " + movie.getMovieReleaseDate());
        }

        // Set hình ảnh cho ImageView
        Glide.with(MovieDetailActivity.this)
                .load(movie.getMovieThumbnail())
                .into(movieBanner);

        Glide.with(MovieDetailActivity.this)
                .load(movie.getMovieThumbnail())
                .into(moviePoster);
    }

    private void setupMovieSynopsis(MovieDetailResponse movie) {
        TextView movieSynopsis = findViewById(R.id.movieSynopsis);
        if (movieSynopsis != null && movie.getMovieDescription() != null) {
            movieSynopsis.setText(movie.getMovieDescription());
        }
    }

    private void setupProductionInfo(MovieDetailResponse movie) {
        // Tìm các TextView trong layout production info
        TextView producerTextView = findViewById(R.id.movieProducer);
        TextView countryTextView = findViewById(R.id.movieCountry);

        // Set dữ liệu nếu các view tồn tại
        if (producerTextView != null) {
            producerTextView.setText(movie.getMovieProducer());
        }

        if (countryTextView != null) {
            countryTextView.setText(movie.getMovieCountry());
        }

        // Thiết lập rating nếu có
        RatingBar ratingBar = findViewById(R.id.movieRatingBar);
        TextView ratingTextView = findViewById(R.id.movieRatingText);

        if (ratingBar != null && movie.getMovieRating().compareTo(BigDecimal.ZERO) > 0) {
            float rating = movie.getMovieRating().floatValue() / 2.0f; // Chuyển sang float và chia 2
            ratingBar.setRating(rating);
        }


        if (ratingTextView != null) {
            ratingTextView.setText(String.format(Locale.getDefault(), "%.1f/10", movie.getMovieRating()));
        }
    }

    private void setupSpinnersAndTabs(MovieDetailResponse movie) {
        Spinner citySpinner = findViewById(R.id.citySpinner);
        Spinner cinemaSpinner = findViewById(R.id.cinemaSpinner);
        TabLayout bookingInfoTabs = findViewById(R.id.bookingInfoTabs);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Set dữ liệu cho Spinner
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                MovieDetailActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.cities)
        );

        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        ArrayAdapter<String> cinemaAdapter = new ArrayAdapter<>(
                MovieDetailActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.cinemas)
        );
        cinemaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cinemaSpinner.setAdapter(cinemaAdapter);

        // Thiết lập ViewPager và TabLayout
        if (viewPager != null && bookingInfoTabs != null) {

            MovieFragmentAdapter adapter = new MovieFragmentAdapter(MovieDetailActivity.this, movie);
            viewPager.setAdapter(adapter);


            new TabLayoutMediator(bookingInfoTabs, viewPager, (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Thông tin");
                        break;
                    case 1:
                        tab.setText("Lịch chiếu");
                        break;
                    case 2:
                        tab.setText("Diễn viên");
                        break;
                }
            }).attach();
        }
    }

    private void setupTrailer(MovieDetailResponse movie) {
        // Thiết lập trailer thumbnail và click listener
        FrameLayout trailerContainer = findViewById(R.id.trailerContainer);
        ImageView trailerThumbnail = findViewById(R.id.trailerThumbnail);

        if (trailerContainer != null && trailerThumbnail != null && movie.getMovieTrailer() != null) {
            // Sử dụng thumbnail của phim làm ảnh đại diện cho trailer
            Glide.with(MovieDetailActivity.this)
                    .load(movie.getMovieThumbnail())
                    .into(trailerThumbnail);

            // Thiết lập click listener để mở trailer
            trailerContainer.setOnClickListener(v -> {
                // Mở trailer YouTube hoặc video player
                String trailerUrl = movie.getMovieTrailer();
                // Thêm code để mở video player hoặc YouTube app
                Log.d("MovieDetail", "Opening trailer: " + trailerUrl);

            });
        }
    }
}