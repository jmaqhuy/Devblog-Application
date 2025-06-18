package com.example.devblogapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.devblogapplication.databinding.TagItemBinding;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.room.TagInRoom;
import com.example.devblogapplication.view.adapter.RankTagAdapter;
import com.example.devblogapplication.viewmodel.SelectFavoriteTagViewModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SelectFavoriteTagActivity extends AppCompatActivity {

    private SelectFavoriteTagViewModel viewModel;
    private ActivitySelectFavoriteTagBinding binding;
//    public final List<Tag> selectedTag = new ArrayList<>();

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



        viewModel.selectedTags.observe(this, tags -> {
            if (tags.size() < 5) {
                binding.btnSave.setText("Save (" + tags.size() + "/5)");
                binding.btnSave.setEnabled(false);
            } else {
                binding.btnSave.setText("Save (5/5)");
                binding.btnSave.setEnabled(true);
            }
            binding.tagContainer.removeAllViews();
            for (Tag tag : tags) {
                TextView tv = createTagTextView(tag);
                binding.tagContainer.addView(tv);
            }

        });


        binding.btnSave.setOnClickListener(v -> viewModel.updateFavoriteTags());
    }

    private void observeViewModel() {
        viewModel.tagsResult.observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                displayTags(result.data);
                viewModel.tagsResult.removeObservers(this);
                String tagsJson = getIntent().getStringExtra("SELECTED_TAGS_JSON");
                if (tagsJson != null) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<Set<Tag>>(){}.getType();
                    Set<Tag> selectedTags = gson.fromJson(tagsJson, listType);
                    viewModel.selectedTags.setValue(selectedTags);
                }
            }
        });

        viewModel.getFilteredTags().observe(this, this::displayTags);

        viewModel.updateResult.observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void displayTags(List<Tag> tags) {
        binding.flexbox.removeAllViews();
        if (tags == null) return;
        Collections.shuffle(tags);

        for (Tag tag : tags) {
            TextView tagView = createTagTextView(tag);
            binding.flexbox.addView(tagView);
        }
    }

    private TextView createTagTextView(Tag tag) {
        TagItemBinding tagItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.tag_item,
                this.binding.flexbox,
                false
        );
        tagItemBinding.setTag(tag);
        boolean isSelected = viewModel.selectedTags.getValue().contains(tag);
        tagItemBinding.tagName.setSelected(isSelected);
        tagItemBinding.setListener((view, id) -> {
            boolean currentlySelected = view.isSelected();
            Set<Tag> selectedTag = viewModel.selectedTags.getValue();
            if (currentlySelected) {
                selectedTag.remove(tag);
                // Find and update the corresponding tag in flexbox
                for (int i = 0; i < binding.flexbox.getChildCount(); i++) {
                    View child = binding.flexbox.getChildAt(i);
                    if (child instanceof TextView) {
                        TextView tv = (TextView) child;
                        if (tv.getText().toString().equals("#" + tag.getName())) {
                            tv.setSelected(false);
                            break;
                        }
                    }
                }
            } else {
                selectedTag.add(tag);
            }
            view.setSelected(!currentlySelected);
            viewModel.selectedTags.setValue(selectedTag);
        });

        tagItemBinding.executePendingBindings();
        return tagItemBinding.tagName;
    }

}
