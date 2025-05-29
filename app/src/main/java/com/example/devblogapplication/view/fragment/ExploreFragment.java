package com.example.devblogapplication.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentExploreBinding;
import com.example.devblogapplication.view.adapter.CustomFragmentStateAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private FragmentExploreBinding binding;
    private PostListFragment topPostFragment = new PostListFragment(PostListFragment.PostContent.TOP);
    private TopTagFragment topTagFragment = new TopTagFragment();
    private List<Fragment> fragments = List.of(topPostFragment, topTagFragment);

    private TabLayoutMediator tabLayoutMediator;
    private List<String> tabNames = List.of("Popular", "Tags");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.viewPager.setAdapter(new CustomFragmentStateAdapter(getActivity(), fragments));
        if (tabLayoutMediator == null) {
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
}