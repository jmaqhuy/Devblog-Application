package com.example.devblogapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.TagRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.UserInfoDTO;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private final MediatorLiveData<Resource<UserInfoDTO>> userLiveData = new MediatorLiveData<>();
    public LiveData<Resource<UserInfoDTO>> user = userLiveData;
    public LiveData<Resource<List<Tag>>> favoriteTags = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
        userRepository = new UserRepository(application);
    }

    public String getUserIdFromDB(){
        return userRepository.getCurrentUser().getId();
    }


    public void getUser(String uuid) {
        Log.d("ProfileViewModel", "getUser: " + uuid);

        LiveData<Resource<UserInfoDTO>> userSource = userRepository.getUser(uuid);

        userLiveData.addSource(userSource, result -> {
            Log.d("ProfileViewModel", "Observer triggered with status: " + result.status);
            userLiveData.setValue(result);

            if (result.status != Resource.Status.LOADING) {
                userLiveData.removeSource(userSource);
            }

            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                Log.d("ProfileViewModel", "Resource success -> set value " + result.data.getId());

                favoriteTags = tagRepository.getFavoriteTags(uuid);
            } else if (result.status == Resource.Status.ERROR) {
                Log.e("ProfileViewModel", "Resource error: " + result.message);
            }
        });
    }
}
