package com.example.devblogapplication.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.devblogapplication.R;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.model.TagWithScore;
import com.example.devblogapplication.model.UserDTO;
import com.example.devblogapplication.viewmodel.TopTagViewModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

public class BindingAdapters {

    @BindingAdapter("markdown")
    public static void setMarkdown(TextView textView, String markdown) {
        if (markdown == null) {
            textView.setText("");
            return;
        }

        Markwon markwon = Markwon.builder(textView.getContext())
                .usePlugin(ImagesPlugin.create())
                .usePlugin(TablePlugin.create(textView.getContext()))
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(HtmlPlugin.create())
                .build();

        markwon.setMarkdown(textView, markdown);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(RoundedImageView riv, String url) {
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http")) {
//                url = "http://10.0.2.2:8080/images/" + url;
                url = "https://jmaqhuy.id.vn/images/" + url;
            }
            Glide.with(riv.getContext())
                    .load(url)
                    .placeholder(R.drawable.image)
                    .into(riv);
        } else {
            riv.setImageResource(R.drawable.image);
        }
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http")) {
                url = "https://jmaqhuy.id.vn/images/" + url;
            }
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.image)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.image);
        }
    }

    @BindingAdapter({"items", "listener"})
    public static void bindPosts(RecyclerView rv,
                                 List<PostDTO> list,
                                 PostAdapter.OnPostActionListener listener) {
        PostAdapter adapter = (PostAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new PostAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }
        adapter.updateData(list);
    }

    @BindingAdapter({"users", "listener"})
    public static void bindUsers(RecyclerView rv,
                                 List<UserDTO> list,
                                 UserListAdapter.OnUserClickListener listener) {
        UserListAdapter adapter = (UserListAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new UserListAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }
        adapter.updateData(list);
    }

    @BindingAdapter({"tags", "listener"})
    public static void bindTopTags(RecyclerView rv,
                                   List<TagWithScore> listTag,
                                   RankTagAdapter.OnTagActionListener listener) {
        RankTagAdapter adapter = (RankTagAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new RankTagAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }
        adapter.updateData(listTag);
    }

    @BindingAdapter({"allTag", "listener"})
    public static void bindAlphabetAllTags(RecyclerView rv,
                                           List<Tag> allTag,
                                           RankTagAdapter.OnTagActionListener listener) {
        Log.d("BindingAdapters", "Bind Alphabet with recycler view for that character");
        TagAlphabetAdapter adapter = (TagAlphabetAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new TagAlphabetAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }
        adapter.updateData(allTag);
    }

    @BindingAdapter({"filteredTag", "listener"})
    public static void bindAllTags(RecyclerView rv,
                                   List<Tag> filteredTag,
                                   RankTagAdapter.OnTagActionListener listener) {
        AllTagAdapter adapter = (AllTagAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new AllTagAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }

        adapter.updateData(filteredTag);
    }


    @BindingAdapter({"comments", "listener"})
    public static void bindComments(RecyclerView rv,
                                    List<PostCommentDTO> list,
                                    CommentAdapter.OnCommentActionListener listener) {
        CommentAdapter adapter = (CommentAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new CommentAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }
        adapter.updateData(list);
    }

    @BindingAdapter({"searchHistory", "listener"})
    public static void bindSearchHistory(RecyclerView rv,
                                         List<String> searchHistory,
                                         TextViewAdapter.OnSearchItemClickListener listener) {
        TextViewAdapter adapter = (TextViewAdapter) rv.getAdapter();
        if (adapter == null) {
            adapter = new TextViewAdapter(new ArrayList<>(), listener);
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setAdapter(adapter);
        }
        adapter.updateData(searchHistory);
    }

    @BindingAdapter("backgroundTintBasedOnLiked")
    public static void setBackgroundTintBasedOnLiked(View view, boolean isLiked) {
        Context context = view.getContext();
        int tintColor = isLiked ?
                ContextCompat.getColor(context, R.color.light_blue) :
                ContextCompat.getColor(context, R.color.like_holder_background);
        view.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tintColor));
    }

    @BindingAdapter("buttonStyleBasedOnFollowing")
    public static void setButtonStyleBasedOnFollowing(View view, boolean isFollowing) {
        Context context = view.getContext();
        int tintColor = isFollowing ?
                ContextCompat.getColor(context, R.color.edit_text_color) :
                ContextCompat.getColor(context, R.color.blue);

        view.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tintColor));
        if (view instanceof TextView) {
            int textColor = isFollowing ?
                    ContextCompat.getColor(context, R.color.text_color) :
                    ContextCompat.getColor(context, R.color.white);
            ((TextView) view).setTextColor(textColor);
            ((TextView) view).setText(isFollowing ? "Unfollow" : "Follow");
        }
    }

    @BindingAdapter("backgroundTintBasedOnBookmarked")
    public static void setBackgroundTintBasedOnBookmarked(View view, boolean isBookmarked) {
        Context context = view.getContext();
        int tintColor = isBookmarked ?
                ContextCompat.getColor(context, R.color.bookmark_holder_background) :
                ContextCompat.getColor(context, R.color.background);
        view.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tintColor));
    }
}
