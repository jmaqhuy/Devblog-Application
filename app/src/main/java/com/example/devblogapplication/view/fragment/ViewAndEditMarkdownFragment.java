package com.example.devblogapplication.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentViewAndEditMarkdownBinding;
import com.example.devblogapplication.viewmodel.ProfileViewModel;
import com.example.devblogapplication.viewmodel.ViewAndEditMarkdownViewModel;


public class ViewAndEditMarkdownFragment extends Fragment {
    private FragmentViewAndEditMarkdownBinding binding;
    private ViewAndEditMarkdownViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_and_edit_markdown, container, false);
        viewModel = new ViewModelProvider(this).get(ViewAndEditMarkdownViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());


        ProfileViewModel parentViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        parentViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                viewModel.content.setValue(user.data.getReadme());
            }
        });
        return binding.getRoot();
    }
}