package com.example.devblogapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.ImageRepository;
import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.data.TagRepository;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.request.CreateNewPostRequest;
import com.example.devblogapplication.model.request.ShareExternalPostRequest;
import com.example.devblogapplication.room.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MultipartBody;

public class CreatePostViewModel extends AndroidViewModel {
    private final PostRepository postRepository = new PostRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final ImageRepository imageRepository = new ImageRepository();
    private final String TAG = "CreatePostViewModel";
    private LiveData<Resource<String>> currentUploadSource; // Track current upload source

    private final MutableLiveData<Resource<PostDTO>> createPostStatus = new MutableLiveData<>();
    public final LiveData<Resource<PostDTO>> createPostStatusLiveData = createPostStatus;

    private final MediatorLiveData<List<Tag>> allTags = new MediatorLiveData<>();
    public final LiveData<List<Tag>> allTagsLiveData = allTags;

    // Upload image
    private final MediatorLiveData<Resource<String>> _imageUploadStatus = new MediatorLiveData<>();
    public LiveData<Resource<String>> imageUploadStatus = _imageUploadStatus;

    // Input fields
    public final MutableLiveData<String> title = new MutableLiveData<>();
    public final MutableLiveData<String> content = new MutableLiveData<>();
    public final MutableLiveData<String> searchBox = new MutableLiveData<>();
    public final MutableLiveData<String> url = new MutableLiveData<>();
    public final MutableLiveData<String> thumbnail = new MutableLiveData<>();

    public final MutableLiveData<List<Tag>> tags = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<Tag>> selectedTags = new MutableLiveData<>(new ArrayList<>());

    private final MediatorLiveData<List<Tag>> filteredTags = new MediatorLiveData<>();
    public final LiveData<List<Tag>> filteredTagsLiveData = filteredTags;

    public final MutableLiveData<Boolean> previewing = new MutableLiveData<>(false);

    public CreatePostViewModel(@NonNull Application application) {
        super(application);
        LiveData<Resource<List<Tag>>> tags = tagRepository.getAllTags();
        allTags.addSource(tags, resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                allTags.postValue(resource.data);
            }
        });
    }



    public void setupTagFiltering(List<Tag> fullTagList) {
        if (fullTagList == null) return;
        filteredTags.addSource(searchBox, query -> {
            if (query == null || query.isEmpty()) {
                filteredTags.setValue(List.of());
            } else {
                List<Tag> filtered = new ArrayList<>();
                for (Tag tag : fullTagList) {
                    if (tag.getName().toLowerCase().contains(query.toLowerCase())) {
                        filtered.add(tag);
                    }
                }
                filteredTags.setValue(filtered);
            }
        });

        filteredTags.setValue(List.of());
    }

    public void setPreviewing() {
        previewing.setValue(Boolean.FALSE.equals(previewing.getValue()));
    }

    public void onCreatePost() {
        CreateNewPostRequest request = new CreateNewPostRequest();
        request.setTitle(title.getValue());
        request.setContent(content.getValue());
        request.setThumbnail(thumbnail.getValue());
        request.setTags(selectedTags.getValue()
                .stream()
                .map(t -> t.getName())
                .collect(Collectors.toList()));
        postRepository.createPost(request).observeForever(createPostStatus::postValue);
    }

    public void onSharePost() {
        ShareExternalPostRequest request = new ShareExternalPostRequest();
        request.setUrl(url.getValue());
        request.setTags(selectedTags.getValue()
                .stream()
                .map(t -> t.getName())
                .collect(Collectors.toList()));
        request.setUserThrough(content.getValue());
        postRepository.sharePost(request).observeForever(createPostStatus::postValue);
    }

    public void uploadImage(MultipartBody.Part image, boolean isUpdateThumbnail) {
        if (currentUploadSource != null) {
            _imageUploadStatus.removeSource(currentUploadSource);
        }

        LiveData<Resource<String>> source = imageRepository.uploadImage(image);
        currentUploadSource = source;
        _imageUploadStatus.addSource(source, result -> {
            Log.d(TAG, "upload status: Uploading");
            _imageUploadStatus.setValue(result);
            if (result.status != Resource.Status.LOADING) {
                if (result.status == Resource.Status.SUCCESS) {
                    Log.d(TAG, "upload status: Success");
                    if (isUpdateThumbnail) {
                        thumbnail.setValue(result.data);
                    } else {
                        StringBuilder newContent = getStringBuilder(result);
                        content.setValue(newContent.toString());
                    }
                } else {
                    Log.d(TAG, "upload status: Failed - " + result.message);
                }
                _imageUploadStatus.removeSource(source);
                currentUploadSource = null;
            }
        });
    }

    private @NonNull StringBuilder getStringBuilder(Resource<String> result) {
        String currentContent = content.getValue();
        if (currentContent == null) currentContent = "";

        StringBuilder newContent = new StringBuilder(currentContent);
        if (!currentContent.isEmpty() && !currentContent.endsWith("\n")) {
            newContent.append("\n");
        }

        newContent.append("![alt text](")
                .append("https://jmaqhuy.id.vn/images/")
                .append(result.data).append(")");
        return newContent;
    }
}