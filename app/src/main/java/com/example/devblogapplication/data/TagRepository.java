package com.example.devblogapplication.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.TagWithScore;
import com.example.devblogapplication.model.response.TagDetailResponse;
import com.example.devblogapplication.room.DevblogDatabase;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;
import com.example.devblogapplication.room.UserDAO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagRepository {
    private final ApiService api = NetworkClient.api();
    private UserDAO userDAO;

    public TagRepository(Context context) {
        userDAO = DevblogDatabase.getInstance(context).userDAO();
    }

    public LiveData<Resource<List<Tag>>> getAllTags(){
        MutableLiveData<Resource<List<Tag>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.getTags().enqueue(new Callback<ApiResponse<List<Tag>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Tag>>> call, Response<ApiResponse<List<Tag>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {

                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Tag>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }


    public LiveData<Resource> updateFavoriteTags(List<Tag> tags){
        MutableLiveData<Resource> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.updateFavoriteTags(tags, userDAO.getCurrentUser().getId()).enqueue(new Callback<ApiResponse<List<Tag>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Tag>>> call, Response<ApiResponse<List<Tag>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveData.postValue(Resource.success(null));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Tag>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<Tag>>> getFavoriteTags(String userId){
        MutableLiveData<Resource<List<Tag>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.getFavoriteTags(userId).enqueue(new Callback<ApiResponse<List<Tag>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Tag>>> call, Response<ApiResponse<List<Tag>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Tag>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<TagWithScore>>> getTopTags(){
        MutableLiveData<Resource<List<TagWithScore>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.getTopTags().enqueue(new Callback<ApiResponse<List<TagWithScore>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TagWithScore>>> call, Response<ApiResponse<List<TagWithScore>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TagWithScore>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<TagDetailResponse>> getTagDetail(int id){
        MutableLiveData<Resource<TagDetailResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.getTagDetail(id).enqueue(new Callback<ApiResponse<TagDetailResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<TagDetailResponse>> call, Response<ApiResponse<TagDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<TagDetailResponse>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public void toggleFavorite(int id, boolean b) {
        
    }
}
