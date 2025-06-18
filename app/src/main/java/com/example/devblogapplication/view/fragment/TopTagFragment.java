package com.example.devblogapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentTopTagBinding;
import com.example.devblogapplication.view.activity.TagDetailActivity;
import com.example.devblogapplication.viewmodel.TopTagViewModel;

public class TopTagFragment extends Fragment {
    private FragmentTopTagBinding binding;
    private TopTagViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_tag, container, false);
        viewModel = new ViewModelProvider(this).get(TopTagViewModel.class);
        binding.setVm(viewModel);
        binding.setListener((view, id) -> {
            Intent intent = new Intent(getActivity(), TagDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }
}