package com.erudito.main_stay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private SharedPreferences preferences;

    public PreferenceHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void addString(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void addInteger(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void addLong(String key, long value) {
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void addBoolean(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void clear() {
        // here you get your prefrences by either of two methods
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void remove(String key) {
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public int getInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    public Boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }
}
