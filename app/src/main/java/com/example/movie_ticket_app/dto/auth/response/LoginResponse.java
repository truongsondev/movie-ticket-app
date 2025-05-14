package com.example.movie_ticket_app.dto.auth.response;


import java.time.LocalDate;
import java.util.List;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO profile;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserDTO getProfile() {
        return profile;
    }

    public void setProfile(UserDTO profile) {
        this.profile = profile;
    }

    public static class UserDTO {
        private String userEmail;
        private String userName;
        private String userMobile;
        private boolean userGender;
        private List<Integer> userBirthday;

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
        }

        public boolean isUserGender() {
            return userGender;
        }

        public void setUserGender(boolean userGender) {
            this.userGender = userGender;
        }

        public List<Integer> getUserBirthday() {
            return userBirthday;
        }

        public void setUserBirthday(List<Integer> userBirthday) {
            this.userBirthday = userBirthday;
        }
    }
}
