package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.TagRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.room.TagInRoom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectFavoriteTagViewModel extends AndroidViewModel {
    private final TagRepository repo;

    public MutableLiveData<String> searchBox = new MutableLiveData<>();

    private MediatorLiveData<Resource<List<Tag>>> _tagsResult = new MediatorLiveData<>();
    public LiveData<Resource<List<Tag>>> tagsResult = _tagsResult;


    private MutableLiveData<Boolean> _loading = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> _success = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> _error = new MutableLiveData<>(false);
    private MutableLiveData<String> _errorMessage = new MutableLiveData<>("");
    public MutableLiveData<Boolean> searchEmptyResult = new MutableLiveData<>(false);
    public MutableLiveData<String> searchErrorMessage = new MutableLiveData<>("");

    public final MutableLiveData<Set<Tag>> selectedTags = new MutableLiveData<>(new HashSet<>());


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

    private List<Tag> fullTagList = new ArrayList<>();
    private boolean isTagFilterSourceAdded = false;

    public SelectFavoriteTagViewModel(Application application) {
        super(application);
        repo = new TagRepository(application);
        // Add searchBox source only once
        if (!isTagFilterSourceAdded) {
            filteredTags.addSource(searchBox, query -> {
                searchEmptyResult.setValue(false);
                if (fullTagList == null || fullTagList.isEmpty()) {
                    filteredTags.setValue(new ArrayList<>());
                    return;
                }
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
            isTagFilterSourceAdded = true;
        }
    }

    private void setupTagFiltering(List<Tag> tags) {
        fullTagList = tags;
        filteredTags.setValue(tags);
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

    public void updateFavoriteTags() {
        if (updating.getValue() != null && updating.getValue()) {
            return;
        }
        if (selectedTags.getValue() == null || selectedTags.getValue().isEmpty()) {
            _updateResult.setValue(Resource.error("No tags selected"));
            return;
        }
        LiveData<Resource> source = repo.updateFavoriteTags(new ArrayList<>(selectedTags.getValue()));
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
