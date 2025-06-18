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
import java.util.Map;

import lombok.Getter;

public class ProfileViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private final MediatorLiveData<Resource<UserInfoDTO>> userLiveData = new MediatorLiveData<>();
    public LiveData<Resource<UserInfoDTO>> user = userLiveData;

    private final MutableLiveData<Boolean> _editable = new MutableLiveData<>(false);
    public final LiveData<Boolean> editable = _editable;

    private final MediatorLiveData<Resource<Map<String, Boolean>>> _followUser = new MediatorLiveData<>();
    public LiveData<Resource<Map<String, Boolean>>> followUser = _followUser;

    private final String TAG = "ProfileViewModel";

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
        userRepository = new UserRepository(application);
    }
    @Getter
    private String userId;


    public void getUser(String uuid) {
        String currentUserId = userRepository.getCurrentUser().getId();
        if (uuid == null || uuid.isEmpty()) {
            uuid = currentUserId;
            _editable.setValue(true);
        } else if (uuid.equals(currentUserId)){
            _editable.setValue(true);
        } else {
            _editable.setValue(false);
        }
        userId = uuid;
        Log.d(TAG, "getUser: " + uuid);

        LiveData<Resource<UserInfoDTO>> userSource = userRepository.getUser(uuid);

        userLiveData.addSource(userSource, result -> {
            Log.d(TAG, "Observer triggered with status: " + result.status);
            userLiveData.setValue(result);
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                Log.d(TAG, "Resource success -> set value " + result.data.getId());

            } else if (result.status == Resource.Status.ERROR) {
                Log.e(TAG, "Resource error: " + result.message);
            }
            if (result.status != Resource.Status.LOADING) {
                userLiveData.removeSource(userSource);
                Log.d(TAG, "User data loaded: " + result.data.getUsername());
            }
        });
    }

    public void followUser() {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "followUser: userId is null or empty");
            return;
        }

        Log.d(TAG, "followUser: " + userId);

        LiveData<Resource<Map<String, Boolean>>> source = userRepository.followUser(userId);

        _followUser.addSource(source, result -> {
            _followUser.setValue(result);

            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                Log.d(TAG, "Follow operation successful");
            } else if (result.status == Resource.Status.ERROR) {
                Log.e(TAG, "Follow user error: " + result.message);
            }

            if (result.status != Resource.Status.LOADING) {
                _followUser.removeSource(source);
            }
        });
    }
    public void updateProfile() {
        Resource<UserInfoDTO> currentResource = userLiveData.getValue();
        if (currentResource != null && currentResource.data != null) {
            UserInfoDTO oldUser = currentResource.data;
            boolean newFollowingStatus = _followUser.getValue().data.getOrDefault("following", false);
            Log.d(TAG, "Updating following status to: " + newFollowingStatus);
            UserInfoDTO updatedUser = updateUset(oldUser, newFollowingStatus);
            userLiveData.setValue(Resource.success(updatedUser));
        }
    }

    private @NonNull UserInfoDTO updateUset(UserInfoDTO oldUser, boolean newFollowingStatus) {
        int newFollowerCount = oldUser.getFollowers();
        if (newFollowingStatus && !oldUser.getIsFollowing()) {
            // User just followed, increment follower count
            newFollowerCount++;
        } else if (!newFollowingStatus && oldUser.getIsFollowing()) {
            // User just unfollowed, decrement follower count
            newFollowerCount = Math.max(0, newFollowerCount - 1);
        }

        return new UserInfoDTO(
                oldUser.getId(),
                oldUser.getEmail(),
                oldUser.getFullname(),
                oldUser.getUsername(),
                oldUser.getAvatarLink(),
                oldUser.getReadme(),
                oldUser.getRegistrationAt(),
                oldUser.getFavoriteTags(),
                newFollowerCount,
                oldUser.getFollowing(),
                oldUser.getPosts(),
                newFollowingStatus
        );
    }

}
