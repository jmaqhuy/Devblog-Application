package com.example.devblogapplication.view.activity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityMainBinding;
import com.example.devblogapplication.view.adapter.HomeAdapterViewPager;
import com.example.devblogapplication.view.fragment.ExploreFragment;
import com.example.devblogapplication.view.fragment.HomeFragment;
import com.example.devblogapplication.view.fragment.NotificationFragment;
import com.example.devblogapplication.view.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private HomeAdapterViewPager adapter;
    private List<Fragment> fragments = new ArrayList<>();

    private int previousNavItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fragments.add(new HomeFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new NotificationFragment());
        fragments.add(new ProfileFragment());

        previousNavItemId = R.id.home;

        adapter = new HomeAdapterViewPager(this, fragments);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNav.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        binding.bottomNav.setSelectedItemId(R.id.explore);
                        break;
                    case 2:
                        binding.bottomNav.setSelectedItemId(R.id.notification);
                        break;
                    case 3:
                        binding.bottomNav.setSelectedItemId(R.id.profile);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int newNavItemId = item.getItemId();
            if (newNavItemId == previousNavItemId) {
                // TODO: reload page content
                return true;
            }

            if (item.getItemId() == R.id.home) {
                binding.viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.explore) {
                binding.viewPager.setCurrentItem(1);
            } else if (item.getItemId() == R.id.notification) {
                binding.viewPager.setCurrentItem(2);
            } else if (item.getItemId() == R.id.profile) {
                binding.viewPager.setCurrentItem(3);
            }
            previousNavItemId = newNavItemId;

            return true;
        });
    }


}