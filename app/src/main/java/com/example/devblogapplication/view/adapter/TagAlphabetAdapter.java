package com.example.devblogapplication.view.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.databinding.TagAlphabetItemBinding;
import com.example.devblogapplication.model.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagAlphabetAdapter extends RecyclerView.Adapter<TagAlphabetAdapter.TagAlphabetViewHolder> {
    private final List<Character> alphabets = List.of('#','A','B','C','D','E','F','G',
            'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    private final List<Tag> tags;
    private final RankTagAdapter.OnTagActionListener listener;

    private Map<Character, List<Tag>> cachedTagsByAlphabet = new HashMap<>();
    private boolean isCacheValid = false;

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
        if (!isCacheValid) {
            buildCache();
        }

        List<Tag> filteredTags = cachedTagsByAlphabet.getOrDefault(character, new ArrayList<>());
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
        isCacheValid = false;
        notifyDataSetChanged();
    }

    private void buildCache() {
        cachedTagsByAlphabet.clear();

        for (Character alphabet : alphabets) {
            cachedTagsByAlphabet.put(alphabet, new ArrayList<>());
        }
        for (Tag tag : tags) {
            if (tag.getName() != null && !tag.getName().isEmpty()) {
                char firstChar = tag.getName().charAt(0);
                Character key;

                if (Character.isLetter(firstChar)) {
                    key = Character.toUpperCase(firstChar);
                } else {
                    key = '#';
                }

                List<Tag> tagList = cachedTagsByAlphabet.get(key);
                if (tagList != null) {
                    tagList.add(tag);
                }
            }
        }

        isCacheValid = true;
        Log.d("TagAlphabetAdapter", "Cache built with " + tags.size() + " tags");
    }
}
