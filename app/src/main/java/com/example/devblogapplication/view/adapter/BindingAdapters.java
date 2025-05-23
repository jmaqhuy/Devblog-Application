package com.example.devblogapplication.view.adapter;

import android.content.Context;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
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

    @BindingAdapter("app:backgroundTintBasedOnLiked")
    public static void setBackgroundTintBasedOnLiked(View view, boolean isLiked) {
        Context context = view.getContext();
        int tintColor = isLiked ?
                ContextCompat.getColor(context, R.color.light_blue) :
                ContextCompat.getColor(context, R.color.light_grey);
        view.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tintColor));
    }
}
