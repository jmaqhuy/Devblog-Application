package com.example.devblogapplication.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.ErrorResponse;
import com.example.devblogapplication.model.request.LoginRequest;
import com.example.devblogapplication.model.response.LoginResponse;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;
import com.example.devblogapplication.model.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final String TAG = "AuthRepository";
    private final ApiService api = NetworkClient.api();

    public LiveData<Resource<LoginResponse>> login(String email, String password) {
        MutableLiveData<Resource<LoginResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        api.login(new LoginRequest(email, password)).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("Something went wrong");
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    errorMessage.delete(0, errorMessage.length());
                    errorMessage.append(errorResponse.getMessage());

                    liveData.postValue(Resource.error(errorMessage.toString()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable throwable) {
                liveData.postValue(Resource.error(throwable.getMessage() != null
                        ? throwable.getMessage()
                        : "Network error"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<LoginResponse>> register(String email, String password) {
        MutableLiveData<Resource<LoginResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());
        api.register(new LoginRequest(email, password)).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                Log.d(TAG, "Register Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("Something went wrong");
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    errorMessage.delete(0, errorMessage.length());
                    if (errorResponse.getDetails() != null) {
                        if (errorResponse.getDetails().containsKey("email")) {
                            errorMessage.append(errorResponse.getDetails().get("email"));
                        } else if (errorResponse.getDetails().containsKey("password")) {
                            errorMessage.append(errorResponse.getDetails().get("password"));
                        }
                    } else {
                        errorMessage.append(errorResponse.getMessage());
                    }

                    Log.e("Register Error", errorMessage.toString());
                    liveData.postValue(Resource.error(errorMessage.toString()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable throwable) {
                liveData.postValue(Resource.error(throwable.getMessage() != null
                        ? throwable.getMessage()
                        : "Network error"));
            }
        });
        return liveData;
    }

    public LiveData<Resource<LoginResponse>> introspect() {
        MutableLiveData<Resource<LoginResponse>> live = new MutableLiveData<>();
        live.postValue(Resource.loading());
        api.introspect().enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                Log.d(TAG, "Introspect Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    live.postValue(Resource.success(response.body().getData()));
                } else {
                    live.postValue(Resource.error("Something went wrong"));
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    Log.e("Introspect Error", errorResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                live.postValue(Resource.error("Something went wrong"));
            }
        });

        return live;
    }
}
