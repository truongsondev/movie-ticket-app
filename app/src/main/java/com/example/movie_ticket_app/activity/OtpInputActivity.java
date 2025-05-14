package com.example.movie_ticket_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;
//import com.example.movie_ticket_app.dto.auth.request.ResendOtpRequest;
import com.example.movie_ticket_app.dto.auth.request.VerifyOtpRequest;
import com.example.movie_ticket_app.dto.auth.response.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpInputActivity extends AppCompatActivity {

    private static final String TAG = "OtpInputActivity";

    // UI components
    private TextView otpTitle, otpDescription, resendCode;
    private EditText[] otpDigits = new EditText[6];
    private Button verifyButton;

    // Data
    private String email;
    private String token;
    private CountDownTimer resendTimer;
    private boolean canResend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Get data from intent
        email = getIntent().getStringExtra("EMAIL");
        token = getIntent().getStringExtra("TOKEN");

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup OTP input fields
        setupOtpInputs();

        // Setup click listeners
        setupListeners();

        // Update description with email
        updateDescription();

        // Start resend timer
        startResendTimer();
    }

    private void initViews() {
        otpTitle = findViewById(R.id.otpTitle);
        otpDescription = findViewById(R.id.otpDescription);

        // Initialize OTP digit fields
        otpDigits[0] = findViewById(R.id.otp_digit_1);
        otpDigits[1] = findViewById(R.id.otp_digit_2);
        otpDigits[2] = findViewById(R.id.otp_digit_3);
        otpDigits[3] = findViewById(R.id.otp_digit_4);
        otpDigits[4] = findViewById(R.id.otp_digit_5);
        otpDigits[5] = findViewById(R.id.otp_digit_6);

        verifyButton = findViewById(R.id.verifyButton);
        resendCode = findViewById(R.id.resendCode);
    }

    private void updateDescription() {
        if (!TextUtils.isEmpty(email)) {
            String maskedEmail = maskEmail(email);
            otpDescription.setText("We've sent a 6-digit code to " + maskedEmail);
        }
    }

    private String maskEmail(String email) {
        if (TextUtils.isEmpty(email) || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];

        if (name.length() <= 2) {
            return name + "@" + domain;
        }

        String maskedName = name.substring(0, 2) +
                new String(new char[name.length() - 2]).replace("\0", "*");

        return maskedName + "@" + domain;
    }

    private void setupOtpInputs() {
        // Add text change listeners to automatically move focus
        for (int i = 0; i < otpDigits.length; i++) {
            final int currentIndex = i;

            otpDigits[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && currentIndex < otpDigits.length - 1) {
                        // Move to next digit
                        otpDigits[currentIndex + 1].requestFocus();
                    }

                    // Check if all digits are filled
                    checkAllDigitsFilled();
                }
            });

            // Handle backspace to move to previous digit
            otpDigits[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL &&
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            currentIndex > 0 &&
                            TextUtils.isEmpty(otpDigits[currentIndex].getText())) {
                        // Move to previous digit
                        otpDigits[currentIndex - 1].requestFocus();
                        otpDigits[currentIndex - 1].setText("");
                        return true;
                    }
                    return false;
                }
            });
        }

        // Set focus on first digit
        otpDigits[0].requestFocus();
    }

    private void checkAllDigitsFilled() {
        boolean allFilled = true;

        for (EditText digit : otpDigits) {
            if (TextUtils.isEmpty(digit.getText())) {
                allFilled = false;
                break;
            }
        }

        verifyButton.setEnabled(allFilled);
    }

    private void setupListeners() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

//
    }

    private String getOtpCode() {
        StringBuilder otp = new StringBuilder();
        for (EditText digit : otpDigits) {
            otp.append(digit.getText().toString());
        }
        return otp.toString();
    }

    private void verifyOtp() {
        String otpCode = getOtpCode();

        if (otpCode.length() != 6) {
            Toast.makeText(this, "Please enter all 6 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        setLoadingState(true);

        // Create verify OTP request with token
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest(email, token, otpCode);

        // Call verify OTP API
        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<VerifyOtpResponse>> call = apiServices.verifyOtp(verifyOtpRequest);

        call.enqueue(new Callback<APIResponse<VerifyOtpResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<VerifyOtpResponse>> call, Response<APIResponse<VerifyOtpResponse>> response) {
                // Reset loading state
                setLoadingState(false);

                if (response.isSuccessful() && response.body() != null) {
                    APIResponse<VerifyOtpResponse> apiResponse = response.body();

                    if (apiResponse.getCode() == 20000) {
                        // Show success message
                        Toast.makeText(OtpInputActivity.this,
                                "Verification successful!",
                                Toast.LENGTH_SHORT).show();

                        // Navigate to login screen
                        navigateToLogin();
                    } else {
                        // Handle API error message
                        String errorMessage = apiResponse.getMessage();
                        if (TextUtils.isEmpty(errorMessage)) {
                            errorMessage = "Verification failed. Please try again.";
                        }
                        Toast.makeText(OtpInputActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                        // Clear OTP fields
                        clearOtpFields();
                    }
                } else {
                    // Handle HTTP error
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error response: " + errorBody);
                        Toast.makeText(OtpInputActivity.this,
                                "Verification failed: " + response.code(), Toast.LENGTH_SHORT).show();

                        // Clear OTP fields
                        clearOtpFields();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                        Toast.makeText(OtpInputActivity.this,
                                "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<VerifyOtpResponse>> call, Throwable t) {
                // Reset loading state
                setLoadingState(false);

                // Log the error
                Log.e(TAG, "Network error during verification", t);

                // Show error message
                Toast.makeText(OtpInputActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void clearOtpFields() {
        for (EditText digit : otpDigits) {
            digit.setText("");
        }
    }

    private void startResendTimer() {
        // Disable resend button
        canResend = false;
        resendCode.setEnabled(false);

        // Cancel existing timer if any
        if (resendTimer != null) {
            resendTimer.cancel();
        }

        // Start a 60-second countdown
        resendTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendCode.setText("Resend in " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                resendCode.setText("Resend");
                resendCode.setEnabled(true);
                canResend = true;
            }
        }.start();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(OtpInputActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoadingState(boolean isLoading) {
        verifyButton.setEnabled(!isLoading);
        verifyButton.setText(isLoading ? "Verifying..." : "Verify");

        // Disable input fields during loading
        for (EditText digit : otpDigits) {
            digit.setEnabled(!isLoading);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendTimer != null) {
            resendTimer.cancel();
        }
    }
}
