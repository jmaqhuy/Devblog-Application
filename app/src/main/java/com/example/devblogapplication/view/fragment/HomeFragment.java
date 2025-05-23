package com.example.devblogapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentHomeBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.view.activity.CreatePostActivity;
import com.example.devblogapplication.view.adapter.CustomFragmentStateAdapter;
import com.example.devblogapplication.viewmodel.HomeViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private PostDTO createPostResult;
    private PostListFragment postForYouFragment = new PostListFragment(PostListFragment.PostContent.FOR_YOU);
    private PostListFragment postFollowingFragment = new PostListFragment(PostListFragment.PostContent.FOLLOWING);
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabNames = List.of("For you", "Following");
    private TabLayoutMediator tabLayoutMediator;

    private ActivityResultLauncher<Intent> resultLauncher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.createPost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreatePostActivity.class);
            resultLauncher.launch(intent);
        });
        fragments.add(postForYouFragment);
        fragments.add(postFollowingFragment);
        binding.viewPager.setAdapter(new CustomFragmentStateAdapter(this.getActivity(), fragments));
        if (tabLayoutMediator == null){
            tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                    (tab, position) -> tab.setText(tabNames.get(position)));
            tabLayoutMediator.attach();
        }
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TextView textView = (TextView) LayoutInflater
                    .from(this.getContext()).inflate(R.layout.tab_title, null);
            binding.tabLayout.getTabAt(i).setCustomView(textView);
        }

        return binding.getRoot();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null){
                            createPostResult = (PostDTO) data.getSerializableExtra("post");
                            if (createPostResult != null) {
                                Log.d("HomeFragment", "Received Post ID: " + createPostResult.getId());
                                postForYouFragment.addPost(createPostResult);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}