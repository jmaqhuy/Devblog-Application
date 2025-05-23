package com.example.devblogapplication.data;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.ErrorResponse;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.request.CreateNewPostRequest;
import com.example.devblogapplication.model.request.ShareExternalPostRequest;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private final ApiService api = NetworkClient.api();
    private int pageNumber = 0;

    public LiveData<Resource<List<PostDTO>>> getPostForYou() {
        MutableLiveData<Resource<List<PostDTO>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.getPostForYou(pageNumber).enqueue(new Callback<ApiResponse<List<PostDTO>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<PostDTO>>> call, Response<ApiResponse<List<PostDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Post Repository", "get post success, length: " + response.body().getData().size());
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PostDTO>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        pageNumber++;

        return liveData;
    }

    public LiveData<Resource<PostDTO>> getPostDetail(Long postId) {
        MutableLiveData<Resource<PostDTO>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.getPostDetail(postId).enqueue(new Callback<ApiResponse<PostDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostDTO>> call, Response<ApiResponse<PostDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PostDTO>> call, Throwable throwable) {

            }
        });

        return liveData;
    }

    public LiveData<Resource<Boolean>> likePost(Long postId) {
        MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.likePost(postId).enqueue(new Callback<ApiResponse<Map<String, Boolean>>>() {

            @Override
            public void onResponse(Call<ApiResponse<Map<String, Boolean>>> call, Response<ApiResponse<Map<String, Boolean>>> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Boolean>>> call, Throwable throwable) {

            }
        });
        return liveData;
    }

    public LiveData<Resource<List<PostCommentDTO>>> getComments(Long postId, @Nullable String parentId) {
        MutableLiveData<Resource<List<PostCommentDTO>>> liveData = new MutableLiveData<>();

        api.getComments(postId, parentId).enqueue(new Callback<ApiResponse<List<PostCommentDTO>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PostCommentDTO>>> call, Response<ApiResponse<List<PostCommentDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PostCommentDTO>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });

        return liveData;
    }

    public LiveData<Resource<PostCommentDTO>> pushComments(Long postId, Map<String, String> comment) {
        MutableLiveData<Resource<PostCommentDTO>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.pushComments(postId, comment).enqueue(new Callback<ApiResponse<PostCommentDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostCommentDTO>> call, Response<ApiResponse<PostCommentDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PostCommentDTO>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });

        return liveData;
    }

    public LiveData<Resource<PostDTO>> createPost(CreateNewPostRequest request) {
        MutableLiveData<Resource<PostDTO>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.createPost(request).enqueue(new Callback<ApiResponse<PostDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostDTO>> call, Response<ApiResponse<PostDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PostDTO>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<PostDTO>> sharePost(ShareExternalPostRequest request){
        MutableLiveData<Resource<PostDTO>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.sharePost(request).enqueue(new Callback<ApiResponse<PostDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostDTO>> call, Response<ApiResponse<PostDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PostDTO>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });

        return liveData;
    }
}
