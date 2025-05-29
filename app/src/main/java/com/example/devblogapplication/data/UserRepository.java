package com.example.devblogapplication.data;

import android.content.Context;
import android.util.Log;

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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService apiService = NetworkClient.api();
    private final UserDAO userDAO;
    private final String TAG = "UserRepository";

    public UserRepository(Context appContext) {
        userDAO = DevblogDatabase.getInstance(appContext).userDAO();
    }

    public LiveData<Resource<UserInfoDTO>> updateProfile(UpdateProfileRequest request) {
        MutableLiveData<Resource<UserInfoDTO>> liveData = new MutableLiveData<>();
        apiService.updateProfile(request, userDAO.getCurrentUser().getId()).enqueue(new Callback<ApiResponse<UserInfoDTO>>() {
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
        Log.d(TAG, "deleteAllData: UserInfo has been deleted");
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
                        .posts(user.getPosts())
                        .build()
        );
    }

    public LiveData<User> getLiveCurrentUser(){
        return userDAO.observeCurrentUser();
    }

    public User getCurrentUser(){
        return userDAO.getCurrentUser();
    }

    public LiveData<Resource<UserInfoDTO>> getUser(String userId){
        Log.d(TAG, "get User, id: " + userId);
        MutableLiveData<Resource<UserInfoDTO>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading());

        apiService.getUser(userId).enqueue(new Callback<ApiResponse<UserInfoDTO>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserInfoDTO>> call, Response<ApiResponse<UserInfoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.d(TAG, "Live data post success value ");
                    liveData.postValue(Resource.success(response.body().getData()));
                    if (Objects.equals(userDAO.getCurrentUser().getId(), response.body().getData().getId())){
                        Log.d(TAG, "get current user -> save to db ");
                        insertUser(response.body().getData());
                    }
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
}
