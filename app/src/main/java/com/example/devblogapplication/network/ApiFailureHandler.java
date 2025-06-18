package com.example.devblogapplication.network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ApiFailureHandler {
    public static String handleFailure(Throwable t) {
        if (t instanceof IOException) {
            if (t instanceof SocketTimeoutException) {
                return "Request timeout";
            } else if (t instanceof UnknownHostException) {
                return  "No internet connection";
            } else if (t instanceof ConnectException) {
                return  "Connection failed";
            } else {
                return  "Network error";
            }
        } else {
            return "An unexpected error occurred";
        }
    }
}
