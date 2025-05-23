package com.example.devblogapplication.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.FragmentSharePostBinding;
import com.example.devblogapplication.room.Tag;
import com.example.devblogapplication.utils.FileUtils;
import com.example.devblogapplication.viewmodel.CreatePostViewModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

public class SharePostFragment extends Fragment {
    private FragmentSharePostBinding binding;
    private CreatePostViewModel viewModel;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private static final String TAG = "SharePostFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share_post, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(CreatePostViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.filteredTagsLiveData.observe(getViewLifecycleOwner(), this::displayTags);
        viewModel.selectedTags.observe(getViewLifecycleOwner(), tags -> {
            binding.tagContainer.removeAllViews();
            for (Tag tag : tags) {
                TextView tv = createTagTextView(tag, true);
                binding.tagContainer.addView(tv);
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        MultipartBody.Part imagePart = FileUtils.prepareFilePart(this.getContext(), "image", imageUri);
                        Log.d(TAG, "onCreateView: Get image success");
                        if (imagePart != null) {
                            viewModel.uploadImage(imagePart, false);
                        }
                    }
                }
        );

        binding.addImageToContent.setOnClickListener(view -> {
            launchPickImage();
        });
        return binding.getRoot();
    }
    public void launchPickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImageLauncher.launch(intent);
    }

    private void displayTags(List<Tag> tags) {
        binding.flexbox.removeAllViews();
        if (tags == null) return;

        for (Tag tag : tags) {
            boolean isSelected = viewModel.selectedTags.getValue().contains(tag);
            TextView tagView = createTagTextView(tag, isSelected);
            binding.flexbox.addView(tagView);
        }
    }

    private TextView createTagTextView(Tag tag, boolean isSelected) {
        TextView tagView = new TextView(this.getContext());
        String formatted = getString(R.string.tag_format, tag.getName());
        tagView.setText(formatted);
        tagView.setTextSize(16);
        tagView.setPadding(10, 10, 10, 10);
        tagView.setBackgroundResource(R.drawable.favorite_tag_item);
        tagView.setLayoutParams(createTagLayoutParams());

        updateTagSelectionState(tagView, isSelected);

        tagView.setOnClickListener(v -> {
            List<Tag> selected = viewModel.selectedTags.getValue();
            if (selected == null) {
                selected = new ArrayList<>();
            } else {
                selected = new ArrayList<>(selected);
            }

            if (tagView.isSelected()) {
                selected.remove(tag);
            } else {
                selected.add(tag);
            }

            viewModel.selectedTags.setValue(selected);
            updateTagSelectionState(tagView, !tagView.isSelected());
        });

        return tagView;
    }

    private void updateTagSelectionState(TextView tagView, boolean isSelected) {
        tagView.setSelected(isSelected);
        int textColor = isSelected ? R.color.white : R.color.black;
        tagView.setTextColor(ContextCompat.getColor(this.getContext(), textColor));
    }

    private ViewGroup.LayoutParams createTagLayoutParams() {
        int margin = dpToPx(4);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(margin, margin, margin, margin);
        return params;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}