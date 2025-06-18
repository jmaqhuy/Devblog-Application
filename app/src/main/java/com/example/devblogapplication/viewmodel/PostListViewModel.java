package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class PostListViewModel extends AndroidViewModel {
    private final PostRepository postRepo;
    private final MediatorLiveData<List<PostDTO>> _posts = new MediatorLiveData<>();
    public LiveData<List<PostDTO>> posts = _posts;

    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isError = new MutableLiveData<>();

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private int page = 0;

    public PostListViewModel(@NonNull Application application) {
        super(application);
        postRepo = new PostRepository(application);
    }

    public void loadMyPosts(String uuid) {
        LiveData<Resource<List<PostDTO>>> source = postRepo.getMyPosts(uuid);
        _posts.addSource(source, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                _posts.setValue(result.data);
            }
        });
    }

    public void loadPostForYou() {
        LiveData<Resource<List<PostDTO>>> source = postRepo.getPostForYou(page);
        _posts.addSource(source, result -> {
            _errorMessage.setValue(null);
            isError.setValue(false);
            if (result.status == Resource.Status.SUCCESS) {
                _posts.setValue(result.data);
                if (_posts.getValue() == null || _posts.getValue().isEmpty()) {
                    _errorMessage.setValue("No posts found");
                    isError.setValue(true);
                }
                isLoading.setValue(false);
            } else if (result.status == Resource.Status.LOADING) {
                isLoading.setValue(true);
            } else if (result.status == Resource.Status.ERROR) {
                _errorMessage.setValue(result.message);
                isError.setValue(true);
                isLoading.setValue(false);
            }
        });
    }

    public void loadTopPosts() {
        LiveData<Resource<List<PostDTO>>> source = postRepo.getTopPost(0);
        _posts.addSource(source, result -> {
            _errorMessage.setValue(null);
            isError.setValue(false);
            if (result.status == Resource.Status.SUCCESS) {
                _posts.setValue(result.data);
                if (_posts.getValue() == null || _posts.getValue().isEmpty()) {
                    _errorMessage.setValue(result.message);
                    isError.setValue(true);
                }
            } else if (result.status == Resource.Status.ERROR) {
                _errorMessage.setValue(result.message);
                isError.setValue(true);
            }
        });
    }

    public void loadFollowingPosts() {
        LiveData<Resource<List<PostDTO>>> source = postRepo.getFollowingPosts(0);
        _posts.addSource(source, result -> {
            _errorMessage.setValue(null);
            isError.setValue(false);
            if (result.status == Resource.Status.SUCCESS) {
                _posts.setValue(result.data);
                if (_posts.getValue() == null || _posts.getValue().isEmpty()) {
                    _errorMessage.setValue("No posts found");
                    isError.setValue(true);
                }
            } else if (result.status == Resource.Status.ERROR) {
                _errorMessage.setValue(result.message);
                isError.setValue(true);
            }
        });
    }

    public void likePost(PostDTO post) {
        boolean isLiked = post.isLiked();
        if (isLiked) {
            post.setLikes(post.getLikes() - 1);
        } else {
            post.setLikes(post.getLikes() + 1);
        }
        post.setLiked(!isLiked);
        postRepo.likePost(post.getId());
        List<PostDTO> currentPosts = _posts.getValue();
        if (currentPosts != null) {
            int index = currentPosts.indexOf(post);
            if (index != -1) {
                currentPosts.set(index, post);
                _posts.postValue(new ArrayList<>(currentPosts));
            }
        }
    }

    public void bookmarkPost(PostDTO post) {
        post.setBookmarked(!post.isBookmarked());
        postRepo.bookmarkPost(post.getId());
        List<PostDTO> currentPosts = _posts.getValue();
        if (currentPosts != null) {
            int index = currentPosts.indexOf(post);
            if (index != -1) {
                currentPosts.set(index, post);
                _posts.postValue(new ArrayList<>(currentPosts));
            }
        }
    }

    public void addPost(PostDTO post) {
        List<PostDTO> currentPosts = _posts.getValue();
        if (currentPosts == null) {
            currentPosts = new ArrayList<>();
        } else {
            currentPosts = new ArrayList<>(currentPosts);
        }
        currentPosts.add(0, post);
        _posts.setValue(currentPosts);
    }
}
