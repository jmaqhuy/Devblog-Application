package com.example.devblogapplication.view.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityTagDetailBinding;
import com.example.devblogapplication.model.PostDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.view.adapter.PostAdapter;
import com.example.devblogapplication.viewmodel.TagDetailViewModel;

public class TagDetailActivity extends AppCompatActivity {
    private TagDetailViewModel viewModel;
    private ActivityTagDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tag_detail);
        viewModel = new ViewModelProvider(this).get(TagDetailViewModel.class);
        binding.setVm(viewModel);
        binding.setListener(new PostAdapter.OnPostActionListener() {
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
        });
        binding.setLifecycleOwner(this);
        int id = getIntent().getIntExtra("id", 0);
        viewModel.getTagDetail(id);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}