package com.example.movie_ticket_app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.utils.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvMobile, tvGender, tvBirthday;
    private Button btnLogout;
    private SessionManager sessionManager;

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

        // Setup logout button
        setupLogoutButton();
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvGender = findViewById(R.id.tvGender);
        tvBirthday = findViewById(R.id.tvBirthday);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void displayUserInfo() {
        // Set username
        String username = sessionManager.getUserName();
        if (username != null && !username.isEmpty()) {
            tvUsername.setText(username);
        } else {
            tvUsername.setText("N/A");
        }

        // Set email
        String email = sessionManager.getUserEmail();
        if (email != null && !email.isEmpty()) {
            tvEmail.setText(email);
        } else {
            tvEmail.setText("N/A");
        }

        // Set mobile
        String mobile = sessionManager.getUserMobile();
        if (mobile != null && !mobile.isEmpty()) {
            tvMobile.setText(mobile);
        } else {
            tvMobile.setText("N/A");
        }

        // Set gender
        boolean gender = sessionManager.getUserGender();
        tvGender.setText(gender ? "Male" : "Female");

        // Set birthday
        List<Integer> birthday = sessionManager.getUserBirthday();
//        if (birthday != null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            tvBirthday.setText(birthday.format(formatter));
//        } else {
//            tvBirthday.setText("N/A");
//        }
    }

    private void setupLogoutButton() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout user
                sessionManager.logout();

                // Show success message
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Close this activity and return to previous screen
                finish();
            }
        });
    }
}
