package com.example.movie_ticket_app.dto.auth.response;

public class RegisterReponse {
    private String email;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "RegisterReponse{email='" + email + "', token='" + token + "'}";
    }
}
