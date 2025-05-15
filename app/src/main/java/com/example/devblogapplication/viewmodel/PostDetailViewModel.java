package com.example.devblogapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;

public class PostDetailViewModel extends ViewModel {
    private PostRepository repo = new PostRepository();
    private MediatorLiveData<Resource<PostDTO>> _post = new MediatorLiveData<>();

    public LiveData<Resource<PostDTO>> post = _post;


    public void get(Long postId){
        _post.addSource(repo.getPostDetail(postId), result ->{
            _post.setValue(result);
        });
    }
}
