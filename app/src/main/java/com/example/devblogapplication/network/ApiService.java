package com.example.devblogapplication.network;

import androidx.annotation.Nullable;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.request.CreateNewPostRequest;
import com.example.devblogapplication.model.request.ShareExternalPostRequest;
import com.example.devblogapplication.room.Tag;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    /* Auth API */
    @POST("/api/introspect")
    Call<ApiResponse<LoginResponse>> introspect();

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

    @POST("/api/users/me/favorite-tags")
    Call<ApiResponse<List<Tag>>> updateFavoriteTags(@Body List<Tag> tags);



    /* Post API */
    @GET("/api/posts/for-you")
    Call<ApiResponse<List<PostDTO>>> getPostForYou(@Query("pageNumber") int pageNumber);

    @POST("/api/posts/{postId}/like")
    Call<ApiResponse<Map<String, Boolean>>> likePost(@Path("postId") Long postId);

    @GET("/api/posts/{postId}/comment")
    Call<ApiResponse<List<PostCommentDTO>>> getComments(@Path("postId") Long postId, @Query("parentId") @Nullable String parentId);


    @POST("/api/posts/{postId}/comment")
    Call<ApiResponse<PostCommentDTO>> pushComments(@Path("postId") Long postId, @Body Map<String, String> comment);



    @GET("/api/posts/{postId}")
    Call<ApiResponse<PostDTO>>  getPostDetail(@Path("postId") Long postId);

    @POST("/api/posts/create")
    Call<ApiResponse<PostDTO>> createPost(@Body CreateNewPostRequest request);

    @POST("/api/posts/share")
    Call<ApiResponse<PostDTO>> sharePost(@Body ShareExternalPostRequest request);


}
