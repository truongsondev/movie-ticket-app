package com.example.movie_ticket_app.dto.auth.request;

public class RegisterRequest {
    private String userName;


    private String userEmail;


    private String userPassword;


    private String userMobile;


    private boolean userGender;


    private String userBirthday;

    public RegisterRequest(String userName, String userEmail, String userPassword, String userMobile, boolean userGender, String userBirthday) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userMobile = userMobile;
        this.userGender = userGender;
        this.userBirthday = userBirthday;
    }
}
