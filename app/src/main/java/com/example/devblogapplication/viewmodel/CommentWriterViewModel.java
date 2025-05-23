package com.example.devblogapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.PostRepository;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.Resource;

import java.util.Map;

public class CommentWriterViewModel extends AndroidViewModel {
    public MutableLiveData<String> content = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPreviewing = new MutableLiveData<>(false);
    public PostRepository repository = new PostRepository();
    public MediatorLiveData<Resource<PostCommentDTO>> _pushCommentResult = new MediatorLiveData<>();
    public LiveData<Resource<PostCommentDTO>> pushCommentResult = _pushCommentResult;

    public CommentWriterViewModel(@NonNull Application application) {
        super(application);
    }

    public void setPreviewComment(boolean preview){
        isPreviewing.setValue(preview);
    }

    public void pushComment(Long postId){
        if (content.getValue() == null || content.getValue().isEmpty()){
            Log.d("Comment", "No content to send");
            return;
        }
        Log.d("Comment", "Sending comment: " + content.getValue());
        Map<String, String> comment = Map.of("comment", content.getValue());
        LiveData<Resource<PostCommentDTO>> source = repository.pushComments(postId, comment);

        _pushCommentResult.addSource(source, result -> {
            _pushCommentResult.setValue(result);

        });
    }


}
