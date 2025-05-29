package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.devblogapplication.data.TagRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.response.TagDetailResponse;

public class TagDetailViewModel extends AndroidViewModel {
    private final TagRepository tagRepository;

    private final MediatorLiveData<Resource<TagDetailResponse>> _tagDetail = new MediatorLiveData<>();
    public LiveData<Resource<TagDetailResponse>> tagDetail = _tagDetail;
    public TagDetailViewModel(@NonNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
    }


    public void getTagDetail(int id){
        LiveData<Resource<TagDetailResponse>> source = tagRepository.getTagDetail(id);
        _tagDetail.addSource(source, result -> {
            _tagDetail.setValue(result);
            if (result.status != Resource.Status.LOADING){
                _tagDetail.removeSource(source);
            }
        });
    }


}
