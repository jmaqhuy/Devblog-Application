package com.example.devblogapplication;

import android.app.Application;

import com.example.devblogapplication.network.NetworkClient;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkClient.init(this);
    }
}

