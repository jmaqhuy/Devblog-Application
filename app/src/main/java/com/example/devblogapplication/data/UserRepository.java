package com.example.devblogapplication.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.model.ApiResponse;
import com.example.devblogapplication.model.ErrorResponse;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.UserInfoDTO;
import com.example.devblogapplication.model.request.UpdateProfileRequest;
import com.example.devblogapplication.network.ApiService;
import com.example.devblogapplication.network.NetworkClient;
import com.example.devblogapplication.room.DevblogDatabase;
import com.example.devblogapplication.room.User;
import com.example.devblogapplication.room.UserDAO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService apiService = NetworkClient.api();
    private final UserDAO userDAO;

    public UserRepository(Context appContext) {
        userDAO = DevblogDatabase.getInstance(appContext).userDAO();
    }

    public LiveData<Resource<UserInfoDTO>> updateProfile(UpdateProfileRequest request) {
        MutableLiveData<Resource<UserInfoDTO>> liveData = new MutableLiveData<>();
        apiService.updateProfile(request).enqueue(new Callback<ApiResponse<UserInfoDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserInfoDTO>> call, Response<ApiResponse<UserInfoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body().getData()));
                } else {
                    ErrorResponse errorResponse = NetworkClient.parseError(response.errorBody());
                    liveData.postValue(Resource.error(errorResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfoDTO>> call, Throwable throwable) {
                liveData.postValue(Resource.error("Something went wrong"));
            }
        });
        return liveData;
    }

    public void deleteAllData(){
        userDAO.deleteAllUsers();
    }

    public void insertUser(UserInfoDTO user){
        userDAO.deleteAllUsers();
        userDAO.insertUser(
                User.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullname(user.getFullname())
                        .username(user.getUsername())
                        .avatarLink(user.getAvatarLink())
                        .readme(user.getReadme())
                        .registrationAt(user.getRegistrationAt())
                        .followers(user.getFollowers())
                        .following(user.getFollowing())
                        .build()
        );
    }

    public LiveData<User> getUser(){
        return userDAO.observeCurrentUser();
    }
}
