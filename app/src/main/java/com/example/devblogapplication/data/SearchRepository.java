package com.example.devblogapplication.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.ErrorResponse;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.response.SearchResponse;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private static final String TAG = "SearchRepository";
    private final ApiService api = NetworkClient.api();
    private static SearchRepository instance;
    private SearchRepository() {
        // Private constructor to enforce singleton pattern
    }
    public static SearchRepository getInstance() {
        if (instance == null) {
            instance = new SearchRepository();
        }
        return instance;
    }

    public LiveData<Resource<SearchResponse>> search(String keyword, String target) {
        MutableLiveData<Resource<SearchResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.search(keyword, target).enqueue(new Callback<ApiResponse<SearchResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<SearchResponse>> call, Response<ApiResponse<SearchResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: successful");
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchResponse>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<String>>> getSearchHistory() {
        MutableLiveData<Resource<List<String>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.getSearchHistory().enqueue(new Callback<ApiResponse<List<String>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<String>>> getSearchRecommendations(String kw) {
        MutableLiveData<Resource<List<String>>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.getSearchRecommendations(kw).enqueue(new Callback<ApiResponse<List<String>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }
}

