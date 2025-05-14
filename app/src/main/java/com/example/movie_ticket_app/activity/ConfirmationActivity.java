package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_app.MainActivity;
import com.example.movie_ticket_app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
    private ImageView ivMoviePoster, ivQrCode;

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

        // In a real app, you would fetch these from your API based on the IDs
        movieTitle = "Thám Tử Kiền: Kỳ Án Không Đầu";
        movieFormat = "2D Phụ Đề - T16";
        theaterName = "Galaxy Nguyễn Du - RAP 5";
        showtimeText = "Suất: 18:15 - Thứ Ba, 13/05/2025";
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

        // QR Code
        ivQrCode = findViewById(R.id.ivQrCode);

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
            // In a real app, you would save the ticket to the device or share it
            Toast.makeText(this, "Đã lưu vé vào thiết bị", Toast.LENGTH_SHORT).show();
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
            ivMoviePoster.setImageResource(R.drawable.placeholder_poster);
        }
    }

    private void updateTicketSummary() {
        // Set ticket summary
        tvTicketSummary.setText("1x Người Lớn - Member");

        // Set seat info
        tvSeatInfo.setText("Ghế: A9");

        // Set total price
        tvTotalPrice.setText(currencyFormatter.format(totalPrice) + " đ");
    }

    private void generateQrCode() {
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
        }
    }
}
