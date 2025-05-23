package com.example.devblogapplication.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentPostListBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.view.activity.PostDetailActivity;
import com.example.devblogapplication.view.adapter.PostAdapter;
import com.example.devblogapplication.viewmodel.PostListViewModel;

public class PostListFragment extends Fragment {

    private FragmentPostListBinding binding;
    private PostListViewModel viewModel;
    private PostContent postContent;

    public PostListFragment(PostContent postContent) {
        this.postContent = postContent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_list, container, false);
        viewModel = new ViewModelProvider(this).get(PostListViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        if (postContent == PostContent.FOR_YOU) {
            viewModel.loadPostForYou();
        }

        binding.setListener(new PostAdapter.OnPostActionListener() {
            @Override
            public void onLike(PostDTO post, int position) {
                PostAdapter.PostViewHolder holder = (PostAdapter.PostViewHolder) binding.postView.findViewHolderForAdapterPosition(position);
                if (holder != null) {
                    animateLikeButton(holder.binding.like);
                }
                viewModel.likePost(post);
            }

            @Override
            public void onComment(PostDTO post) {
                if (post == null || post.getId() == null) return;
                if (post.getExternalPost() == null){
                    Intent intent = new Intent(getContext(), PostDetailActivity.class);
                    intent.putExtra("postId", post.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onBookmark(PostDTO post) { /*...*/ }

            @Override
            public void onMore(PostDTO post) { /*...*/ }

            @Override
            public void onReadExternalPost(PostDTO post) {
                if (post == null ||
                        post.getExternalPost() == null ||
                        post.getExternalPost().getDomain() == null ||
                        post.getExternalPost().getPath() == null) return;
                String url = "https://" + post.getExternalPost().getDomain() + post.getExternalPost().getPath();
                try {
                    Uri uri = Uri.parse(url);
                    Log.d("HomeFragment", "onReadExternalPost: uri" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRead(PostDTO post){
                if (post == null || post.getId() == null) return;
                if (post.getExternalPost() == null){
                    Intent intent = new Intent(getContext(), PostDetailActivity.class);
                    intent.putExtra("postId", post.getId());
                    startActivity(intent);
                }

            }
        });

        return binding.getRoot();
    }

    private void animateLikeButton(ImageButton likeButton) {
        likeButton.setPivotX(0f);
        likeButton.setPivotY(likeButton.getHeight());

        ObjectAnimator scaleXUp = ObjectAnimator.ofFloat(likeButton, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleYUp = ObjectAnimator.ofFloat(likeButton, "scaleY", 1f, 1.1f);
        ObjectAnimator rotateLeft = ObjectAnimator.ofFloat(likeButton, "rotation", 0f, -20f);

        ObjectAnimator scaleXDown = ObjectAnimator.ofFloat(likeButton, "scaleX", 1.1f, 1f);
        ObjectAnimator scaleYDown = ObjectAnimator.ofFloat(likeButton, "scaleY", 1.1f, 1f);
        ObjectAnimator rotateBack = ObjectAnimator.ofFloat(likeButton, "rotation", -20f, 0f);

        scaleXUp.setDuration(300);
        scaleYUp.setDuration(300);
        rotateLeft.setDuration(300);
        scaleXDown.setDuration(300);
        scaleYDown.setDuration(300);
        rotateBack.setDuration(300);

        AnimatorSet upSet = new AnimatorSet();
        upSet.playTogether(scaleXUp, scaleYUp, rotateLeft);

        AnimatorSet downSet = new AnimatorSet();
        downSet.playTogether(scaleXDown, scaleYDown, rotateBack);

        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(upSet, downSet);
        fullAnimation.start();
    }

    public void addPost(PostDTO postDTO){
        viewModel.addPost(postDTO);
    }

    public enum PostContent {
        FOR_YOU,
        FOLLOWING
    }
}