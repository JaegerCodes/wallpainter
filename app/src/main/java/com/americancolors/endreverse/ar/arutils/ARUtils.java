package com.americancolors.endreverse.ar.arutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ARUtils {
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    public enum VisualizerState {
        LIVE,
        IMAGE,
        MASKING
    }

    public static String appendTimeStamp() {
        return new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.US).format(new Date());
    }

    public static String retriveFilePathMemory(Context context) {
        return context.getSharedPreferences("MyPref", 0).getString("key_name", "No Path");
    }

    public static void saveToPreferences(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(CAMERA_PREF, 0).edit();
        edit.putBoolean(ALLOW_KEY, true);
        edit.apply();
    }

    public static void storeFilePathMemory(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("MyPref", 0).edit();
        edit.putString("key_name", str);
        edit.apply();
    }
}
