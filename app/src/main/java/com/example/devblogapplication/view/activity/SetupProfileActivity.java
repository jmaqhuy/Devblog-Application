package com.example.devblogapplication.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivitySetupProfileBinding;
import com.example.devblogapplication.utils.FileUtils;
import com.example.devblogapplication.viewmodel.SetupProfileViewModel;

import okhttp3.MultipartBody;

public class SetupProfileActivity extends AppCompatActivity {

    private ActivitySetupProfileBinding binding;
    private SetupProfileViewModel viewModel;

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(SetupProfileViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.email.setValue(getIntent().getStringExtra("email"));
        Log.d("Setup Profile", "user email: " + getIntent().getStringExtra("email"));

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        MultipartBody.Part imagePart = FileUtils.prepareFilePart(this, "image", imageUri);
                        viewModel.uploadImage(imagePart);
                    }
                }
        );

        viewModel.imageUploadStatus.observe(this, result -> {
            switch (result.status){
                case SUCCESS:
                    Glide.with(this)
                            .load("https://jmaqhuy.id.vn/images/" + result.data)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                            Target<Drawable> target, boolean isFirstResource) {
                                    viewModel.setUploadingStatus(false);
                                    binding.profileImage.setImageDrawable(ContextCompat.getDrawable( getApplicationContext(), R.drawable.sample_avatar));
                                    Toast.makeText(SetupProfileActivity.this, "Fail to load image", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model,
                                                               Target<Drawable> target, DataSource dataSource,
                                                               boolean isFirstResource) {
                                    viewModel.setUploadingStatus(false);
                                    return false;
                                }
                            })
                            .into(binding.profileImage);
                    break;
                case ERROR:
                    viewModel.setUploadingStatus(false);
                    binding.profileImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sample_avatar));
                    break;
                case LOADING:
                    viewModel.setUploadingStatus(true);
            }
        });

        binding.btnPickImage.setOnClickListener(
                v -> launchPickImage()
        );

        viewModel.updateProfileStatus.observe(this, result -> {
            switch (result.status) {
                case SUCCESS:
                    if (result.data.getFavoriteTags() == null || result.data.getFavoriteTags().size() < 5) {
                        Intent intent = new Intent(this, SelectFavoriteTagActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    break;
                case ERROR:
                case LOADING:
                    break;
            }
        });

    }

    public void launchPickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImageLauncher.launch(intent);
    }
}