package com.example.devblogapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.devblogapplication.data.TagRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.room.Tag;

import java.util.ArrayList;
import java.util.List;

public class SelectFavoriteTagViewModel extends ViewModel {
    private final TagRepository repo = new TagRepository();

    public MutableLiveData<String> searchBox = new MutableLiveData<>();

    private MediatorLiveData<Resource<List<Tag>>> _tagsResult = new MediatorLiveData<>();
    public LiveData<Resource<List<Tag>>> tagsResult = _tagsResult;


    private MutableLiveData<Boolean> _loading = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> _success = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> _error = new MutableLiveData<>(false);
    private MutableLiveData<String> _errorMessage = new MutableLiveData<>("");
    public MutableLiveData<Boolean> searchEmptyResult = new MutableLiveData<>(false);
    public MutableLiveData<String> searchErrorMessage = new MutableLiveData<>("");


    public LiveData<Boolean> loading = _loading;
    public LiveData<Boolean> success = _success;
    public LiveData<Boolean> error = _error;
    public LiveData<String> errorMessage = _errorMessage;


    public MutableLiveData<Boolean> updating = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> update_success = new MutableLiveData<>(false);
    private MediatorLiveData<Resource> _updateResult = new MediatorLiveData<>();
    public LiveData<Resource> updateResult = _updateResult;

    private final MediatorLiveData<List<Tag>> filteredTags = new MediatorLiveData<>();
    public LiveData<List<Tag>> getFilteredTags() {
        return filteredTags;
    }

    public void getAllTags() {
        LiveData<Resource<List<Tag>>> source = repo.getAllTags();

        _tagsResult.addSource(source, result -> {
            _tagsResult.setValue(result);
            switch (result.status) {
                case LOADING:
                    _loading.setValue(true);
                    _error.setValue(false);
                    _success.setValue(false);
                    break;
                case SUCCESS:
                    _loading.setValue(false);
                    _error.setValue(false);
                    _success.setValue(true);
                    setupTagFiltering(result.data);
                    break;
                case ERROR:
                    _loading.setValue(false);
                    _error.setValue(true);
                    _success.setValue(false);
                    _errorMessage.setValue(result.message);
                    break;
            }
            if (result.status != Resource.Status.LOADING) {
                _tagsResult.removeSource(source);
            }

        });

    }
    private void setupTagFiltering(List<Tag> fullTagList) {
        filteredTags.addSource(searchBox, query -> {
            searchEmptyResult.setValue(false);
            if (query == null || query.isEmpty()) {
                filteredTags.setValue(fullTagList);
            } else {
                List<Tag> filtered = new ArrayList<>();
                for (Tag tag : fullTagList) {
                    if (tag.getName().toLowerCase().contains(query.toLowerCase())) {
                        filtered.add(tag);
                    }
                }
                filteredTags.setValue(filtered);
                if (filtered.isEmpty()) {
                    searchErrorMessage.setValue("No tags found matching \"#" + query + "\"");
                    searchEmptyResult.setValue(true);
                }
            }
        });

        filteredTags.setValue(fullTagList);
    }

    public void updateFavoriteTags(List<Tag> tags) {
        if (updating.getValue() != null && updating.getValue()) {
            return;
        }
        LiveData<Resource> source = repo.updateFavoriteTags(tags);
        _updateResult.addSource(source, result -> {
            _updateResult.setValue(result);
            switch (result.status) {
                case LOADING:
                    updating.setValue(true);
                    update_success.setValue(false);
                    break;
                case SUCCESS:
                    updating.setValue(false);
                    update_success.setValue(true);
                    break;

            }
            if (result.status != Resource.Status.LOADING) {
                _updateResult.removeSource(source);
            }
        });
    }


}
























