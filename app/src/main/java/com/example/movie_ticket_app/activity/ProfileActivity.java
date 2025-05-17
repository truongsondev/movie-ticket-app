package com.example.movie_ticket_app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvPhoneNumber;
    private TextView tvEmail;
    private Button btnLogout;
    private SessionManager sessionManager;
    private LinearLayout tabInfo, tabTransaction, tabNotification;
    private LinearLayout itemHotline, itemEmail, itemCompanyInfo, itemTerms, itemPaymentPolicy, itemPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        // Initialize views
        initViews();

        // Display user information
        displayUserInfo();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        // User info
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvEmail = findViewById(R.id.tvEmail);

        // Tabs
        tabInfo = findViewById(R.id.tabInfo);
        tabTransaction = findViewById(R.id.tabTransaction);
        tabNotification = findViewById(R.id.tabNotification);

        // Menu items
        itemHotline = findViewById(R.id.itemHotline);
        itemEmail = findViewById(R.id.itemEmail);
        itemCompanyInfo = findViewById(R.id.itemCompanyInfo);
        itemTerms = findViewById(R.id.itemTerms);
        itemPaymentPolicy = findViewById(R.id.itemPaymentPolicy);
        itemPrivacyPolicy = findViewById(R.id.itemPrivacyPolicy);

        // Logout button
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void displayUserInfo() {
        // Set username from session
        String username = sessionManager.getUserName();
        if (username != null && !username.isEmpty()) {
            tvUserName.setText(username);
        }

        // Set phone number from session
        String mobile = sessionManager.getUserMobile();
        if (mobile != null && !mobile.isEmpty()) {
            tvPhoneNumber.setText("Số điện thoại: " + mobile);
        }

        // Set email from session
        String email = sessionManager.getUserEmail();
        if (email != null && !email.isEmpty()) {
            tvEmail.setText("Email: " + email);
        }
    }

    private void setupClickListeners() {
        // Tab click listeners
        tabInfo.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Thông tin", Toast.LENGTH_SHORT).show();
        });

        tabTransaction.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Giao dịch", Toast.LENGTH_SHORT).show();
        });

        tabNotification.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Thông báo", Toast.LENGTH_SHORT).show();
        });

        // Menu item click listeners
        itemHotline.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Gọi điện thoại", Toast.LENGTH_SHORT).show();
        });

        itemEmail.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Gửi email", Toast.LENGTH_SHORT).show();
        });

        itemCompanyInfo.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Thông tin công ty", Toast.LENGTH_SHORT).show();
        });

        itemTerms.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Điều khoản sử dụng", Toast.LENGTH_SHORT).show();
        });

        itemPaymentPolicy.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Chính sách thanh toán", Toast.LENGTH_SHORT).show();
        });

        itemPrivacyPolicy.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Chính sách bảo mật", Toast.LENGTH_SHORT).show();
        });

        // Logout button click listener
        btnLogout.setOnClickListener(v -> {
            // Logout user
            if (sessionManager != null) {
                sessionManager.logout();
            }

            // Show success message
            Toast.makeText(ProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

            // Close this activity and return to previous screen
            finish();
        });
    }
}
