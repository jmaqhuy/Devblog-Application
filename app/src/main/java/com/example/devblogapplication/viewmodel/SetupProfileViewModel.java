package com.example.devblogapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.devblogapplication.data.ImageRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.UserInfoDTO;
import com.example.devblogapplication.model.request.UpdateProfileRequest;

import okhttp3.MultipartBody;

public class SetupProfileViewModel extends ViewModel {
    private final ImageRepository imageRepo = new ImageRepository();
    private final UserRepository userRepo = new UserRepository();

    private final MediatorLiveData<Resource<String>> _imageUploadStatus = new MediatorLiveData<>();
    public LiveData<Resource<String>> imageUploadStatus = _imageUploadStatus;


    public MutableLiveData<String> email = new MutableLiveData<>(),
            name = new MutableLiveData<>(),
            username = new MutableLiveData<>();

    private MutableLiveData<Boolean> _uploading_image = new MutableLiveData<>(false);
    public LiveData<Boolean> uploading_image = _uploading_image;

    private final MediatorLiveData<Resource<UserInfoDTO>> _updateProfileStatus = new MediatorLiveData<>();
    public LiveData<Resource<UserInfoDTO>> updateProfileStatus = _updateProfileStatus;


    private final MutableLiveData<String> _error = new MutableLiveData<>("");
    public LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    public LiveData<Boolean> loading = _loading;

    public void updateProfile() {
        if (_loading.getValue()) {
            return;
        }

        if (_imageUploadStatus.getValue() == null || _imageUploadStatus.getValue().data == null) {
            _error.postValue("Please set your avatar");
            return;
        } else if (email.getValue() == null || email.getValue().isEmpty()) {
            _error.postValue("Email is required");
            return;
        } else if (name.getValue() == null || name.getValue().isEmpty()) {
            _error.postValue("Name is required");
            return;
        } else if (username.getValue() == null || username.getValue().isEmpty()) {
            _error.postValue("Username is required");
            return;
        }

        _loading.setValue(true);
        _error.setValue("");

        LiveData<Resource<UserInfoDTO>> source = userRepo.updateProfile(
                new UpdateProfileRequest(email.getValue(),
                        username.getValue(),
                        name.getValue(),
                        _imageUploadStatus.getValue().data));
        _updateProfileStatus.addSource(source, result -> {
            _updateProfileStatus.setValue(result);
            switch (result.status) {
                case LOADING:
                    _loading.setValue(true);
                    break;
                case SUCCESS:
                    _loading.setValue(false);
                    break;
                case ERROR:
                    _loading.setValue(false);
                    _error.setValue(result.message);
                    break;
            }
            if (result.status != Resource.Status.LOADING) {
                _updateProfileStatus.removeSource(source);
            }
        });

    }


    public void uploadImage(MultipartBody.Part image) {
        _uploading_image.setValue(true);
        LiveData<Resource<String>> source = imageRepo.uploadImage(image);

        _imageUploadStatus.addSource(source, result -> {
            _imageUploadStatus.setValue(result);
            if (result.status != Resource.Status.LOADING) {
                _imageUploadStatus.removeSource(source);
            }
        });
    }

    public void setUploadingStatus(boolean status) {
        _uploading_image.setValue(status);
    }

}
