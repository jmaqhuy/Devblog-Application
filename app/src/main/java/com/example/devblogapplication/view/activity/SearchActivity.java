package com.example.devblogapplication.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.example.devblogapplication.databinding.ActivitySearchBinding;
import com.example.devblogapplication.view.adapter.CustomFragmentStateAdapter;
import com.example.devblogapplication.view.fragment.SearchResultFragment;
import com.example.devblogapplication.viewmodel.SearchViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private List<Fragment> fragments = new ArrayList<>();
    private TabLayoutMediator tabLayoutMediator;
    private final List<String> tabNames = List.of("All", "Posts", "Users", "Tags");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.setVm(viewModel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.editText.requestFocus();

        binding.editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (Boolean.FALSE.equals(viewModel.isRecommendVisible.getValue())) {
                    viewModel.showRecommend();
                }
            }
        });
        binding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEND ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = binding.editText.getText().toString().trim();
                if (!query.isEmpty()) {
                    viewModel.search(query);
                }
                return true;
            }
            return false;
        });

        binding.btnBack.setOnClickListener(v -> finish());

        // Hide RecyclerView when an item is clicked
        binding.setItemSearchListener(item -> {
            viewModel.search(item);
        });

        // Create fragments with type
        fragments.add(SearchResultFragment.newInstance(SearchResultFragment.SearchType.ALL));
        fragments.add(SearchResultFragment.newInstance(SearchResultFragment.SearchType.POST));
        fragments.add(SearchResultFragment.newInstance(SearchResultFragment.SearchType.USER));
        fragments.add(SearchResultFragment.newInstance(SearchResultFragment.SearchType.TAG));
        binding.viewPager.setAdapter(new CustomFragmentStateAdapter(this, fragments));

        if (tabLayoutMediator == null){
            tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                    (tab, position) -> tab.setText(tabNames.get(position)));
            tabLayoutMediator.attach();
        }
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TextView textView = (TextView) LayoutInflater
                    .from(this).inflate(R.layout.tab_title, null);
            binding.tabLayout.getTabAt(i).setCustomView(textView);
        }

        // Observe clearFocusEvent to clear EditText focus after search
        viewModel.clearFocusEvent.observe(this, shouldClear -> {
            if (shouldClear != null && shouldClear) {
                clearFocusEditText();
                // Reset event
                viewModel.clearFocusEvent.setValue(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.editText.getWindowToken(), 0);
                }
            }

        });
    }

    public void clearFocusEditText() {
        binding.editText.clearFocus();
    }
}