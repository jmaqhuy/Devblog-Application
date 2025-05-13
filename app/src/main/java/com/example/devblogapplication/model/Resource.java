package com.example.devblogapplication.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    public enum Status {LOADING, SUCCESS, ERROR}

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(@NonNull String msg) {
        return new Resource<>(Status.ERROR, null, msg);
    }
}