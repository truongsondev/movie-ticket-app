package com.example.movie_ticket_app.data.api;

import java.io.IOException;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AuthInterceptor implements Interceptor {
    private final String token;

    public AuthInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl url = original.url();

        // Kiểm tra nếu URL chứa /api/v1/show/
        if (url.encodedPath().contains("/api/v1/show/")) {
            Request newRequest = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        // Ngược lại thì gửi request bình thường
        return chain.proceed(original);
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor("your-access-token"))
            .build();

}
