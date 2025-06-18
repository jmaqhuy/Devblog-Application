package com.example.devblogapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentProfileBinding;
import com.example.devblogapplication.databinding.TagItemBinding;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.utils.BottomMenu;
import com.example.devblogapplication.view.activity.TagDetailActivity;
import com.example.devblogapplication.view.adapter.CustomFragmentStateAdapter;
import com.example.devblogapplication.view.adapter.RankTagAdapter;
import com.example.devblogapplication.viewmodel.ProfileViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private TabLayoutMediator tabLayoutMediator;

    private ReadmeFragment readmeFragment;
    private PostListFragment yourPostFragment;
    private PostListFragment bookmarkFragment;
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> tabNames = List.of("Readme", "Post", "Bookmark");


    private String uuid;

    public static ProfileFragment newInstance(String uuid) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString("uuid", null);
            Log.d("TopTagFragment", "Received userId: " + uuid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.getUser(uuid);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            Log.d("ProfileFragment", "User data updated: " + user.status);
            if (user.status == Resource.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.viewPager.setVisibility(View.INVISIBLE);

            } else {
                if (yourPostFragment == null) {
                    yourPostFragment = PostListFragment.newInstance(PostListFragment.PostContent.OWN, viewModel.getUserId());
                }
                if (bookmarkFragment == null) {
                    bookmarkFragment = PostListFragment.newInstance(PostListFragment.PostContent.BOOKMARK);
                }
                if (readmeFragment == null) {
                    readmeFragment = ReadmeFragment.newInstance(Boolean.TRUE.equals(viewModel.editable.getValue()));
                }
                fragments.add(readmeFragment);
                fragments.add(yourPostFragment);
                fragments.add(bookmarkFragment);

                binding.viewPager.setAdapter(new CustomFragmentStateAdapter(requireActivity(), fragments));
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
                displayTags(user.data.getFavoriteTags(), 5);
                binding.progressBar.setVisibility(View.GONE);
                binding.viewPager.setVisibility(View.VISIBLE);

            }
        });
        binding.followBtn.setOnClickListener(v -> {
            Log.d("ProfileFragment", "Follow button clicked");
            viewModel.followUser();
        });
        viewModel.followUser.observe(getViewLifecycleOwner(), result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                viewModel.updateProfile();
            }
        });
        binding.settingsBtn.setOnClickListener(v -> {
            BottomMenu.ShowSettingBottomMenu(requireContext(), Objects.requireNonNull(viewModel.user.getValue()).data);
        });
        return binding.getRoot();
    }


    private void displayTags(Set<Tag> tags, int sz) {
        binding.flexbox.removeAllViews();
        if (tags == null) return;
        int i = 0;
        for (Tag tag : tags) {
            TextView tagView = createTagTextView(tag, (view,id) -> {
                Intent intent = new Intent(getActivity(), TagDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            });
            binding.flexbox.addView(tagView);
            if (++i == sz) {
                break;
            }
        }
        if (tags.size() > sz) {
            String more = " +" + (tags.size() - sz) + " ";
            TextView tagView = createTagTextView(new Tag(-1, more, null), (view, id) -> {
                binding.flexbox.removeAllViews();
                displayTags(viewModel.user.getValue().data.getFavoriteTags(), viewModel.user.getValue().data.getFavoriteTags().size());
            });
            binding.flexbox.addView(tagView);
        }
    }

    private TextView createTagTextView(Tag tag, RankTagAdapter.OnTagActionListener listener) {
        TagItemBinding tagItemBinding = TagItemBinding.inflate(
                LayoutInflater.from(this.getContext()),
                this.binding.flexbox,
                false
        );

        tagItemBinding.setTag(tag);
        tagItemBinding.setListener(listener);
        tagItemBinding.executePendingBindings();

        tagItemBinding.tagName.setTextSize(13);
        return tagItemBinding.tagName;
    }
}