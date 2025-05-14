package com.example.movie_ticket_app.data.api;

import com.example.movie_ticket_app.common.APIResponse;
import com.example.movie_ticket_app.dto.auth.request.LoginRequest;
import com.example.movie_ticket_app.dto.auth.request.RegisterRequest;
import com.example.movie_ticket_app.dto.auth.request.VerifyOtpRequest;
import com.example.movie_ticket_app.dto.auth.response.LoginResponse;
import com.example.movie_ticket_app.dto.auth.response.RegisterResponse;

import com.example.movie_ticket_app.dto.auth.response.VerifyOtpResponse;
import com.example.movie_ticket_app.dto.movie.response.GetShowResponse;
import com.example.movie_ticket_app.dto.movie.response.ListMovieHomeResponse;
import com.example.movie_ticket_app.dto.movie.response.MovieDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {
    @POST("auth/register")
    Call<APIResponse<RegisterResponse>> register(@Body RegisterRequest request);
    @POST("auth/verify-otp")
    Call<APIResponse<VerifyOtpResponse>> verifyOtp(@Body VerifyOtpRequest otpRequest);
    @GET("movie/home")
    Call<APIResponse<ListMovieHomeResponse>> getAllMovie();
    @GET("movie/{id}")
    Call<APIResponse<MovieDetailResponse>> getDetailMovie(@Path("id") int movieId);

    @POST("auth/login")
    Call<APIResponse<LoginResponse>> login(@Body LoginRequest request);

    @GET("show/{id}")
    Call<APIResponse<GetShowResponse>> getShow(@Path("id") int id);
}
