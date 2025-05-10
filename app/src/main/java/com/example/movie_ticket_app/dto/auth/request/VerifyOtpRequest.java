package com.example.movie_ticket_app.dto.auth.request;

public class VerifyOtpRequest {
    private String email;
    private String token;
    private String otp;

    public VerifyOtpRequest(String email, String token, String otp) {
        this.email = email;
        this.token = token;
        this.otp = otp;
    }
}
