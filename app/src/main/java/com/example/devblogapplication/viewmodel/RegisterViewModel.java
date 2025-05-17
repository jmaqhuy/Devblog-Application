package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.AuthRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.response.LoginResponse;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.utils.ValidEmail;

public class RegisterViewModel extends AndroidViewModel {
    private final AuthRepository repo = new AuthRepository();
    private final UserRepository userRepo;
    public RegisterViewModel(Application application) {
        super(application);
        userRepo = new UserRepository(application);
    }

    public final MutableLiveData<String> email = new MutableLiveData<>(),
            password = new MutableLiveData<>(),
            retypePassword = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>("");
    private final MediatorLiveData<Resource<LoginResponse>> _registerResult = new MediatorLiveData<>();

    public LiveData<Boolean> loading = _loading;
    public LiveData<String> errorMessage = _errorMessage;
    public LiveData<Resource<LoginResponse>> registerResult = _registerResult;


    public void onClickRegisterButton() {
        if (_loading.getValue()) {
            return;
        }
        if (email.getValue() == null || email.getValue().isEmpty()) {
            _errorMessage.setValue("Email is required");
            return;
        } else if (!ValidEmail.validate(email.getValue())) {
            _errorMessage.setValue("Invalid email");
            return;
        } else if (password.getValue() == null || password.getValue().isEmpty()) {
            _errorMessage.setValue("Password is required");
            return;
        } else if (retypePassword.getValue() == null || retypePassword.getValue().isEmpty()) {
            _errorMessage.setValue("Retype password is required");
            return;
        } else if (password.getValue().length() < 8) {
            _errorMessage.setValue("Password must be at least 8 characters");
            return;
        } else if (!password.getValue().equals(retypePassword.getValue())) {
            _errorMessage.setValue("Password and retype password must be the same");
            return;
        }
        _loading.setValue(true);
        _errorMessage.setValue("");

        LiveData<Resource<LoginResponse>> source = repo.register(
                email.getValue(), password.getValue());

        _registerResult.addSource(source, result -> {
            _registerResult.setValue(result);
            switch (result.status) {
                case LOADING:
                    _loading.setValue(true);
                    break;
                case SUCCESS:
                    _loading.setValue(false);
                    userRepo.insertUser(result.data.getUserInfo());
                    break;
                case ERROR:
                    _loading.setValue(false);
                    _errorMessage.setValue(result.message);
                    break;
            }
            if (result.status != Resource.Status.LOADING) {
                _registerResult.removeSource(source);
            }
        });
    }
}
