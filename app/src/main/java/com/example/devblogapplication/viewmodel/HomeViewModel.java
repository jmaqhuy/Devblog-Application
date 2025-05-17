package com.example.devblogapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.data.UserRepository;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.room.User;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final PostRepository repo = new PostRepository();
    private final UserRepository userRepo;

    private final MediatorLiveData<List<PostDTO>> _posts = new MediatorLiveData<>();
    public LiveData<List<PostDTO>> posts = _posts;

    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isError   = new MutableLiveData<>();

    public LiveData<User> user;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        user = userRepo.getUser();
        loadPosts();
    }


    public void loadPosts() {
        isLoading.setValue(true);
        repo.getPosts().observeForever(resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                _posts.setValue(resource.data);
                isError.setValue(false);
            } else if (resource.status == Resource.Status.ERROR) {
                isError.setValue(true);
            }
            isLoading.setValue(false);
        });
    }

    public void likePost(PostDTO post){
        boolean isLiked = post.isLiked();
        if (isLiked) {
            post.setLikes(post.getLikes() - 1);
        } else {
            post.setLikes(post.getLikes() + 1);
        }
        post.setLiked(!isLiked);
        repo.likePost(post.getId());
        _posts.setValue(_posts.getValue());
    }

    public void dislikePost(PostDTO post){
        post.setDisliked(!post.isDisliked());
        _posts.setValue(_posts.getValue());
    }

}
