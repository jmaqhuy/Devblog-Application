package com.example.devblogapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SecurePrefsHelper {

    private static final String FILE_NAME = "secure_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private static SharedPreferences getEncryptedPrefs(Context context) {
        try {
            MasterKey masterKeyAlias = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    context,
                    FILE_NAME,
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveAccessToken(Context context, String token) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply();
        }
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            return prefs.getString(KEY_ACCESS_TOKEN, null);
        }
        return null;
    }

    public static void clearAccessToken(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            prefs.edit().remove(KEY_ACCESS_TOKEN).apply();
        }
    }

    public static void saveRememberMe(Context context, boolean remember) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            prefs.edit().putBoolean("remember_me", remember).apply();
        }
    }

    public static boolean getRememberMe(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            return prefs.getBoolean("remember_me", false);
        }
        return false;
    }
}
