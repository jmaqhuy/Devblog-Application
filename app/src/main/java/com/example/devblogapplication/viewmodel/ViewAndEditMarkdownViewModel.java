package com.example.devblogapplication.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewAndEditMarkdownViewModel extends ViewModel {
    public final MutableLiveData<String> content = new MutableLiveData<>();
    public final MutableLiveData<Boolean> previewing = new MutableLiveData<>(false);

    public void setPreviewing() {
        previewing.setValue(Boolean.FALSE.equals(previewing.getValue()));
    }

}
