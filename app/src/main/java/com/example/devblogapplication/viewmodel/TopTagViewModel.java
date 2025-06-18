package com.example.devblogapplication.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.devblogapplication.data.TagRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.TagWithScore;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TopTagViewModel extends AndroidViewModel {
    private final TagRepository tagRepository;
    private final MediatorLiveData<List<TagWithScore>> tagResource = new MediatorLiveData<>();
    public final LiveData<List<TagWithScore>> topTags = tagResource;

    private final MediatorLiveData<List<Tag>> allTagResource = new MediatorLiveData<>();
    public final LiveData<List<Tag>> allTags = allTagResource;

    public TopTagViewModel(@NonNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
        getTopTag();
    }

    public void getTopTag(){
        LiveData<Resource<List<TagWithScore>>> topTagsLiveData = tagRepository.getTopTags();
        tagResource.addSource(topTagsLiveData, result -> {
            tagResource.setValue(result.data);

            if (result.status != Resource.Status.LOADING) {
                tagResource.removeSource(topTagsLiveData);
                LiveData<Resource<List<Tag>>> allTagsLiveData = tagRepository.getAllTags();
                allTagResource.addSource(allTagsLiveData, allTagsResult -> {
                    if (allTagsResult.status == Resource.Status.SUCCESS) {
                        new Thread(() -> {
                            List<Tag> sortedTags = allTagsResult.data.stream()
                                    .sorted(Comparator.comparing(Tag::getName, String.CASE_INSENSITIVE_ORDER))
                                    .collect(Collectors.toList());
                            new Handler(Looper.getMainLooper()).post(() -> {
                                allTagResource.setValue(sortedTags);
                            });
                        }).start();
                    } else {
                        allTagResource.setValue(null);
                    }
                    if (allTagsResult.status != Resource.Status.LOADING) {
                        allTagResource.removeSource(allTagsLiveData);
                    }
                });
            }
        });
    }
}
