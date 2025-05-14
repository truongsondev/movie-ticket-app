package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.data.model.PaymentMethod;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";

    // UI Components
    private ImageButton btnBack, btnClose;
    private Button btnApplyPromo, btnGoBack, btnPay;
    private EditText etPromoCode;
    private TextView tvMovieTitle, tvMovieFormat, tvTheaterName, tvShowtime;
    private TextView tvTicketSummary, tvSeatInfo, tvTotalPrice;
    private ImageView ivMoviePoster;
    private LinearLayout layoutYourPromos, layoutStarsPoints;
    private TextView tvYourPromos, tvStarsPoints;
    private RadioButton rbOnePay, rbMomo, rbZaloPay, rbCash;

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
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize currency formatter
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Get data from intent
        getIntentData();

        // Initialize views
        initViews();

        // Setup click listeners
        setupListeners();

        // Load movie data
        loadMovieData();

        // Load payment methods
        loadPaymentMethods();

        // Update ticket summary
        updateTicketSummary();
    }

    private void getIntentData() {
        movieId = getIntent().getIntExtra("MOVIE_ID", 0);
        theaterId = getIntent().getIntExtra("THEATER_ID", 0);
        showtimeId = getIntent().getIntExtra("SHOWTIME_ID", 0);
        selectedSeats = getIntent().getStringExtra("SELECTED_SEATS");
        ticketPrice = getIntent().getDoubleExtra("TICKET_PRICE", 0);
        foodPrice = getIntent().getDoubleExtra("FOOD_PRICE", 0);

        // In a real app, you would fetch these from your API based on the IDs
        movieTitle = "Thám Tử Kiền: Kỳ Án Không Đầu";
        movieFormat = "2D Phụ Đề - T16";
        theaterName = "Galaxy Nguyễn Du - RAP 5";
        showtimeText = "Suất: 18:15 - Thứ Ba, 13/05/2025";

        // Calculate total price
        totalPrice = ticketPrice + foodPrice;
    }

    private void initViews() {
        // Toolbar buttons
        btnBack = findViewById(R.id.btnBack);
        btnClose = findViewById(R.id.btnClose);

        // Movie info
        ivMoviePoster = findViewById(R.id.ivMoviePoster);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieFormat = findViewById(R.id.tvMovieFormat);
        tvTheaterName = findViewById(R.id.tvTheaterName);
        tvShowtime = findViewById(R.id.tvShowtime);

        // Promo section
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        layoutYourPromos = findViewById(R.id.layoutYourPromos);
        tvYourPromos = findViewById(R.id.tvYourPromos);
        layoutStarsPoints = findViewById(R.id.layoutStarsPoints);
        tvStarsPoints = findViewById(R.id.tvStarsPoints);

        // Payment methods
        rbOnePay = findViewById(R.id.rbOnePay);
        rbMomo = findViewById(R.id.rbMomo);
        rbZaloPay = findViewById(R.id.rbZaloPay);
        rbCash = findViewById(R.id.rbCash);

        // Bottom bar
        tvTicketSummary = findViewById(R.id.tvTicketSummary);
        tvSeatInfo = findViewById(R.id.tvSeatInfo);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnPay = findViewById(R.id.btnPay);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnClose.setOnClickListener(v -> finish());

        btnApplyPromo.setOnClickListener(v -> {
            String promoCode = etPromoCode.getText().toString().trim();
            if (promoCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã khuyến mãi", Toast.LENGTH_SHORT).show();
                return;
            }

            // In a real app, you would validate the promo code with your API
            Toast.makeText(this, "Đang áp dụng mã khuyến mãi: " + promoCode, Toast.LENGTH_SHORT).show();
        });

        layoutYourPromos.setOnClickListener(v -> {
            // Toggle visibility of your promos content
            if (tvYourPromos.getVisibility() == View.VISIBLE) {
                tvYourPromos.setVisibility(View.GONE);
            } else {
                tvYourPromos.setVisibility(View.VISIBLE);
            }
        });

        layoutStarsPoints.setOnClickListener(v -> {
            // Toggle visibility of stars points content
            if (tvStarsPoints.getVisibility() == View.VISIBLE) {
                tvStarsPoints.setVisibility(View.GONE);
            } else {
                tvStarsPoints.setVisibility(View.VISIBLE);
            }
        });

        btnGoBack.setOnClickListener(v -> finish());

        btnPay.setOnClickListener(v -> {
            // In a real app, you would process the payment here
            Toast.makeText(this, "Đang xử lý thanh toán...", Toast.LENGTH_SHORT).show();

            // Navigate to confirmation screen
            Intent intent = new Intent(PaymentActivity.this, ConfirmationActivity.class);
            intent.putExtra("MOVIE_ID", movieId);
            intent.putExtra("THEATER_ID", theaterId);
            intent.putExtra("SHOWTIME_ID", showtimeId);
            intent.putExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("TICKET_PRICE", ticketPrice);
            intent.putExtra("FOOD_PRICE", foodPrice);
            intent.putExtra("TOTAL_PRICE", totalPrice);
            startActivity(intent);
        });
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

    private void loadPaymentMethods() {
        // In a real app, you would load payment methods from your API
        // For now, we'll just set OnePay as selected
        rbOnePay.setChecked(true);
    }

    private void updateTicketSummary() {
        // Set ticket summary
        tvTicketSummary.setText("1x Người Lớn - Member");

        // Set seat info
        tvSeatInfo.setText("Ghế: A9");

        // Set total price
        tvTotalPrice.setText(currencyFormatter.format(totalPrice) + " đ");
    }
}
