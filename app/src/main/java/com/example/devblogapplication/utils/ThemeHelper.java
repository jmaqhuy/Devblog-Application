package com.example.devblogapplication.utils;

import android.content.Context;
import android.content.res.Configuration;

public class ThemeHelper {

    public static int getThemeBasedRawResource(Context context, int lightResource, int darkResource) {
        int currentNightMode = context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                return darkResource;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                return lightResource;
        }
    }
}
