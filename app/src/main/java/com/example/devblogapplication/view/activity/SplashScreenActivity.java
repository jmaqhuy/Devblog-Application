package com.example.devblogapplication.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.utils.SecurePrefsHelper;
import com.example.devblogapplication.viewmodel.SplashViewModel;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private SplashViewModel vm;
    private final long MIN_DELAY = 3000;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets s = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(s.left, s.top, s.right, s.bottom);
            return insets;
        });

        vm = new ViewModelProvider(this).get(SplashViewModel.class);
        startTime = System.currentTimeMillis();
        if (!SecurePrefsHelper.getRememberMe(this)) {
            SecurePrefsHelper.clearAccessToken(this);
            long elapsed = System.currentTimeMillis() - startTime;
            long delay = Math.max(0, MIN_DELAY - elapsed);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent next = new Intent(this, WelcomeActivity.class);
                startActivity(next);
                finish();
            }, delay);
        }

        vm.sessionValid.observe(this, valid -> {
            if (valid == null) return;
            long elapsed = System.currentTimeMillis() - startTime;
            long delay = Math.max(0, MIN_DELAY - elapsed);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent next;
                if (Boolean.TRUE.equals(valid)) {
                    next = new Intent(this, MainActivity.class);
//                    next = new Intent(this, SelectFavoriteTagActivity.class);
                    // TODO: Chuyen lai ve Main sau test
                } else {
                    next = new Intent(this, WelcomeActivity.class);
                    SecurePrefsHelper.clearAccessToken(this);
                }
                startActivity(next);
                finish();
            }, delay);
        });

        vm.checkSession();
    }
}
