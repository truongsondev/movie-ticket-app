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
import com.example.movie_ticket_app.data.model.FoodItem;
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
    private TextView tvTicketSummary, tvSeatInfo, tvTotalPrice, tvSeatPrice;
    private TextView tvFoodSummary; // Added for food summary
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
    private ArrayList<FoodItem> selectedFoodItems; // Added for selected food items
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

        // Update food summary
        updateFoodSummary();
    }

    private void getIntentData() {
        movieId = getIntent().getIntExtra("MOVIE_ID", 0);
        theaterId = getIntent().getIntExtra("THEATER_ID", 0);
        showtimeId = getIntent().getIntExtra("SHOWTIME_ID", 0);
        selectedSeats = getIntent().getStringExtra("SELECTED_SEATS");
        ticketPrice = getIntent().getDoubleExtra("TICKET_PRICE", 0);
        foodPrice = getIntent().getDoubleExtra("FOOD_PRICE", 0);

        // Get selected food items if available
        selectedFoodItems = getIntent().getParcelableArrayListExtra("SELECTED_FOOD_ITEMS");
        if (selectedFoodItems == null) {
            selectedFoodItems = new ArrayList<>();
        }

        // In a real app, you would fetch these from your API based on the IDs
        movieTitle = "Biệt đội sấm set";
        movieFormat = "2D Phụ Đề - T16";
        theaterName = "RAP B";
        showtimeText = "Suất: 20:00 - Chủ Nhật, 1/6/2025";

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
        tvSeatPrice = findViewById(R.id.tvSeatPrice);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnPay = findViewById(R.id.btnPay);

        // Add food summary TextView
        // First, check if the food summary TextView already exists in the layout
        tvFoodSummary = findViewById(R.id.tvFoodSummary);

        // If it doesn't exist, create it dynamically
        if (tvFoodSummary == null && !selectedFoodItems.isEmpty()) {
            LinearLayout summaryContainer = findViewById(R.id.summaryContainer);
            if (summaryContainer != null) {
                tvFoodSummary = new TextView(this);
                tvFoodSummary.setId(View.generateViewId());
                tvFoodSummary.setTextColor(getResources().getColor(android.R.color.black));
                tvFoodSummary.setTextSize(14);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                tvFoodSummary.setLayoutParams(params);
                summaryContainer.addView(tvFoodSummary);
            }
        }
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

            // Create lists to store food information
            ArrayList<String> foodNames = new ArrayList<>();
            ArrayList<Integer> foodQuantities = new ArrayList<>();
            ArrayList<String> foodPricesStr = new ArrayList<>();

            // Collect selected food items information
            if (selectedFoodItems != null) {
                for (FoodItem item : selectedFoodItems) {
                    foodNames.add(item.getName());
                    foodQuantities.add(item.getQuantity());
                    foodPricesStr.add(String.valueOf(item.getPrice()));
                }
            }

            // Navigate to confirmation screen
            Intent intent = new Intent(PaymentActivity.this, ConfirmationActivity.class);
            intent.putExtra("MOVIE_ID", movieId);
            intent.putExtra("THEATER_ID", theaterId);
            intent.putExtra("SHOWTIME_ID", showtimeId);
            intent.putExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("TICKET_PRICE", ticketPrice);
            intent.putExtra("FOOD_PRICE", foodPrice);
            intent.putExtra("TOTAL_PRICE", totalPrice);

            // Pass food information as string arrays
            intent.putStringArrayListExtra("FOOD_NAMES", foodNames);
            intent.putIntegerArrayListExtra("FOOD_QUANTITIES", foodQuantities);
            intent.putStringArrayListExtra("FOOD_PRICES", foodPricesStr);

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
            ivMoviePoster.setImageResource(R.drawable.ss);
        }
    }

    private void loadPaymentMethods() {
        // In a real app, you would load payment methods from your API
        // For now, we'll just set OnePay as selected
        rbOnePay.setChecked(true);
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

        // Set seat price
        tvSeatPrice.setText(currencyFormatter.format(ticketPrice) + " đ");

        // Set total price
        tvTotalPrice.setText(currencyFormatter.format(totalPrice) + " đ");
    }

    private void updateFoodSummary() {
        // If there are no selected food items or the TextView doesn't exist, return
        if (selectedFoodItems.isEmpty() || tvFoodSummary == null) {
            return;
        }

        StringBuilder foodSummary = new StringBuilder("Đồ ăn: ");

        for (int i = 0; i < selectedFoodItems.size(); i++) {
            FoodItem item = selectedFoodItems.get(i);
            foodSummary.append(item.getQuantity()).append("x ").append(item.getName());

            if (i < selectedFoodItems.size() - 1) {
                foodSummary.append(", ");
            }
        }

        tvFoodSummary.setText(foodSummary.toString());
    }
}