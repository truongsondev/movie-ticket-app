package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.adapter.FoodItemAdapter;
import com.example.movie_ticket_app.data.model.FoodItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodSelectionActivity extends AppCompatActivity {

    private static final String TAG = "FoodSelectionActivity";

    // UI Components
    private ImageButton btnBack;
    private Button btnContinue, btnGoBack;
    private TextView tvTitle, tvTotalPrice, tvSeatSummary, tvSeatPrice; // Added tvSeatPrice
    private RecyclerView recyclerViewFoodItems;

    // Data
    private int movieId;
    private int theaterId;
    private int showtimeId;
    private String selectedSeats;
    private double ticketPrice;
    private double foodPrice = 0;
    private List<FoodItem> foodItems = new ArrayList<>();
    private List<FoodItem> selectedFoodItems = new ArrayList<>();
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_selection);

        // Initialize currency formatter
        currencyFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Get data from intent
        movieId = getIntent().getIntExtra("MOVIE_ID", 0);
        theaterId = getIntent().getIntExtra("THEATER_ID", 0);
        showtimeId = getIntent().getIntExtra("SHOWTIME_ID", 0);
        selectedSeats = getIntent().getStringExtra("SELECTED_SEATS");
        ticketPrice = getIntent().getDoubleExtra("TICKET_PRICE", 0);

        // Initialize views
        initViews();

        // Setup click listeners
        setupListeners();

        // Load food items
        loadFoodItems();

        // Update seat summary
        updateSeatSummary();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);
        btnGoBack = findViewById(R.id.btnGoBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvSeatSummary = findViewById(R.id.tvSeatSummary);
        tvSeatPrice = findViewById(R.id.tvSeatPrice); // Initialize tvSeatPrice
        recyclerViewFoodItems = findViewById(R.id.recyclerViewFoodItems);

        // Setup RecyclerView
        recyclerViewFoodItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnGoBack.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {
            // Navigate to payment screen
            Intent intent = new Intent(FoodSelectionActivity.this, PaymentActivity.class);
            intent.putExtra("MOVIE_ID", movieId);
            intent.putExtra("THEATER_ID", theaterId);
            intent.putExtra("SHOWTIME_ID", showtimeId);
            intent.putExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("TICKET_PRICE", ticketPrice);
            intent.putExtra("FOOD_PRICE", foodPrice);
            startActivity(intent);
        });
    }

    private void loadFoodItems() {
        // Create sample food items
        foodItems.clear();

        // Capybara Set
        FoodItem capybaraSet = new FoodItem();
        capybaraSet.setId(1);
        capybaraSet.setName("Capybara Set");
        capybaraSet.setDescription("Capybara lạc bước, đắt tay em về nào stars ơi, đừng quên 1 bắp ngọt và 1 nước size L tặng kèm nhé");
        capybaraSet.setPrice(350000);
        capybaraSet.setImageResourceId(R.drawable.food_capybara_set);
        foodItems.add(capybaraSet);

        // Combo 2 Big
        FoodItem combo2Big = new FoodItem();
        combo2Big.setId(2);
        combo2Big.setName("Combo 2 Big");
        combo2Big.setDescription("\"Nhân đôi sự sảng khoái! Combo 2 gồm 1 bắp rang bơ lớn, 2 Pepsi cỡ lớn – tiết kiệm hơn 28,000!");
        combo2Big.setPrice(109000);
        combo2Big.setImageResourceId(R.drawable.food_combo_2_big);
        foodItems.add(combo2Big);

        // Combo 2 Big Extra
        FoodItem combo2BigExtra = new FoodItem();
        combo2BigExtra.setId(3);
        combo2BigExtra.setName("Combo 2 Big Extra");
        combo2BigExtra.setDescription("\"Nhân đôi sự sảng khoái! Combo 2 gồm 1 bắp rang bơ lớn, 2 Pepsi cỡ lớn + 1 snack tùy chọn– tiết kiệm hơn 33,000!");
        combo2BigExtra.setPrice(129000);
        combo2BigExtra.setImageResourceId(R.drawable.food_combo_2_big_extra);
        foodItems.add(combo2BigExtra);

        // Combo Friends 1 Big
        FoodItem comboFriends1Big = new FoodItem();
        comboFriends1Big.setId(4);
        comboFriends1Big.setName("Combo Friends 1 Big");
        comboFriends1Big.setDescription("\"Chia sẻ niềm vui với bạn bè! Combo gồm 1 bắp rang bơ lớn, 4 Pepsi cỡ lớn – tiết kiệm hơn 45,000!");
        comboFriends1Big.setPrice(189000);
        comboFriends1Big.setImageResourceId(R.drawable.food_combo_friends);
        foodItems.add(comboFriends1Big);

        // Set adapter
        FoodItemAdapter adapter = new FoodItemAdapter(this, foodItems, new FoodItemAdapter.OnFoodItemClickListener() {
            @Override
            public void onIncreaseQuantity(FoodItem foodItem) {
                foodItem.setQuantity(foodItem.getQuantity() + 1);
                updateFoodPrice();
                updateTotalPrice();
            }

            @Override
            public void onDecreaseQuantity(FoodItem foodItem) {
                if (foodItem.getQuantity() > 0) {
                    foodItem.setQuantity(foodItem.getQuantity() - 1);
                    updateFoodPrice();
                    updateTotalPrice();
                }
            }
        });
        recyclerViewFoodItems.setAdapter(adapter);
    }

    private void updateFoodPrice() {
        foodPrice = 0;
        selectedFoodItems.clear();

        for (FoodItem item : foodItems) {
            if (item.getQuantity() > 0) {
                foodPrice += item.getPrice() * item.getQuantity();
                selectedFoodItems.add(item);
            }
        }
    }

    private void updateTotalPrice() {
        double total = ticketPrice + foodPrice;
        tvTotalPrice.setText(currencyFormatter.format(total) + " đ");
    }

    private void updateSeatSummary() {
        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            // Display the formatted seat information that was passed from SeatSelectionActivity
            tvSeatSummary.setText(selectedSeats);

            // Update the seat price display
            tvSeatPrice.setText(currencyFormatter.format(ticketPrice) + " đ");

            // Update total price
            tvTotalPrice.setText(currencyFormatter.format(ticketPrice + foodPrice) + " đ");
        }
    }
}