package com.example.devblogapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.devblogapplication.R;
import com.example.devblogapplication.databinding.ActivityCreatePostBinding;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.view.adapter.CustomFragmentStateAdapter;
import com.example.devblogapplication.view.fragment.SharePostFragment;
import com.example.devblogapplication.view.fragment.WritePostFragment;
import com.example.devblogapplication.viewmodel.CreatePostViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class CreatePostActivity extends AppCompatActivity {

    private ActivityCreatePostBinding binding;
    private CreatePostViewModel viewModel;
    private List<Fragment> fragments = List.of(new WritePostFragment(), new SharePostFragment());
    private List<String> tabNames = List.of("Create", "Share");
    private TabLayoutMediator tabLayoutMediator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
        binding.setLifecycleOwner(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUpTabLayoutWithViewpager();
        viewModel.createPostStatusLiveData.observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                Intent resultIntent = new Intent();
                Log.d("CreatePostActivity", "put share post result to intent, id: " + resource.data.getId());
                resultIntent.putExtra("post", resource.data);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void setUpTabLayoutWithViewpager() {
        binding.viewPager.setAdapter(new CustomFragmentStateAdapter(this, fragments));
        if (tabLayoutMediator == null) {
            tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                    (tab, position) -> tab.setText(tabNames.get(position)));
            tabLayoutMediator.attach();
        }
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TextView textView = (TextView) LayoutInflater
                    .from(this).inflate(R.layout.tab_title, null);
            binding.tabLayout.getTabAt(i).setCustomView(textView);

        }
    }

}