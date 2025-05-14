package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;
import com.example.movie_ticket_app.dto.auth.request.LoginRequest;
import com.example.movie_ticket_app.dto.auth.response.LoginResponse;
import com.example.movie_ticket_app.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword, textViewRegister;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize session manager
        sessionManager = new SessionManager(getApplicationContext());

        // If already logged in, finish this activity
        if (sessionManager.isLoggedIn()) {
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup click listeners
        setupListeners();
    }

    private void initViews() {
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    loginUser();
                }
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to forgot password screen
                Toast.makeText(LoginActivity.this, "Forgot Password feature coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to register screen
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate email
        String email = editTextEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Validate password
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        return isValid;
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Show loading state
        setLoadingState(true);

        // Create login request
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call login API
        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<LoginResponse>> call = apiServices.login(loginRequest);

        call.enqueue(new Callback<APIResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<LoginResponse>> call, Response<APIResponse<LoginResponse>> response) {
                // Reset loading state
                setLoadingState(false);

                if (response.isSuccessful() && response.body() != null) {
                    APIResponse<LoginResponse> apiResponse = response.body();

                    if (apiResponse.getCode() == 20000 && apiResponse.getMetadata() != null) {
                        LoginResponse loginResponse = apiResponse.getMetadata();

                        // Save user session
                        sessionManager.createLoginSession(
                                loginResponse.getAccessToken(),
                                loginResponse.getRefreshToken(),
                                loginResponse.getProfile()
                        );

                        // Show success message
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Return to previous screen
                        finish();
                    } else {
                        // Handle API error message
                        String errorMessage = apiResponse.getMessage();
                        if (TextUtils.isEmpty(errorMessage)) {
                            errorMessage = "Login failed. Please try again.";
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle HTTP error
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error response: " + errorBody);
                        Toast.makeText(LoginActivity.this,
                                "Login failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                        Toast.makeText(LoginActivity.this,
                                "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<LoginResponse>> call, Throwable t) {
                // Reset loading state
                setLoadingState(false);

                // Log the error
                Log.e(TAG, "Network error during login", t);

                // Show error message
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoadingState(boolean isLoading) {
        buttonLogin.setEnabled(!isLoading);
        buttonLogin.setText(isLoading ? "Logging in..." : "Login");

        // Optionally disable input fields during loading
        editTextEmail.setEnabled(!isLoading);
        editTextPassword.setEnabled(!isLoading);
    }
}
