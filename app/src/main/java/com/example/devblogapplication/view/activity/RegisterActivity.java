package com.example.devblogapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityRegisterBinding;
import com.example.devblogapplication.model.Tag;
import com.example.devblogapplication.utils.SecurePrefsHelper;
import com.example.devblogapplication.viewmodel.RegisterViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        binding.btnGotoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        viewModel.registerResult.observe(this, result -> {
            switch (result.status) {
                case SUCCESS:
                    SecurePrefsHelper.saveAccessToken(this, result.data.getToken());
                    SecurePrefsHelper.saveRememberMe(this, true);
                    if (result.data.getUserInfo().getAvatarLink() == null
                            || result.data.getUserInfo().getAvatarLink().isEmpty()
                            || result.data.getUserInfo().getUsername().isEmpty()
                            || result.data.getUserInfo().getFullname().isEmpty()) {
                        Intent intent = new Intent(this, SetupProfileActivity.class);
                        intent.putExtra("uuid", result.data.getUserInfo().getId());
                        startActivity(intent);
                        finishAffinity();
                    } else if (result.data.getUserInfo().getFavoriteTags() == null
                            || result.data.getUserInfo().getFavoriteTags().size() < 5) {
                        Intent intent = new Intent(this, SelectFavoriteTagActivity.class);
                        Gson gson = new Gson();
                        String tagsJson = gson.toJson(result.data.getUserInfo().getFavoriteTags());
                        intent.putExtra("SELECTED_TAGS_JSON", tagsJson);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }

        });
    }
}