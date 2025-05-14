package com.example.movie_ticket_app.utils;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.example.movie_ticket_app.dto.auth.response.LoginResponse.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SessionManager {
    private static final String PREF_NAME = "MovieTicketAppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_MOBILE = "userMobile";
    private static final String KEY_USER_GENDER = "userGender";
    private static final String KEY_USER_BIRTHDAY = "userBirthday";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final Gson gson = new Gson();
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String accessToken, String refreshToken, UserDTO profile) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);

        if (profile != null) {
            editor.putString(KEY_USER_EMAIL, profile.getUserEmail());
            editor.putString(KEY_USER_NAME, profile.getUserName());
            editor.putString(KEY_USER_MOBILE, profile.getUserMobile());
            editor.putBoolean(KEY_USER_GENDER, profile.isUserGender());
//            if (profile.getUserBirthday() != null) {
//                editor.putString(KEY_USER_BIRTHDAY, profile.getUserBirthday().format(DATE_FORMATTER));
//            }
        }

        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getAccessToken() {
        return pref.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return pref.getString(KEY_REFRESH_TOKEN, null);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }

    public String getUserMobile() {
        return pref.getString(KEY_USER_MOBILE, null);
    }

    public boolean getUserGender() {
        return pref.getBoolean(KEY_USER_GENDER, false);
    }

    public List<Integer> getUserBirthday() {
        String birthdayStr = pref.getString(KEY_USER_BIRTHDAY, null);
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            try {
                // Chuyển đổi chuỗi JSON thành List<Integer> (năm, tháng, ngày)
                Type listType = new TypeToken<List<Integer>>() {}.getType();
                List<Integer> birthdayList = gson.fromJson(birthdayStr, listType);

                return birthdayList;  // Trả về List<Integer> thay vì LocalDate
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }




    public UserDTO getUserProfile() {
        UserDTO profile = new UserDTO();
        profile.setUserEmail(getUserEmail());
        profile.setUserName(getUserName());
        profile.setUserMobile(getUserMobile());
        profile.setUserGender(getUserGender());
        return profile;
    }



    public void updateAccessToken(String newAccessToken) {
        editor.putString(KEY_ACCESS_TOKEN, newAccessToken);
        editor.commit();
    }

    public void updateRefreshToken(String newRefreshToken) {
        editor.putString(KEY_REFRESH_TOKEN, newRefreshToken);
        editor.commit();
    }

    public void updateUserProfile(UserDTO profile) {
        if (profile != null) {
            editor.putString(KEY_USER_EMAIL, profile.getUserEmail());
            editor.putString(KEY_USER_NAME, profile.getUserName());
            editor.putString(KEY_USER_MOBILE, profile.getUserMobile());
            editor.putBoolean(KEY_USER_GENDER, profile.isUserGender());


            editor.commit();
        }
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
