package com.example.devblogapplication.view.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivitySelectFavoriteTagBinding;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.viewmodel.SelectFavoriteTagViewModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFavoriteTagActivity extends AppCompatActivity {

    private SelectFavoriteTagViewModel viewModel;
    private ActivitySelectFavoriteTagBinding binding;
    private final List<Tag> selectedTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_favorite_tag);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(SelectFavoriteTagViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        observeViewModel();
        viewModel.getAllTags();
        if (selectedTags.size() < 5) {
            binding.btnSave.setEnabled(false);
        } else {
            binding.btnSave.setEnabled(true);
        }
    }

    private void observeViewModel() {
        viewModel.tagsResult.observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                displayTags(result.data);
            }
        });

        viewModel.getFilteredTags().observe(this, this::displayTags);
    }

    private void displayTags(List<Tag> tags) {
        binding.flexbox.removeAllViews();
        if (tags == null) return;
        // trộn tag
        Collections.shuffle(tags);

        List<Tag> displayTags = tags.size() > 20 ? tags.subList(0, 20) : tags;

        for (Tag tag : displayTags) {
            boolean isSelected = selectedTags.contains(tag);
            TextView tagView = createTagTextView(tag, isSelected);
            binding.flexbox.addView(tagView);
        }
    }

    private TextView createTagTextView(Tag tag, boolean isSelected) {
        TextView tagView = new TextView(this);
        String formatted = getString(R.string.tag_format, tag.getName());
        tagView.setText(formatted);
        tagView.setTextSize(16);
        tagView.setPadding(10, 10, 10, 10);
        tagView.setBackgroundResource(R.drawable.favorite_tag_item);
        tagView.setLayoutParams(createTagLayoutParams());

        updateTagSelectionState(tagView, isSelected);

        tagView.setOnClickListener(v -> {
            boolean currentlySelected = tagView.isSelected();
            if (currentlySelected) {
                selectedTags.remove(tag);
            } else {
                selectedTags.add(tag);
            }
            updateTagSelectionState(tagView, !currentlySelected);
        });

        return tagView;
    }

    private void updateTagSelectionState(TextView tagView, boolean isSelected) {
        tagView.setSelected(isSelected);
        int textColor = isSelected ? R.color.white : R.color.black;
        tagView.setTextColor(ContextCompat.getColor(this, textColor));
        binding.btnSave.setText("Save (" + selectedTags.size() + "/5)");
        if (selectedTags.size() < 5) {
            binding.btnSave.setEnabled(false);
        } else {
            binding.btnSave.setEnabled(true);
        }
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
