package com.example.movie_ticket_app.data.api;

import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.dto.auth.request.RegisterRequest;
import com.example.movie_ticket_app.dto.auth.request.VerifyOtpRequest;
import com.example.movie_ticket_app.dto.auth.response.RegisterReponse;
import com.example.movie_ticket_app.dto.auth.response.VerifyOtpReponse;
import com.example.movie_ticket_app.dto.movie.response.ListMovieHomeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {
    @POST("auth/register")
    Call<APIResponse<RegisterReponse>> register(@Body RegisterRequest request);

    @POST("auth/verify-otp")
    Call<APIResponse<VerifyOtpReponse>> verifyOtp(@Body VerifyOtpRequest otpRequest);

    @GET("movie/home")
    Call<APIResponse<ListMovieHomeResponse>> getAllMovie();
}
