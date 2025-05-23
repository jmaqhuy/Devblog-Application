package com.example.devblogapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.CommentViewBinding;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.PostDTO;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final List<PostCommentDTO> comments;
    private final OnCommentActionListener listener;

    public CommentAdapter(List<PostCommentDTO> comments, OnCommentActionListener listener) {
        this.comments = comments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.comment_view,
                parent,
                false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.binding.setComment(comments.get(position));
        holder.binding.setListener(listener);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final CommentViewBinding binding;
        public CommentViewHolder(@NonNull CommentViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnCommentActionListener {
        void onReportComment(PostCommentDTO comment);
    }
    public void updateData(List<PostCommentDTO> newComments) {
        comments.clear();
        if (newComments != null) comments.addAll(newComments);
        notifyDataSetChanged();
    }
}
