package com.example.movie_ticket_app.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;

public class OtpInputActivity extends AppCompatActivity {

    private EditText[] otpDigits = new EditText[6];
    private Button verifyButton;
    private TextView resendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_input_layout);

        // Initialize OTP digit fields
        otpDigits[0] = findViewById(R.id.otp_digit_1);
        otpDigits[1] = findViewById(R.id.otp_digit_2);
        otpDigits[2] = findViewById(R.id.otp_digit_3);
        otpDigits[3] = findViewById(R.id.otp_digit_4);
        otpDigits[4] = findViewById(R.id.otp_digit_5);
        otpDigits[5] = findViewById(R.id.otp_digit_6);

        verifyButton = findViewById(R.id.verifyButton);
        resendCode = findViewById(R.id.resendCode);

        setupOtpInputs();
        setupButtons();
    }

    private void setupOtpInputs() {
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

                        otpDigits[currentIndex + 1].requestFocus();
                    } else if (s.length() == 0 && currentIndex > 0) {
                        // Move focus back when deleting
                        otpDigits[currentIndex - 1].requestFocus();
                    }

                    // Check if all digits are filled
                    checkVerificationComplete();
                }
            });
        }
    }

    private void setupButtons() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = getEnteredOtp();
                // Implement your verification logic here
                Toast.makeText(OtpInputActivity.this, "Verifying code: " + otp, Toast.LENGTH_SHORT).show();
            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement resend code logic here
                Toast.makeText(OtpInputActivity.this, "Resending code...", Toast.LENGTH_SHORT).show();
                clearOtpFields();
            }
        });
    }

    private String getEnteredOtp() {
        StringBuilder otp = new StringBuilder();
        for (EditText digit : otpDigits) {
            otp.append(digit.getText().toString());
        }
        return otp.toString();
    }

    private void checkVerificationComplete() {
        boolean isComplete = true;
        for (EditText digit : otpDigits) {
            if (digit.getText().toString().isEmpty()) {
                isComplete = false;
                break;
            }
        }
        verifyButton.setEnabled(isComplete);
    }

    private void clearOtpFields() {
        for (EditText digit : otpDigits) {
            digit.setText("");
        }
        otpDigits[0].requestFocus();
    }
}

