package com.example.devblogapplication.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentReadmeBinding;
import com.example.devblogapplication.viewmodel.ProfileViewModel;

public class ReadmeFragment extends Fragment {
    private FragmentReadmeBinding binding;

    private static boolean isEditable;

    public static ReadmeFragment newInstance(boolean isEditable) {
        ReadmeFragment fragment = new ReadmeFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEditable", isEditable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isEditable = getArguments().getBoolean("isEditable", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_readme, container, false);
        binding.setLifecycleOwner(this);
        ProfileViewModel parentViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        parentViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.readme.setText(user.data.getReadme());
            }
        });
        if (isEditable) {
            binding.updateReadmeBtn.setVisibility(View.VISIBLE);
        } else {
            binding.updateReadmeBtn.setVisibility(View.GONE);
        }
        binding.updateReadmeBtn.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_up,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out_down
                )
                .replace(R.id.container, new ViewAndEditMarkdownFragment())
                .addToBackStack(null)
                .commit());
        return binding.getRoot();
    }
}