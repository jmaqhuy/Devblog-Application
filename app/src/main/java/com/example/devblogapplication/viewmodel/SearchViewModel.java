package com.example.devblogapplication.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.devblogapplication.data.SearchRepository;
import com.example.devblogapplication.model.Resource;
import com.example.devblogapplication.model.response.SearchResponse;

import java.util.List;

import lombok.var;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";
    private final SearchRepository searchRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.searchRepository = SearchRepository.getInstance();
        getSearchHistory();
    }

    private final MutableLiveData<Boolean> _isShowPost = new MutableLiveData<>(false);
    public LiveData<Boolean> isShowPost = _isShowPost;
    private final MutableLiveData<Boolean> _isShowTag = new MutableLiveData<>(false);
    public LiveData<Boolean> isShowTag = _isShowTag;
    private final MutableLiveData<Boolean> _isShowUser = new MutableLiveData<>(false);
    public LiveData<Boolean> isShowUser = _isShowUser;

    private final MediatorLiveData<List<String>> _searchHistory = new MediatorLiveData<>();
    private final MediatorLiveData<List<String>> _recommendations = new MediatorLiveData<>();

    public MutableLiveData<String> keyword = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> clearFocusEvent = new MutableLiveData<>(false);

    private final MediatorLiveData<Resource<SearchResponse>> _searchResponse = new MediatorLiveData<>();
    public LiveData<Resource<SearchResponse>> searchResponse = _searchResponse;


    private final MutableLiveData<Boolean> _isRecommendVisible = new MutableLiveData<>(true);
    public LiveData<Boolean> isRecommendVisible = _isRecommendVisible;


    public final MediatorLiveData<List<String>> recyclerList = new MediatorLiveData<>();


    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;



    private final Handler handler = new Handler(Looper.getMainLooper());
    private String lastKeyword = "";
    private Runnable recommendRunnable = null;

    {
        // Observe keyword changes for debounce and switching list
        keyword.observeForever(kw -> {
            if (recommendRunnable != null) handler.removeCallbacks(recommendRunnable);
            recommendRunnable = () -> {
                if (kw == null || kw.isEmpty()) {
                    recyclerList.setValue(_searchHistory.getValue());
                } else if (!kw.equals(lastKeyword) && isRecommendVisible.getValue() != null && isRecommendVisible.getValue()) {
                    getSearchRecommendations(kw);
                }
                lastKeyword = kw == null ? "" : kw;
            };
            handler.postDelayed(recommendRunnable, 1000);
        });

        // When history or recommendations change, update recyclerList accordingly
        _searchHistory.observeForever(list -> {
            if (keyword.getValue() == null || keyword.getValue().isEmpty()) {
                recyclerList.setValue(list);
            }
        });
        _recommendations.observeForever(list -> {
            if (keyword.getValue() != null && !keyword.getValue().isEmpty()) {
                recyclerList.setValue(list);
            }
        });
    }

    public void search(String kw) {

        Log.d(TAG, "search: called with kw=" + kw);
        if (isLoading.getValue() != null && isLoading.getValue()) {
            Log.d(TAG, "search: already loading, return");
            return; // Prevent multiple searches while loading
        }
        if (kw != null && !kw.isEmpty()) {
            keyword.setValue(kw);
            // Add the searched keyword to history if not already present
            List<String> history = _searchHistory.getValue();
            if (history != null && !history.contains(kw)) {
                history.add(0, kw); // Add to the top
                _searchHistory.setValue(history);
                Log.d(TAG, "search: added to history: " + kw);
            }
        }

        if (keyword.getValue() == null || keyword.getValue().isEmpty()) {
            Log.d(TAG, "search: keyword is empty, return");
            return;
        }
        clearFocusEvent.setValue(true);
        hideRecommend();
        _isLoading.setValue(true);
        _isShowPost.setValue(false);
        _isShowTag.setValue(false);
        _isShowUser.setValue(false);

        LiveData<Resource<SearchResponse>> source = searchRepository.search(keyword.getValue(), "all");
        _searchResponse.addSource(source, result -> {
            Log.d(TAG, "search: result received, status=" + result.status + ", data=" + result.data);
            if (result.status == Resource.Status.SUCCESS) {
                _searchResponse.setValue(result);
                if (result.data != null && !result.data.getTags().isEmpty()){
                    _isShowTag.setValue(true);
                }
                if (result.data != null && !result.data.getPosts().isEmpty()) {
                    _isShowPost.setValue(true);
                }
                if (result.data != null && !result.data.getUsers().isEmpty()) {
                    _isShowUser.setValue(true);
                }
            }
            if (result.status != Resource.Status.LOADING) {
                _isLoading.setValue(false);
                _searchResponse.removeSource(source);
            }
        });
    }

    public void getSearchHistory() {
        var source = SearchRepository.getInstance().getSearchHistory();
        _searchHistory.addSource(source, result -> {
            _searchHistory.setValue(result.data);
            if (result.status != Resource.Status.LOADING) {
                _searchHistory.removeSource(source);
            }
        });
    }

    public void getSearchRecommendations(String kw) {
        var source = SearchRepository.getInstance().getSearchRecommendations(kw);
        _recommendations.addSource(source, result -> {
            _recommendations.setValue(result.data);
            if (result.status != Resource.Status.LOADING) {
                _recommendations.removeSource(source);
            }
        });
    }

    public void showRecommend() {
        _isRecommendVisible.setValue(true);
    }

    public void hideRecommend() {
        _isRecommendVisible.setValue(false);
    }

//    public void setIsShowPost(boolean isShow) {
//        _isShowPost.setValue(isShow);
//    }
//    public void setIsShowTag(boolean isShow) {
//        _isShowTag.setValue(isShow);
//    }
//    public void setIsShowUser(boolean isShow) {
//        _isShowUser.setValue(isShow);
//    }
}

