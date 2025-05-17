package com.example.devblogapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.PostViewBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.TagDTO;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<PostDTO> posts;
    private final OnPostActionListener listener;

    public PostAdapter(List<PostDTO> posts, OnPostActionListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.post_view,
                parent,
                false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostDTO post = posts.get(position);
        holder.binding.setPost(post);
        holder.binding.like.setSelected(post.isLiked());
        holder.binding.dislike.setSelected(post.isDisliked());
        holder.binding.flexbox.removeAllViews();
        for (TagDTO tag : post.getTags()) {
            TextView tagView = createTagTextView(holder.binding.getRoot().getContext(), tag);
            holder.binding.flexbox.addView(tagView);
        }
        holder.binding.setListener(listener);
        holder.binding.setPosition(position);
        holder.binding.executePendingBindings();
    }
    private TextView createTagTextView(Context context, TagDTO tag) {
        TextView tagView = new TextView(context);
        String formatted = context.getString(R.string.tag_format, tag.getName());
        tagView.setText(formatted);
        tagView.setTextSize(10);
        tagView.setBackgroundResource(R.drawable.favorite_tag_item);
        tagView.setPadding(
                (int) (10 * context.getResources().getDisplayMetrics().density),
                (int) (5 * context.getResources().getDisplayMetrics().density),
                (int) (10 * context.getResources().getDisplayMetrics().density),
                (int) (5 * context.getResources().getDisplayMetrics().density)
        );
        FlexboxLayout.LayoutParams params = getLayoutParams(context);
        tagView.setLayoutParams(params);
        return tagView;
    }

    private static FlexboxLayout.LayoutParams getLayoutParams(Context context) {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(
                (int) (4 * context.getResources().getDisplayMetrics().density),
                (int) (4 * context.getResources().getDisplayMetrics().density),
                (int) (4 * context.getResources().getDisplayMetrics().density),
                (int) (4 * context.getResources().getDisplayMetrics().density)
        );
        return params;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updateData(List<PostDTO> newPosts) {
        posts.clear();
        if (newPosts != null) posts.addAll(newPosts);
        notifyDataSetChanged();
    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public final PostViewBinding binding;

        public PostViewHolder(@NonNull PostViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnPostActionListener {
        void onLike(PostDTO post, int position);

        void onDislike(PostDTO post);

        void onComment(PostDTO post);

        void onBookmark(PostDTO post);

        void onMore(PostDTO post);

        void onReadExternalPost(PostDTO post);

        void onRead(PostDTO post);
    }
}
