package com.example.devblogapplication.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.databinding.SearchItemBinding;

import java.util.List;

public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.TextViewHolder> {
    private final List<String> items;
    private final OnSearchItemClickListener listener;

    public TextViewAdapter(List<String> items, OnSearchItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemBinding binding = SearchItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new TextViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        String item = items.get(position);
        holder.binding.setContent(item);
        holder.binding.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public final SearchItemBinding binding;
        public TextViewHolder(SearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnSearchItemClickListener {
        void onItemClick(String item);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<String> newList) {
        this.items.clear();
        if (newList != null) {
            this.items.addAll(newList);
        }
        notifyDataSetChanged();
    }
}
