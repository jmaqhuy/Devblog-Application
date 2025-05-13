package com.example.devblogapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.devblogapplication.data.AuthRepository;
import com.example.devblogapplication.utils.SecurePrefsHelper;

public class SplashViewModel extends ViewModel {
    private final AuthRepository repo = new AuthRepository();
    private final MutableLiveData<Boolean> _sessionValid = new MutableLiveData<>();
    public LiveData<Boolean> sessionValid = _sessionValid;

    public void checkSession() {
        _sessionValid.setValue(null);


        LiveData<Boolean> source = repo.introspect();

        Observer<Boolean> observer = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                _sessionValid.setValue(result);
                source.removeObserver(this);
            }
        };

        source.observeForever(observer);
    }
}

