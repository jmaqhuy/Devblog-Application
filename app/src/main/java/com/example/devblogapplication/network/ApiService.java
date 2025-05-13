package com.example.devblogapplication.network;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.UserInfoDTO;
import com.example.devblogapplication.model.request.LoginRequest;
import com.example.devblogapplication.model.request.UpdateProfileRequest;
import com.example.devblogapplication.model.response.LoginResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {
    /* Auth API */
    @POST("/api/introspect")
    Call<Boolean> introspect();

    @POST("/api/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);


    @POST("/api/register")
    Call<ApiResponse<LoginResponse>> register(@Body LoginRequest request);

    /* Profile API */
    @Multipart
    @POST("/api/images")
    Call<Map<String, String>> uploadImage(@Part MultipartBody.Part image);

    @PUT("/api/users/me")
    Call<ApiResponse<UserInfoDTO>> updateProfile(@Body UpdateProfileRequest request);

    @GET("/api/tags")
    Call<ApiResponse<List<Tag>>> getTags();
}
