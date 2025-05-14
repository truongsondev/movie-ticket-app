package com.example.movie_ticket_app.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movie_ticket_app.R;
import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.config.ApiClient;
import com.example.movie_ticket_app.data.api.ApiServices;
import com.example.movie_ticket_app.dto.auth.request.RegisterRequest;
import com.example.movie_ticket_app.dto.auth.response.RegisterResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout usernameLayout, emailLayout, passwordLayout, mobileLayout, birthdayLayout;
    private TextInputEditText editTextUsername, editTextEmail, editTextPassword, editTextMobile, editTextBirthday;
    private RadioGroup radioGroupGender;
    private TextView textViewGenderError, textViewLogin;
    private Button buttonRegister;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        initViews();

        // Setup listeners
        setupListeners();

        // Setup date picker
        setupDatePicker();
    }

    private void initViews() {
        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        mobileLayout = findViewById(R.id.mobileLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextBirthday = findViewById(R.id.editTextBirthday);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        textViewGenderError = findViewById(R.id.textViewGenderError);
        textViewLogin = findViewById(R.id.textViewLogin);

        buttonRegister = findViewById(R.id.buttonRegister);

        calendar = Calendar.getInstance();
    }

    private void setupListeners() {
        // Text change listeners for real-time validation
        editTextUsername.addTextChangedListener(new ValidationTextWatcher(editTextUsername));
        editTextEmail.addTextChangedListener(new ValidationTextWatcher(editTextEmail));
        editTextPassword.addTextChangedListener(new ValidationTextWatcher(editTextPassword));
        editTextMobile.addTextChangedListener(new ValidationTextWatcher(editTextMobile));

        // Gender selection listener
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                textViewGenderError.setVisibility(View.GONE);
            }
        });

        // Register button click listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    // All inputs are valid, proceed with registration
                    registerUser();
                }
            }
        });

        // Login text click listener
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login screen
                finish();
            }
        });
    }

    private void setupDatePicker() {
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthdayField();
            }
        };

        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateBirthdayField() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editTextBirthday.setText(sdf.format(calendar.getTime()));
        validateBirthday();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (!validateUsername()) isValid = false;
        if (!validateEmail()) isValid = false;
        if (!validatePassword()) isValid = false;
        if (!validateMobile()) isValid = false;
        if (!validateGender()) isValid = false;
        if (!validateBirthday()) isValid = false;

        return isValid;
    }

    private boolean validateUsername() {
        String username = editTextUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username is required");
            return false;
        } else {
            usernameLayout.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format");
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters length");
            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }
    }

    private boolean validateMobile() {
        String mobile = editTextMobile.getText().toString().trim();
        Pattern pattern = Pattern.compile("^[0-9]{10}$");

        if (TextUtils.isEmpty(mobile)) {
            mobileLayout.setError("Mobile number is required");
            return false;
        } else if (!pattern.matcher(mobile).matches()) {
            mobileLayout.setError("Mobile number must be 10 digits");
            return false;
        } else {
            mobileLayout.setError(null);
            return true;
        }
    }

    private boolean validateGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();

        if (selectedId == -1) {
            textViewGenderError.setVisibility(View.VISIBLE);
            return false;
        } else {
            textViewGenderError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateBirthday() {
        String birthday = editTextBirthday.getText().toString().trim();
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

        if (TextUtils.isEmpty(birthday)) {
            birthdayLayout.setError("Birthday is required");
            return false;
        } else if (!pattern.matcher(birthday).matches()) {
            birthdayLayout.setError("Birthday must be in yyyy-MM-dd format");
            return false;
        } else {
            birthdayLayout.setError(null);
            return true;
        }
    }

    private void registerUser() {
        // Get all the validated input values
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        boolean gender = radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale;
        String birthday = editTextBirthday.getText().toString().trim();

        // Create user registration model
        RegisterRequest user = new RegisterRequest(
                username, email, password, mobile, gender, birthday);

        // TODO: Send registration data to your API
        ApiServices apiServices = ApiClient.getRetrofitInstance().create(ApiServices.class);
        Call<APIResponse<RegisterResponse>> call = apiServices.register(user);
        call.enqueue(new Callback<APIResponse<RegisterResponse>>() {
            @Override
            public void onResponse(Call<APIResponse<RegisterResponse>> call, Response<APIResponse<RegisterResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse data = response.body().getMetadata();
                    String email = data.getEmail();
                    String token = data.getToken();

                    Intent intent = new Intent(RegisterActivity.this, OtpInputActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<RegisterResponse>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // For now, just show a success message
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
    }

    // Text watcher for validation
    private class ValidationTextWatcher implements TextWatcher {
        private View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int id = view.getId();

            if (id == R.id.editTextUsername) {
                validateUsername();
            } else if (id == R.id.editTextEmail) {
                validateEmail();
            } else if (id == R.id.editTextPassword) {
                validatePassword();
            } else if (id == R.id.editTextMobile) {
                validateMobile();
            }
        }

    }
}
