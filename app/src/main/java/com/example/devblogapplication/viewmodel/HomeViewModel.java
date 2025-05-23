package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.room.User;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final UserRepository userRepo;

    public LiveData<User> user;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        user = userRepo.getUser();
    }
}
