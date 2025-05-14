package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;

import com.example.movie_ticket_app.data.model.Seat;
import com.example.movie_ticket_app.data.model.ShowTime;
import com.example.movie_ticket_app.dto.movie.response.GetShowResponse;
import com.example.movie_ticket_app.dto.movie.response.MovieGetShowResponse;
import com.example.movie_ticket_app.dto.movie.response.ShowDTO;
import com.example.movie_ticket_app.dto.movie.response.ShowSeatDTO;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final String TAG = "SeatSelectionActivity";

    // UI Components
    private ImageButton btnBack;
    private Button btnBack2, btnContinue;
    private TextView movieTitle, movieFormat, movieRoom, theaterName, showtime, totalPrice;
    private ImageView moviePoster;
    private Spinner showtimeSpinner;

    // Data
    private int movieId;
    private int theaterId;
    private int showtimeId;
    private String moviePosterUrl;
    private List<ShowTime> showtimes = new ArrayList<>();
    private List<Seat> selectedSeats = new ArrayList<>();
    private Map<String, TextView> seatViews = new HashMap<>();
    private double ticketPrice = 0;
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Initialize currency formatter
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Get data from intent
        movieId = getIntent().getIntExtra("MOVIE_ID", 0);
        theaterId = getIntent().getIntExtra("THEATER_ID", 0);
        showtimeId = getIntent().getIntExtra("SHOWTIME_ID", 0);
        moviePosterUrl = getIntent().getStringExtra("MOVIE_POSTER_URL");

        if (movieId == 0 || theaterId == 0 || showtimeId == 0) {
            Toast.makeText(this, "Invalid movie or theater selection", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup click listeners
        setupListeners();

        // Load movie and showtime data
        loadMovieData();

    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnBack2 = findViewById(R.id.btnBack2);
        btnContinue = findViewById(R.id.btnContinue);

        movieTitle = findViewById(R.id.movieTitle);
        movieFormat = findViewById(R.id.movieFormat);
        movieRoom = findViewById(R.id.movieRoom);
        theaterName = findViewById(R.id.theaterName);
        showtime = findViewById(R.id.showtime);
        totalPrice = findViewById(R.id.totalPrice);

        moviePoster = findViewById(R.id.moviePoster);
        showtimeSpinner = findViewById(R.id.showtimeSpinner);

        // Initialize seat views
        initSeatViews();
    }

    private void initSeatViews() {
        // Row I
        seatViews.put("I1", findViewById(R.id.seat_1));
        seatViews.put("I2", findViewById(R.id.seat_2));
        seatViews.put("I3", findViewById(R.id.seat_3));
        seatViews.put("I4", findViewById(R.id.seat_4));
        seatViews.put("I5", findViewById(R.id.seat_5));
        seatViews.put("I6", findViewById(R.id.seat_6));
        seatViews.put("I7", findViewById(R.id.seat_7));
        seatViews.put("I8", findViewById(R.id.seat_8));

        // Add more rows as needed
        // This is a simplified implementation - in a real app, you would dynamically create these views
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnBack2.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to food selection screen
            Intent intent = new Intent(SeatSelectionActivity.this, FoodSelectionActivity.class);
            intent.putExtra("MOVIE_ID", movieId);
            intent.putExtra("THEATER_ID", theaterId);
            intent.putExtra("SHOWTIME_ID", showtimeId);
            intent.putExtra("SELECTED_SEATS", selectedSeats.toString());
            intent.putExtra("TICKET_PRICE", getTotalPrice());
            startActivity(intent);
        });

        showtimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowTime selected = showtimes.get(position);
                showtimeId = selected.getId();
                updateShowtimeInfo(selected);

                // Fetch new seat map for the selected showtime
                fetchShowDetails(showtimeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Setup seat click listeners
        for (Map.Entry<String, TextView> entry : seatViews.entrySet()) {
            TextView seatView = entry.getValue();
            String seatId = entry.getKey();

            seatView.setOnClickListener(v -> {
                if (seatView.getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.seat_unavailable).getConstantState())) {
                    // Seat is unavailable
                    Toast.makeText(SeatSelectionActivity.this, "Ghế đã được đặt", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (seatView.getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.seat_available).getConstantState())) {
                    // Select seat
                    seatView.setBackground(getResources().getDrawable(R.drawable.seat_selected));
                    seatView.setTextColor(getResources().getColor(android.R.color.white));

                    // Add to selected seats
                    Seat seat = new Seat();
                    seat.setId(seatId);
                    seat.setRow(seatId.substring(0, 1));
                    seat.setNumber(Integer.parseInt(seatId.substring(1)));
                    seat.setPrice(ticketPrice);
                    selectedSeats.add(seat);
                } else {
                    // Unselect seat
                    seatView.setBackground(getResources().getDrawable(R.drawable.seat_available));
                    seatView.setTextColor(getResources().getColor(android.R.color.black));

                    // Remove from selected seats
                    for (int i = 0; i < selectedSeats.size(); i++) {
                        if (selectedSeats.get(i).getId().equals(seatId)) {
                            selectedSeats.remove(i);
                            break;
                        }
                    }
                }

                // Update total price
                updateTotalPrice();
            });
        }
    }

    private void loadMovieData() {
        // Call API to get show information
        fetchShowDetails(showtimeId);
    }

    private void fetchShowDetails(int showId) {
        // Show loading state
        // You might want to add a progress bar here

        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<GetShowResponse>> call = apiServices.getShow(showId);

        call.enqueue(new Callback<APIResponse<GetShowResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<GetShowResponse>> call, Response<APIResponse<GetShowResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getMetadata() != null) {
                    GetShowResponse showResponse = response.body().getMetadata();

                    // Update movie information
                    updateMovieInfo(showResponse);

                    // Update showtime information
                    updateShowtimeInfo(showResponse);

                    // Load available showtimes
                    loadAvailableShowtimes(showResponse.getOtherShows());

                    // Load seat map
                    loadSeatMap(showResponse.getSeats());
                } else {
                    Toast.makeText(SeatSelectionActivity.this,
                            "Failed to load show details", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<GetShowResponse>> call, Throwable t) {
                Toast.makeText(SeatSelectionActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error during fetchShowDetails", t);
            }
        });
    }

    private void updateMovieInfo(GetShowResponse showResponse) {
        MovieGetShowResponse movie = showResponse.getMovie();

        if (movie != null) {
            // Update movie title
            movieTitle.setText(movie.getMovieName());

            // Update movie format (assuming it's 2D with subtitles)
            movieFormat.setText("2D Phụ Đề - ");

            // Update movie age rating
            movieRoom.setText("T" + movie.getMovieAge());

            // Update theater name
            theaterName.setText(showResponse.getCinemaName() + " - " + showResponse.getCinemaHallName());

            // Load movie poster
            String posterUrl = movie.getMovieThumbnail();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                Glide.with(this)
                        .load(posterUrl)
                        .placeholder(R.drawable.placeholder_poster)
                        .into(moviePoster);
            } else {
                moviePoster.setImageResource(R.drawable.placeholder_poster);
            }

            // Save movie ID
            movieId = movie.getMovieId();
        }
    }

    private void updateShowtimeInfo(GetShowResponse showResponse) {
        // Format showtime
        String startTime = showResponse.getShowStartTime();

        try {
            // Parse the API date format (assuming it's in ISO format)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(startTime);

            // Format for display
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));

            String formattedTime = timeFormat.format(date);
            String formattedDate = dateFormat.format(date);

            showtime.setText("Suất: " + formattedTime + " - " + formattedDate);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing date", e);
            showtime.setText("Suất: " + startTime);
        }

        // Save showtime ID
        showtimeId = showResponse.getShowId();
    }

    private void loadAvailableShowtimes(List<ShowDTO> otherShows) {
        if (otherShows != null && !otherShows.isEmpty()) {
            showtimes.clear();

            for (ShowDTO showDTO : otherShows) {
                ShowTime st = new ShowTime();
                st.setId(showDTO.getShowId());

                // Format the time
                String startTime = showDTO.getShowStartTime();
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    Date date = inputFormat.parse(startTime);
                    st.setStartTime(timeFormat.format(date));
                    st.setDate(dateFormat.format(date));
                    st.setRoom(theaterName.getText().toString());
                    st.setPrice(90000); // Default price if not provided by API

                    showtimes.add(st);
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing showtime date", e);
                }
            }

            // Setup spinner
            List<String> showtimeStrings = new ArrayList<>();
            for (ShowTime st : showtimes) {
                showtimeStrings.add(st.getStartTime());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, showtimeStrings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            showtimeSpinner.setAdapter(adapter);

            // Set selected showtime
            for (int i = 0; i < showtimes.size(); i++) {
                if (showtimes.get(i).getId() == showtimeId) {
                    showtimeSpinner.setSelection(i);
                    updateShowtimeInfo(showtimes.get(i));
                    break;
                }
            }
        }
    }

    private void loadSeatMap(List<ShowSeatDTO> seats) {
        // Reset selected seats
        selectedSeats.clear();

        // Reset all seats to available
        for (TextView seatView : seatViews.values()) {
            seatView.setBackground(getResources().getDrawable(R.drawable.seat_available));
            seatView.setTextColor(getResources().getColor(android.R.color.black));
        }

        // In a real implementation, you would use the seats data to determine which seats are available
        // For now, we'll use a simplified approach with some hardcoded unavailable seats

        // Set some seats as unavailable (for demonstration)
        String[] unavailableSeats = {"I4", "I5", "I6", "H3", "H4", "H5", "H6", "G3", "G4", "G5", "G6"};
        for (String seatId : unavailableSeats) {
            TextView seatView = seatViews.get(seatId);
            if (seatView != null) {
                seatView.setBackground(getResources().getDrawable(R.drawable.seat_unavailable));
                seatView.setTextColor(getResources().getColor(R.color.purple_700));
            }
        }

        // Update total price
        updateTotalPrice();
    }

    private void updateShowtimeInfo(ShowTime showtime) {
        // Format showtime
        try {
            // Format for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(showtime.getDate());
            String formattedDate = dateFormat.format(date);

            this.showtime.setText("Suất: " + showtime.getStartTime() + " - " + formattedDate);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing date", e);
            this.showtime.setText("Suất: " + showtime.getStartTime() + " - " + showtime.getDate());
        }

        // Update ticket price
        ticketPrice = showtime.getPrice();
    }

    private void loadSeatMap() {
        // Reset selected seats
        selectedSeats.clear();

        // Reset all seats to available
        for (TextView seatView : seatViews.values()) {
            seatView.setBackground(getResources().getDrawable(R.drawable.seat_available));
            seatView.setTextColor(getResources().getColor(android.R.color.black));
        }

        // Set some seats as unavailable (for demonstration)
        String[] unavailableSeats = {"I4", "I5", "I6", "H3", "H4", "H5", "H6", "G3", "G4", "G5", "G6"};
        for (String seatId : unavailableSeats) {
            TextView seatView = seatViews.get(seatId);
            if (seatView != null) {
                seatView.setBackground(getResources().getDrawable(R.drawable.seat_unavailable));
                seatView.setTextColor(getResources().getColor(R.color.purple_700));
            }
        }

        // Update total price
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice.setText(currencyFormatter.format(getTotalPrice()) + " đ");
    }

    private double getTotalPrice() {
        return selectedSeats.size() * ticketPrice;
    }
}
