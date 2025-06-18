package com.example.devblogapplication.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentPostListBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.utils.BottomMenu;
import com.example.devblogapplication.view.activity.PostDetailActivity;
import com.example.devblogapplication.view.activity.ProfileActivity;
import com.example.devblogapplication.view.adapter.PostAdapter;
import com.example.devblogapplication.viewmodel.PostListViewModel;

public class PostListFragment extends Fragment {

    private FragmentPostListBinding binding;
    private PostListViewModel viewModel;

    private PostContent postContent;
    private String uuid;

    public static PostListFragment newInstance(@NonNull PostContent postContent, @Nullable String uuid) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putString("postContent", postContent.name());
        if (postContent.equals(PostContent.OWN) && uuid != null) {
            args.putString("uuid", uuid);
        }
        fragment.setArguments(args);
        return fragment;
    }
    public static PostListFragment newInstance(@NonNull PostContent postContent) {
        return newInstance(postContent, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String content = getArguments().getString("postContent", "FOR_YOU");
            postContent = PostContent.valueOf(content);
            Log.d("PostListFragment", "onCreate: postContent = " + postContent);
            if (postContent == PostContent.OWN) {
                uuid = getArguments().getString("uuid", null);
                Log.d("PostListFragment", "onCreate: uuid = " + uuid);
            }
        } else {
            postContent = PostContent.OWN;
        }
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
        } else if (postContent == PostContent.OWN) {
            viewModel.loadMyPosts(uuid);
        } else if (postContent == PostContent.TOP) {
            viewModel.loadTopPosts();
        } else if (postContent == PostContent.FOLLOWING) {
            viewModel.loadFollowingPosts();
        }


        binding.setListener(new PostAdapter.OnPostActionListener() {
            @Override
            public void onLike(PostDTO post, int position) {
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
            public void onBookmark(PostDTO post) {
                viewModel.bookmarkPost(post);
            }

            @Override
            public void onMore(PostDTO post) {
                BottomMenu.showPostBottomMenu(getContext(), post);
            }

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
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                intent.putExtra("postId", post.getId());
                startActivity(intent);

            }

            @Override
            public void onAuthorClick(PostDTO post) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("uuid", post.getAuthor().getId());
                startActivity(intent);
            }

            @Override
            public void onExternalAvatarClick(PostDTO post) {
                if (post == null ||
                        post.getExternalPost() == null ||
                        post.getExternalPost().getDomain() == null ||
                        post.getExternalPost().getPath() == null) return;
                String url = "https://" + post.getExternalPost().getDomain();
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    public void addPost(PostDTO postDTO){
        viewModel.addPost(postDTO);
    }

    public enum PostContent {
        TOP,
        FOR_YOU,
        FOLLOWING,
        OWN,
        BOOKMARK
    }
}