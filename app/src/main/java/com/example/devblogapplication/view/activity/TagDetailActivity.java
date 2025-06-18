package com.example.devblogapplication.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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

            @Override
            public void onExternalAvatarClick(PostDTO post) {
                if (post == null ||
                        post.getExternalPost() == null ||
                        post.getExternalPost().getDomain() == null ||
                        post.getExternalPost().getPath() == null) return;
                String url = "https://" + post.getExternalPost().getDomain();
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(TagDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
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
        binding.setFavoriteBtn.setOnClickListener(v -> {
            if (viewModel.tagDetail.getValue() != null &&
                    viewModel.tagDetail.getValue().status == Resource.Status.SUCCESS &&
                    viewModel.tagDetail.getValue().data != null) {

                viewModel.toggleFavorite();

            } else {
                // Thông báo khi chưa load xong data hoặc có lỗi
                Toast.makeText(this, "Please wait for data to load", Toast.LENGTH_SHORT).show();
            }
        });

    }
}