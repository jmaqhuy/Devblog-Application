package com.example.devblogapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostDetailViewModel extends ViewModel {
    private PostRepository repo = new PostRepository();
    private MediatorLiveData<Resource<PostDTO>> _post = new MediatorLiveData<>();

    private MediatorLiveData<List<PostCommentDTO>> _comments = new MediatorLiveData<>();
    public LiveData<List<PostCommentDTO>> comments = _comments;

    public LiveData<Resource<PostDTO>> post = _post;


    public void get(Long postId){
        _post.addSource(repo.getPostDetail(postId), result ->{
            _post.setValue(result);
            if (result.status == Resource.Status.SUCCESS) {
                getComments(postId);
            }
        });
    }

    private void getComments(Long postId){
        _comments.addSource(repo.getComments(postId, null), result ->{
            if (result.status == Resource.Status.SUCCESS){
                _comments.setValue(result.data);
            }
        });
    }
    public void addNewComment(PostCommentDTO comment) {
        if (comments.getValue() != null) {
            List<PostCommentDTO> updatedList = new ArrayList<>(comments.getValue());
            updatedList.add(0, comment);
            _comments.setValue(updatedList);
            PostDTO postDTO = post.getValue().data;
            postDTO.setComments(updatedList.size());
            _post.setValue(Resource.success(postDTO));
        }
    }
}
