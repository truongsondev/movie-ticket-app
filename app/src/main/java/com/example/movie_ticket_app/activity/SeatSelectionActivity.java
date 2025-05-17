package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.data.model.Seat;
import com.example.movie_ticket_app.data.model.ShowTime;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final String TAG = "SeatSelectionActivity";

    // UI Components
    private ImageButton btnBack;
    private Button btnBack2, btnContinue;
    private TextView movieTitle, movieFormat, movieRoom, theaterName, showtime, totalPrice;
    private TextView selectedSeatsInfo; // Added for displaying selected seats
    private Spinner showtimeSpinner;

    // Data
    private int showId = 1;
    private int theaterId = 1;
    private int showtimeId = 1;
    private List<ShowTime> showtimes = new ArrayList<>();
    private List<Seat> selectedSeats = new ArrayList<>();
    private Map<String, TextView> seatViews = new HashMap<>();
    private double ticketPrice = 90000; // Default price in VND
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Initialize currency formatter
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Initialize views
        initViews();

        // Setup click listeners
        setupListeners();

        // Load hardcoded data
        loadHardcodedData();
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
        selectedSeatsInfo = findViewById(R.id.selectedSeatsInfo); // Initialize the selected seats info TextView

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

            // Format selected seats for display
            String formattedSeats = formatSelectedSeats();

            // Navigate to food selection screen
            Intent intent = new Intent(SeatSelectionActivity.this, FoodSelectionActivity.class);
            intent.putExtra("MOVIE_ID", showId);
            intent.putExtra("THEATER_ID", theaterId);
            intent.putExtra("SHOWTIME_ID", showtimeId);
            intent.putExtra("SELECTED_SEATS", formattedSeats);
            intent.putExtra("TICKET_PRICE", getTotalPrice());
            startActivity(intent);
        });

        showtimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowTime selected = showtimes.get(position);
                showtimeId = selected.getId();
                updateShowtimeInfo(selected);

                // Reset seat selection when changing showtime
                resetSeatSelection();
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

                // Update selected seats info
                updateSelectedSeatsInfo();
            });
        }
    }

    private void loadHardcodedData() {
        // Set movie information
        movieTitle.setText("Biệt đội sấm sét");
        movieFormat.setText("2D Phụ Đề - ");
        movieRoom.setText("T16");
        theaterName.setText("Rạp B");

        // Create hardcoded showtimes
        createHardcodedShowtimes();

        // Set initial showtime
        if (!showtimes.isEmpty()) {
            ShowTime initialShowtime = showtimes.get(0);
            updateShowtimeInfo(initialShowtime);
        }

        // Load seat map with hardcoded unavailable seats
        loadSeatMap();
    }

    private void createHardcodedShowtimes() {
        showtimes.clear();

        // Create some hardcoded showtimes
        ShowTime st1 = new ShowTime();
        st1.setId(1);
        st1.setStartTime("20:00");
        st1.setDate("2025-06-01");
        st1.setRoom("Rạp B");
        st1.setPrice(90000);
        showtimes.add(st1);

        ShowTime st2 = new ShowTime();
        st2.setId(2);
        st2.setStartTime("20:30");
        st2.setDate("2025-05-13");
        st2.setRoom("Rạp B");
        st2.setPrice(90000);
        showtimes.add(st2);

        ShowTime st3 = new ShowTime();
        st3.setId(3);
        st3.setStartTime("20:00");
        st3.setDate("2025-6-1");
        st3.setRoom("Rạp B");
        st3.setPrice(90000);
        showtimes.add(st3);

        // Setup spinner
        List<String> showtimeStrings = new ArrayList<>();
        for (ShowTime st : showtimes) {
            showtimeStrings.add(st.getStartTime());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, showtimeStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showtimeSpinner.setAdapter(adapter);
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
            this.showtime.setText("Suất: " + showtime.getStartTime() + " - " + showtime.getDate());
        }

        // Update ticket price
        ticketPrice = showtime.getPrice();
    }

    private void loadSeatMap() {
        // Reset all seats to available
        for (TextView seatView : seatViews.values()) {
            seatView.setBackground(getResources().getDrawable(R.drawable.seat_available));
            seatView.setTextColor(getResources().getColor(android.R.color.black));
        }

        // Set some seats as unavailable (hardcoded)
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

    private void resetSeatSelection() {
        // Clear selected seats
        selectedSeats.clear();

        // Reset seat map
        loadSeatMap();

        // Update UI
        updateTotalPrice();
        updateSelectedSeatsInfo();
    }

    private void updateTotalPrice() {
        totalPrice.setText(currencyFormatter.format(getTotalPrice()) + " đ");
    }

    private void updateSelectedSeatsInfo() {
        if (selectedSeats.isEmpty()) {
            selectedSeatsInfo.setVisibility(View.GONE);
            return;
        }

        selectedSeatsInfo.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder("Ghế đã chọn: ");

        for (int i = 0; i < selectedSeats.size(); i++) {
            Seat seat = selectedSeats.get(i);
            sb.append(seat.getRow()).append(seat.getNumber());

            if (i < selectedSeats.size() - 1) {
                sb.append(", ");
            }
        }

        selectedSeatsInfo.setText(sb.toString());
    }

    private double getTotalPrice() {
        return selectedSeats.size() * ticketPrice;
    }

    // Format selected seats for display in FoodSelectionActivity
    private String formatSelectedSeats() {
        StringBuilder sb = new StringBuilder();

        // Add seat count
        sb.append(selectedSeats.size()).append("x");

        // Add seat type
        if (selectedSeats.size() == 1) {
            sb.append(" Ghế đơn\nGhế: ");
        } else {
            sb.append(" Ghế\nGhế: ");
        }

        // Add seat names
        for (int i = 0; i < selectedSeats.size(); i++) {
            Seat seat = selectedSeats.get(i);
            sb.append(seat.getRow()).append(seat.getNumber());

            if (i < selectedSeats.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}