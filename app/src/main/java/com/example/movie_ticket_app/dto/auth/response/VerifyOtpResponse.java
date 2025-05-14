package com.example.movie_ticket_app.dto.auth.response;



public class VerifyOtpResponse {
    private boolean verified;

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}