package com.example.devblogapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityRegisterBinding;
import com.example.devblogapplication.utils.SecurePrefsHelper;
import com.example.devblogapplication.viewmodel.RegisterViewModel;

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
                    if (result.data.getUserInfo().getAvatarLink() == null || result.data.getUserInfo().getAvatarLink().isEmpty()){
                        Intent intent = new Intent(this, SetupProfileActivity.class);
                        intent.putExtra("email", result.data.getUserInfo().getEmail());
                        Log.d("RegisterActivity", "user email: " + result.data.getUserInfo().getEmail());
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