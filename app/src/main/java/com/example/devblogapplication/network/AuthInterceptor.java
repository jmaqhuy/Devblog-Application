package com.example.devblogapplication.network;

import android.content.Context;
import android.util.Log;

import com.example.devblogapplication.utils.SecurePrefsHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context appContext;

    public AuthInterceptor(Context ctx) {
        this.appContext = ctx.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        if (req.url().encodedPath().equals("/api/login") ||
                req.url().encodedPath().equals("/api/register") ||
                req.url().encodedPath().equals("/api/tags")) {
            return chain.proceed(req);
        }
        Log.d("AuthInterceptor", "Intercepting " + req.method() + " " + req.url());
        String token = SecurePrefsHelper.getAccessToken(appContext);
        Log.d("AuthInterceptor", "Token = " + token);
        if (token != null && !token.isEmpty()) {
            req = req.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }
        return chain.proceed(req);
    }
}
