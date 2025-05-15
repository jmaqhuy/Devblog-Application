package com.example.devblogapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagRepository {
    private final ApiService api = NetworkClient.api();

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

        api.updateFavoriteTags(tags).enqueue(new Callback<ApiResponse<List<Tag>>>() {
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
}
