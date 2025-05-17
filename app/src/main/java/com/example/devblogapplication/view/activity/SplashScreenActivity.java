package com.example.devblogapplication.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.utils.SecurePrefsHelper;
import com.example.devblogapplication.viewmodel.SplashViewModel;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private SplashViewModel vm;
    private final long MIN_DELAY = 3000;
    private long startTime;
    private static final String TAG = "SplashScreenActivity";

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

        // check xem có remember không? -> nếu không
        if (!SecurePrefsHelper.getRememberMe(this)) {
            vm.deleteAllData();
            long elapsed = System.currentTimeMillis() - startTime;
            long delay = Math.max(0, MIN_DELAY - elapsed);
            // xem co tung dang nhap chua -> nếu rồi xóa token, data trong room, chuyển sang login
            if (SecurePrefsHelper.getAccessToken(this) != null){
                SecurePrefsHelper.clearAccessToken(this);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent next = new Intent(this, LoginActivity.class);
                    startActivity(next);
                    finish();
                }, delay);
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent next = new Intent(this, WelcomeActivity.class);
                    startActivity(next);
                    finish();
                }, delay);
            }
        } else {
            vm.sessionValid.observe(this, result -> {
                if (result.status == Resource.Status.LOADING) return;
                Log.d(TAG, "onCreate: Remember = true");

                long elapsed = System.currentTimeMillis() - startTime;
                long delay = Math.max(0, MIN_DELAY - elapsed);


                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent next;
                    if (result.status == Resource.Status.SUCCESS) {
                        // luu user va token
                        Log.d(TAG, "onCreate: Token valid");
                        vm.insertUser(result.data.getUserInfo());
                        SecurePrefsHelper.saveAccessToken(this, result.data.getToken());

                        // check xem truong nao thieu thi chuyen huong cho user cap nhat thong tin
                        if (result.data.getUserInfo().getAvatarLink() == null
                                || result.data.getUserInfo().getAvatarLink().isEmpty()
                                || result.data.getUserInfo().getUsername().isEmpty()
                                || result.data.getUserInfo().getFullname().isEmpty()) {
                            Log.d(TAG, "onCreate: User info null, go to setup profile");
                            next = new Intent(this, SetupProfileActivity.class);
                            next.putExtra("email", result.data.getUserInfo().getEmail());
                            Log.d("RegisterActivity", "user email: " + result.data.getUserInfo().getEmail());
                        } else if (result.data.getUserInfo().getFavoriteTags() == null
                                || result.data.getUserInfo().getFavoriteTags().size() < 5) {
                            Log.d(TAG, "onCreate: favorite tag isn't enough, go to select favorite tag");
                            next = new Intent(this, SelectFavoriteTagActivity.class);
                        } else {
                            Log.d(TAG, "onCreate: go to main");
                            next = new Intent(this, MainActivity.class);
                        }
                    } else {
                        vm.deleteAllData();
                        if (SecurePrefsHelper.getAccessToken(this) != null){
                            next = new Intent(this, LoginActivity.class);
                            SecurePrefsHelper.clearAccessToken(this);
                        } else {
                            next = new Intent(this, WelcomeActivity.class);
                        }

                    }
                    startActivity(next);
                    finish();
                }, delay);
            });

            vm.checkSession();
        }


    }
}
