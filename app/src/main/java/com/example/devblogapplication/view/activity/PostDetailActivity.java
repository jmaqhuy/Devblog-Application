package com.example.devblogapplication.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityPostDetailBinding;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.view.adapter.CommentAdapter;
import com.example.devblogapplication.view.fragment.CommentWriterFragment;
import com.example.devblogapplication.viewmodel.PostDetailViewModel;

public class PostDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding binding;
    private PostDetailViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        getSupportFragmentManager().setFragmentResultListener(
                "comment_posted",
                this,
                (requestKey, bundle) -> {
                    PostCommentDTO newComment = (PostCommentDTO) bundle.getSerializable("comment");
                    if (newComment != null) {
                        viewModel.addNewComment(newComment);
                    }
                }
        );

        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.get(getIntent().getLongExtra("postId", 0L));
        binding.setCommentListener(new CommentAdapter.OnCommentActionListener() {

            @Override
            public void onReportComment(PostCommentDTO comment) {

            }
        });

        binding.writeComment.setOnClickListener(v -> {
            Long postId;
            try {
                postId = viewModel.post.getValue().data.getId();
            } catch (NullPointerException e){
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.e("PostDetailActivity", "onCreate: ", e);
                return;
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.overlayFragment), (v2, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v2.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


           getSupportFragmentManager()
                   .beginTransaction()
                   .setCustomAnimations(
                           R.anim.slide_in_up,
                           R.anim.fade_out,
                           R.anim.fade_in,
                           R.anim.slide_out_down
                   )
                   .add(R.id.overlayFragment, new CommentWriterFragment(postId))
                   .addToBackStack(null)
                   .commit();
        });
    }
}