package com.example.devblogapplication.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.TagItemBinding;
import com.example.devblogapplication.model.Tag;

import java.util.List;

public class AllTagAdapter extends RecyclerView.Adapter<AllTagAdapter.TagViewHolder> {
    private final List<Tag> tags;
    private final RankTagAdapter.OnTagActionListener listener;

    public AllTagAdapter(List<Tag> tags, RankTagAdapter.OnTagActionListener listener) {
        this.tags = tags;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TagItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.tag_item,
                parent,
                false);
        return new TagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.binding.setTag(tag);
        holder.binding.setListener(listener);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        private TagItemBinding binding;
        public TagViewHolder(@NonNull TagItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Tag> newTags) {
        this.tags.clear();
        if (newTags != null) {
            this.tags.addAll(newTags);
        }
        notifyDataSetChanged();
    }
}
