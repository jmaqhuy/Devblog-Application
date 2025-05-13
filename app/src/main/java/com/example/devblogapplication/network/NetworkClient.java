package com.example.devblogapplication.network;

import android.content.Context;

import com.example.devblogapplication.model.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    private static final String BASE_URL = "https://jmaqhuy.id.vn";
    private static Retrofit retrofit;
    private static ApiService apiService;
    private static Converter<ResponseBody, ErrorResponse> errorConverter;

    public static void init(Context appContext) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new AuthInterceptor(appContext))
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
        errorConverter = retrofit.responseBodyConverter(
                ErrorResponse.class, new Annotation[0]
        );
    }

    public static ApiService api() {
        return apiService;
    }

    public static ErrorResponse parseError(ResponseBody body) {
        try {
            return errorConverter.convert(body);
        } catch (IOException e) {
            ErrorResponse fallback = new ErrorResponse();
            fallback.setMessage("Unknown error");
            return fallback;
        }
    }
}

