package com.example.devblogapplication.network;

import androidx.annotation.Nullable;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.TagWithScore;
import com.example.devblogapplication.model.request.CreateNewPostRequest;
import com.example.devblogapplication.model.request.ShareExternalPostRequest;
import com.example.devblogapplication.model.response.SearchResponse;
import com.example.devblogapplication.model.response.TagDetailResponse;
import com.example.devblogapplication.room.TagInRoom;
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

    @GET("/api/users/{id}")
    Call<ApiResponse<UserInfoDTO>> getUser(@Path("id") String uid);

    @PUT("/api/users/{id}")
    Call<ApiResponse<UserInfoDTO>> updateProfile(@Body UpdateProfileRequest request, @Path("id") String uid);

    @PUT("/api/users/{id}/follow")
    Call<ApiResponse<Map<String, Boolean>>> followUser(@Path("id") String uid);

    @GET("/api/tags")
    Call<ApiResponse<List<Tag>>> getTags();

    @POST("/api/users/{id}/favorite-tags")
    Call<ApiResponse<List<Tag>>> updateFavoriteTags(@Body List<Tag> tag, @Path("id") String uid);

    @GET("/api/users/{id}/favorite-tags")
    Call<ApiResponse<List<Tag>>> getFavoriteTags(@Path("id") String uid);

    @GET("/api/tags/top")
    Call<ApiResponse<List<TagWithScore>>> getTopTags();

    @GET("/api/tags/{id}")
    Call<ApiResponse<TagDetailResponse>> getTagDetail(@Path("id") int id);



    /* Post API */
    @GET("/api/posts/for-you")
    Call<ApiResponse<List<PostDTO>>> getPostForYou(@Query("pageNumber") int pageNumber);

    @GET("/api/posts/following")
    Call<ApiResponse<List<PostDTO>>> getPostFollowing(@Query("pageNumber") int pageNumber);

    @GET("/api/posts/top")
    Call<ApiResponse<List<PostDTO>>> getTopPost(@Query("pageNumber") int pageNumber);

    @GET("/api/users/{id}/posts")
    Call<ApiResponse<List<PostDTO>>> getUserPosts(@Path("id") String userId);


    @POST("/api/posts/{postId}/like")
    Call<ApiResponse<Map<String, Boolean>>> likePost(@Path("postId") Long postId);

    @POST("/api/posts/{postId}/bookmark")
    Call<ApiResponse<Map<String, Boolean>>> bookmarkPost(@Path("postId") Long postId);

    @GET("/api/posts/{postId}/comment")
    Call<ApiResponse<List<PostCommentDTO>>> getComments(@Path("postId") Long postId);


    @POST("/api/posts/{postId}/comment")
    Call<ApiResponse<PostCommentDTO>> pushComments(@Path("postId") Long postId, @Body Map<String, String> comment);



    @GET("/api/posts/{postId}")
    Call<ApiResponse<PostDTO>>  getPostDetail(@Path("postId") Long postId);

    @POST("/api/posts/create")
    Call<ApiResponse<PostDTO>> createPost(@Body CreateNewPostRequest request);

    @POST("/api/posts/share")
    Call<ApiResponse<PostDTO>> sharePost(@Body ShareExternalPostRequest request);

    /* Search API */
    @GET("/api/search")
    Call<ApiResponse<SearchResponse>> search(@Query("keyword") String keyword,
                                                   @Query("target") String target);


    @GET("/api/search-history")
    Call<ApiResponse<List<String>>> getSearchHistory();

    @GET("/api/search-recommendations")
    Call<ApiResponse<List<String>>> getSearchRecommendations(@Query("keyword") String keyword);

}
