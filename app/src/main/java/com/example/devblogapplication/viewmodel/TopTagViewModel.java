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

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public TopTagViewModel(@NonNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
    }

    public void getTopTags() {
        // Execute the entire operation in background thread
        executor.execute(() -> {
            try {
                LiveData<Resource<List<TagWithScore>>> topTagsLiveData = tagRepository.getTopTags();

                mainHandler.post(() -> {
                    tagResource.addSource(topTagsLiveData, result -> {
                        if (result.status == Resource.Status.SUCCESS) {
                            executor.execute(() -> {
                                try {
                                    List<TagWithScore> processedTopTags = result.data;
                                    mainHandler.post(() -> tagResource.setValue(processedTopTags));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mainHandler.post(() -> tagResource.setValue(null));
                                }
                            });
                        }
                    });
                });

                LiveData<Resource<List<Tag>>> allTagsLiveData = tagRepository.getAllTags();

                mainHandler.post(() -> {
                    allTagResource.addSource(allTagsLiveData, result -> {
                        if (result.status == Resource.Status.SUCCESS) {
                            executor.execute(() -> {
                                try {
                                    List<Tag> sortedTags = result.data.stream()
                                            .sorted(Comparator.comparing(Tag::getName, String.CASE_INSENSITIVE_ORDER))
                                            .collect(Collectors.toList());

                                    mainHandler.post(() -> allTagResource.setValue(sortedTags));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mainHandler.post(() -> allTagResource.setValue(null));
                                }
                            });
                        }
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    tagResource.setValue(null);
                    allTagResource.setValue(null);
                });
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
