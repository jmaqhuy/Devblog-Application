package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.room.User;

public class HomeViewModel extends AndroidViewModel {
    private final UserRepository userRepo;

    public LiveData<User> user;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        user = userRepo.getLiveCurrentUser();
    }
}
