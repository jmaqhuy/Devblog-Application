package com.example.devblogapplication.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.RankTagViewBinding;
import com.example.devblogapplication.model.TagWithScore;

import java.util.Comparator;
import java.util.List;

public class RankTagAdapter extends RecyclerView.Adapter<RankTagAdapter.TagViewHolder> {

    private final OnTagActionListener listener;
    private final List<TagWithScore> tags;

    public RankTagAdapter(List<TagWithScore> tags, OnTagActionListener listener) {
        this.tags = tags;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RankTagViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.rank_tag_view,
                parent,
                false);
        return new TagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        TagWithScore tag = tags.get(position);
        holder.binding.setTag(tag);
        holder.binding.setListener(listener);
        holder.binding.setPosition(position);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<TagWithScore> newTags) {
        this.tags.clear();
        if (newTags != null) {
            newTags.sort(Comparator.comparing(TagWithScore::getTotalScore).reversed());
            this.tags.addAll(newTags);
        }
        notifyDataSetChanged();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        private RankTagViewBinding binding;
        public TagViewHolder(@NonNull RankTagViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnTagActionListener {
        void onTagClick(View view, int id);
    }
}
