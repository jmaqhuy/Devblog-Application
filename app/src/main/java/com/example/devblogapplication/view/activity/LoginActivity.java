package com.example.devblogapplication.view.activity;

import android.content.Intent;
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
import com.example.devblogapplication.databinding.ActivityLoginBinding;
import com.example.devblogapplication.utils.SecurePrefsHelper;
import com.example.devblogapplication.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.loginResult.observe(this, result -> {
            switch (result.status) {
                case SUCCESS:
                    SecurePrefsHelper.saveAccessToken(this, result.data.getToken());
                    Toast.makeText(this, "Token save" + result.data.getToken(), Toast.LENGTH_SHORT).show();
                    if (binding.rememberMeCheckbox.isChecked()){
                        SecurePrefsHelper.saveRememberMe(this, true);
                    } else {
                        SecurePrefsHelper.saveRememberMe(this, false);
                    }
                    if (result.data.getUserInfo().getAvatarLink() == null || result.data.getUserInfo().getAvatarLink().isEmpty()){
                        Intent intent = new Intent(this, SetupProfileActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }

                    // TODO: điều hướng sang MainActivity…
                    break;
                default:
                    break;
            }
        });

        binding.btnGotoRegister.setOnClickListener( v-> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}