package com.example.devblogapplication.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRepository {
    private final ApiService apiService = NetworkClient.api();

    public LiveData<Resource<String>> uploadImage(MultipartBody.Part image) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        apiService.uploadImage(image).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ImageRepository", "Image uploaded successfully: https://jmaqhuy.id.vn/images/" + response.body().get("imageName"));
                    liveData.postValue(Resource.success(response.body().get("imageName")));
                } else {
                    liveData.postValue(Resource.error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }
}
