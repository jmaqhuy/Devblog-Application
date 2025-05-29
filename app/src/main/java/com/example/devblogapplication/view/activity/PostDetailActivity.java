package com.example.devblogapplication.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityPostDetailBinding;
import com.example.devblogapplication.model.PostCommentDTO;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.view.adapter.CommentAdapter;
import com.example.devblogapplication.view.fragment.CommentWriterFragment;
import com.example.devblogapplication.viewmodel.PostDetailViewModel;
import com.google.android.flexbox.FlexboxLayout;

public class PostDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding binding;
    private PostDetailViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        viewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        viewModel.get(getIntent().getLongExtra("postId", 0L));
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel.post.observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                if (viewModel.post.getValue().data.getExternalPost() == null){
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                        v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
                        return insets;
                    });
                    binding.main.requestApplyInsets();
                }
                for (Tag tag : result.data.getTags()) {
                    TextView tagView = createTagTextView(this, tag);
                    binding.flexbox.addView(tagView);
                }
            }
        });



        binding.setCommentListener(new CommentAdapter.OnCommentActionListener() {

            @Override
            public void onReportComment(PostCommentDTO comment) {

            }
        });

        binding.writeComment.setOnClickListener(v -> {
            Long postId;
            try {
                postId = viewModel.post.getValue().data.getId();
            } catch (NullPointerException e) {
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
    private TextView createTagTextView(Context context, Tag tag) {
        TextView tagView = new TextView(context);
        String formatted = context.getString(R.string.tag_format, tag.getName());
        tagView.setText(formatted);
        tagView.setTextSize(10);
        tagView.setBackgroundResource(R.drawable.favorite_tag_item);
        tagView.setPadding(
                (int) (10 * context.getResources().getDisplayMetrics().density),
                (int) (5 * context.getResources().getDisplayMetrics().density),
                (int) (10 * context.getResources().getDisplayMetrics().density),
                (int) (5 * context.getResources().getDisplayMetrics().density)
        );
        FlexboxLayout.LayoutParams params = getLayoutParams(context);
        tagView.setLayoutParams(params);
        return tagView;
    }

    private static FlexboxLayout.LayoutParams getLayoutParams(Context context) {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(
                0,
                (int) (4 * context.getResources().getDisplayMetrics().density),
                (int) (4 * context.getResources().getDisplayMetrics().density),
                (int) (4 * context.getResources().getDisplayMetrics().density)
        );
        return params;
    }
}