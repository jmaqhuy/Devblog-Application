package com.example.devblogapplication.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentCommentWriterBinding;
import com.example.devblogapplication.viewmodel.CommentWriterViewModel;

public class CommentWriterFragment extends Fragment {
    private FragmentCommentWriterBinding binding;
    private CommentWriterViewModel viewModel;
    private final Long postId;

    public CommentWriterFragment(Long postId) {
        this.postId = postId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_writer, container, false);
        viewModel = new ViewModelProvider(this).get(CommentWriterViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        binding.setPostId(postId);

        viewModel.pushCommentResult.observe(getViewLifecycleOwner(), result -> {
           switch (result.status){
               case LOADING:
                   break;
               case SUCCESS:
                   Bundle commentResult = new Bundle();
                   commentResult.putSerializable("comment", result.data);
                   getParentFragmentManager().setFragmentResult("comment_posted", commentResult);

                   requireActivity().getSupportFragmentManager().popBackStack();
                   break;
               case ERROR:
                   Toast.makeText(this.getContext(), result.message, Toast.LENGTH_SHORT).show();
                   break;
           }
        });
        binding.cancelButton.setOnClickListener(v -> cancel());
        return binding.getRoot();
    }

    public void cancel() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}