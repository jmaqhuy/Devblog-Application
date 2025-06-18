package com.example.devblogapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.devblogapplication.data.AuthRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.UserInfoDTO;
import com.example.devblogapplication.model.response.LoginResponse;
import com.example.devblogapplication.room.User;

public class SplashViewModel extends AndroidViewModel {
    private final AuthRepository repo = new AuthRepository();
    private final UserRepository userRepo;
    private final MediatorLiveData<Resource<LoginResponse>> _sessionValid = new MediatorLiveData<>();
    public LiveData<Resource<LoginResponse>> sessionValid = _sessionValid;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
    }

    public void deleteAllData(){
        userRepo.deleteAllData();
    }

    public void insertUser(UserInfoDTO userInfoDTO){
        Log.d("SplashViewModel", "insertUser: from splash screen, user id: " + userInfoDTO.getId());
        userRepo.insertUser(userInfoDTO);

        userRepo.getLiveCurrentUser().observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null && user.getId().equals(userInfoDTO.getId())) {
                    Log.d("SplashViewModel", "check save, user id: " + user.getId());
                    userRepo.getLiveCurrentUser().removeObserver(this);
                }
            }
        });
    }

    public void checkSession() {
        LiveData<Resource<LoginResponse>> source = repo.introspect();
        _sessionValid.addSource(source, _sessionValid::setValue);
    }




}

