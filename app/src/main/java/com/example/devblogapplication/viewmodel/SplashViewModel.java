package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.devblogapplication.data.AuthRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.UserInfoDTO;
import com.example.devblogapplication.model.response.LoginResponse;

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
        userRepo.insertUser(userInfoDTO);
    }

    public void checkSession() {
        LiveData<Resource<LoginResponse>> source = repo.introspect();
        _sessionValid.addSource(source, _sessionValid::setValue);

    }




}

