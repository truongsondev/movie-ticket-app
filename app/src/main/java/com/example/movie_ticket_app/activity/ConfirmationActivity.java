package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_app.MainActivity;
import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.data.model.FoodItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ConfirmationActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmationActivity";

    // UI Components
    private ImageButton btnClose;
    private Button btnBackToHome, btnSaveTicket;
    private TextView tvBookingId, tvBookingDate;
    private TextView tvMovieTitle, tvMovieFormat, tvTheaterName, tvShowtime;
    private TextView tvTicketSummary, tvSeatInfo, tvTotalPrice;
    private TextView tvFoodSummary; // Added for food summary
    private ImageView ivMoviePoster, ivQrCode;
    private LinearLayout foodSummaryContainer; // Container for food summary

    // Data
    private int movieId;
    private int theaterId;
    private int showtimeId;
    private String selectedSeats;
    private double ticketPrice;
    private double foodPrice;
    private double totalPrice;
    private String movieTitle;
    private String movieFormat;
    private String theaterName;
    private String showtimeText;
    private String moviePosterUrl;
    private String bookingId;
    private ArrayList<String> foodNames;
    private ArrayList<Integer> foodQuantities;
    private ArrayList<String> foodPricesStr;
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize currency formatter
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Get data from intent
        getIntentData();

        // Initialize views
        initViews();

        // Setup click listeners
        setupListeners();

        // Generate booking ID
        generateBookingId();

        // Load movie data
        loadMovieData();

        // Update ticket summary
        updateTicketSummary();

        // Update food summary
        updateFoodSummary();

        // Generate QR code
        generateQrCode();
    }

    private void getIntentData() {
        movieId = getIntent().getIntExtra("MOVIE_ID", 0);
        theaterId = getIntent().getIntExtra("THEATER_ID", 0);
        showtimeId = getIntent().getIntExtra("SHOWTIME_ID", 0);
        selectedSeats = getIntent().getStringExtra("SELECTED_SEATS");
        ticketPrice = getIntent().getDoubleExtra("TICKET_PRICE", 0);
        foodPrice = getIntent().getDoubleExtra("FOOD_PRICE", 0);
        totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0);

        // Get food information
        foodNames = getIntent().getStringArrayListExtra("FOOD_NAMES");
        foodQuantities = getIntent().getIntegerArrayListExtra("FOOD_QUANTITIES");
        foodPricesStr = getIntent().getStringArrayListExtra("FOOD_PRICES");

        // In a real app, you would fetch these from your API based on the IDs
        movieTitle = "Biệt đội sấm set";
        movieFormat = "2D Phụ Đề - T16";
        theaterName = "RAP B";
        showtimeText = "Suất: 20:00 - Chủ Nhật, 1/6/2025";
    }

    private void initViews() {
        // Toolbar buttons
        btnClose = findViewById(R.id.btnClose);

        // Booking info
        tvBookingId = findViewById(R.id.tvBookingId);
        tvBookingDate = findViewById(R.id.tvBookingDate);

        // Movie info
        ivMoviePoster = findViewById(R.id.ivMoviePoster);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieFormat = findViewById(R.id.tvMovieFormat);
        tvTheaterName = findViewById(R.id.tvTheaterName);
        tvShowtime = findViewById(R.id.tvShowtime);

        // Ticket summary
        tvTicketSummary = findViewById(R.id.tvTicketSummary);
        tvSeatInfo = findViewById(R.id.tvSeatInfo);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        // Food summary
        foodSummaryContainer = findViewById(R.id.foodSummaryContainer);
        tvFoodSummary = findViewById(R.id.tvFoodSummary);

        // If food summary TextView doesn't exist, create it
        if (tvFoodSummary == null && foodNames != null && !foodNames.isEmpty()) {
            tvFoodSummary = new TextView(this);
            tvFoodSummary.setId(View.generateViewId());
            tvFoodSummary.setTextColor(getResources().getColor(android.R.color.black));
            tvFoodSummary.setTextSize(14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 4;
            tvFoodSummary.setLayoutParams(params);

            if (foodSummaryContainer != null) {
                foodSummaryContainer.addView(tvFoodSummary);
            }
        }

        // QR Code


        // Buttons
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnSaveTicket = findViewById(R.id.btnSaveTicket);
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> {
            // Go back to home screen
            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnBackToHome.setOnClickListener(v -> {
            // Go back to home screen
            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnSaveTicket.setOnClickListener(v -> {
            // Changed: Now also returns to MainActivity instead of showing toast
            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void generateBookingId() {
        // Generate a random booking ID
        Random random = new Random();
        bookingId = "GXY" + String.format("%06d", random.nextInt(1000000));
        tvBookingId.setText("Mã đặt vé: " + bookingId);

        // Set current date as booking date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
        String currentDate = dateFormat.format(new Date());
        tvBookingDate.setText("Ngày đặt: " + currentDate);
    }

    private void loadMovieData() {
        // Set movie info
        tvMovieTitle.setText(movieTitle);
        tvMovieFormat.setText(movieFormat);
        tvTheaterName.setText(theaterName);
        tvShowtime.setText(showtimeText);

        // Load movie poster
        if (moviePosterUrl != null && !moviePosterUrl.isEmpty()) {
            Glide.with(this)
                    .load(moviePosterUrl)
                    .placeholder(R.drawable.placeholder_poster)
                    .into(ivMoviePoster);
        } else {
            // Set default poster
            ivMoviePoster.setImageResource(R.drawable.ss);
        }
    }

    private void updateTicketSummary() {
        // Parse the selected seats string to get the count
        String seatCount = "1x"; // Default
        if (selectedSeats != null && selectedSeats.contains("x")) {
            seatCount = selectedSeats.substring(0, selectedSeats.indexOf("x") + 1);
        }

        // Set ticket summary based on seat count
        if (seatCount.startsWith("1")) {
            tvTicketSummary.setText(seatCount + " Người Lớn - Member");
        } else {
            tvTicketSummary.setText(seatCount + " Người Lớn - Member");
        }

        // Set seat info
        if (selectedSeats != null && selectedSeats.contains("Ghế:")) {
            String seatInfo = selectedSeats.substring(selectedSeats.indexOf("Ghế:"));
            tvSeatInfo.setText(seatInfo);
        } else {
            tvSeatInfo.setText("Ghế: Không xác định");
        }

        // Set total price
        tvTotalPrice.setText(currencyFormatter.format(totalPrice) + " đ");
    }

    private void updateFoodSummary() {
        // If there are no food items or the TextView doesn't exist, return
        if (foodNames == null || foodNames.isEmpty() || tvFoodSummary == null) {
            if (tvFoodSummary != null) {
                tvFoodSummary.setVisibility(View.GONE);
            }
            return;
        }

        StringBuilder foodSummary = new StringBuilder("Đồ ăn: ");

        for (int i = 0; i < foodNames.size(); i++) {
            String name = foodNames.get(i);
            int quantity = foodQuantities.get(i);

            foodSummary.append(quantity).append("x ").append(name);

            if (i < foodNames.size() - 1) {
                foodSummary.append(", ");
            }
        }

        tvFoodSummary.setText(foodSummary.toString());
        tvFoodSummary.setVisibility(View.VISIBLE);
    }

    private void generateQrCode() {
        if (ivQrCode == null) {
            return; // Skip if QR code ImageView is not found
        }

        try {
            // Generate QR code content
            String qrContent = "BOOKING:" + bookingId +
                    "|MOVIE:" + movieId +
                    "|THEATER:" + theaterId +
                    "|SHOWTIME:" + showtimeId +
                    "|SEATS:" + selectedSeats;

            // Generate QR code bitmap
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            // Set QR code image
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            // If QR code generation fails, show a placeholder
            ivQrCode.setImageResource(R.drawable.qr_placeholder);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
        }
    }
}