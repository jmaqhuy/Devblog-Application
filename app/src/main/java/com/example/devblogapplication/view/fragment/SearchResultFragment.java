package com.example.devblogapplication.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devblogapplication.databinding.FragmentSearchResultBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.view.activity.TagDetailActivity;
import com.example.devblogapplication.view.adapter.PostAdapter;
import com.example.devblogapplication.view.adapter.RankTagAdapter;
import com.example.devblogapplication.viewmodel.SearchViewModel;


public class SearchResultFragment extends Fragment {
    private FragmentSearchResultBinding binding;
    private SearchViewModel viewModel;

    public static final String ARG_TYPE = "type";


    public static SearchResultFragment newInstance(SearchType type) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        binding.setVm(viewModel);
        binding.setTagListener((view,id) -> {
            Intent intent = new Intent(getActivity(), TagDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
        binding.setPostListener(new PostAdapter.OnPostActionListener() {
            @Override
            public void onLike(PostDTO post, int position) {

            }

            @Override
            public void onComment(PostDTO post) {

            }

            @Override
            public void onBookmark(PostDTO post) {

            }

            @Override
            public void onMore(PostDTO post) {

            }

            @Override
            public void onReadExternalPost(PostDTO post) {

            }

            @Override
            public void onRead(PostDTO post) {

            }

            @Override
            public void onAuthorClick(PostDTO post) {

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


        binding.setLifecycleOwner(getViewLifecycleOwner());

        SearchType type = SearchType.ALL;
        if (getArguments() != null) {
            String typeStr = getArguments().getString(ARG_TYPE, "ALL");
            type = SearchType.valueOf(typeStr);
        }
        // Show/hide sections based on type
        binding.setSearchType(type.name());
        return binding.getRoot();
    }


    public enum SearchType {
        ALL, POST, USER, TAG
    }
}