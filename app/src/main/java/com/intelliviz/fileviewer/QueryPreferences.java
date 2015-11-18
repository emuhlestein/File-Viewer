package com.intelliviz.fileviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Created by edm on 11/16/2015.
 */
public class QueryPreferences {
    private static final String PREF_CURRENT_DIRECTORY = "currentDirectory";

    public static String getCurrentDirectory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CURRENT_DIRECTORY, "");
    }

    public static void setCurrentDirectory(Context context, String currentDirectory) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PREF_CURRENT_DIRECTORY, currentDirectory);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
        else {
            editor.commit();
        }
    }
}
