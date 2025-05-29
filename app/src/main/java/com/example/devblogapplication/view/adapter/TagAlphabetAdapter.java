package com.example.devblogapplication.view.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.databinding.TagAlphabetItemBinding;
import com.example.devblogapplication.model.Tag;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TagAlphabetAdapter extends RecyclerView.Adapter<TagAlphabetAdapter.TagAlphabetViewHolder> {
    private final List<Character> alphabets = List.of('#','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    private final List<Tag> tags;
    private final RankTagAdapter.OnTagActionListener listener;

    public TagAlphabetAdapter(List<Tag> tags, RankTagAdapter.OnTagActionListener listener) {
        this.tags = tags;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagAlphabetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TagAlphabetItemBinding binding = TagAlphabetItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new TagAlphabetViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAlphabetViewHolder holder, int position) {
        Character character = alphabets.get(position);
        holder.binding.setCharacter(character);
        Log.d("BindingAdapters", "bindAllTags: tag size" + tags.size());
        List<Tag> filteredTags;
        if (character == '#') {
            filteredTags = tags.stream()
                    .filter(t -> t.getName() != null && !Character.isLetter(t.getName().charAt(0)))
                    .sorted(Comparator.comparing(Tag::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        } else {
            filteredTags = tags.stream()
                    .filter(t -> t.getName() != null &&
                            (t.getName().startsWith(character.toString().toUpperCase()) ||
                                    t.getName().startsWith(character.toString().toLowerCase())))
                    .sorted(Comparator.comparing(Tag::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        }
        holder.binding.setTags(filteredTags);
        holder.binding.setListener(listener);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return alphabets.size();
    }

    public static class TagAlphabetViewHolder extends RecyclerView.ViewHolder {
        public final TagAlphabetItemBinding binding;
        public TagAlphabetViewHolder(@NonNull TagAlphabetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Tag> tags) {
        this.tags.clear();
        if (tags != null) {
            this.tags.addAll(tags);
        }
        notifyDataSetChanged();
    }
}
