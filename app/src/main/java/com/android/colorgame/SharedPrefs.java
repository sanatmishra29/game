package com.android.colorgame;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class SharedPrefs {

    private static SharedPreferences sharedPreferences;
    private static final String Pref_name=SharedPrefsKey.SharedPref_name;

    public static void putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.apply();
    }
    public static String getKey(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }
    public static void deleteKeys(Context context, String key){
        sharedPreferences = context.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

    public static void putBoolean(Context context, String key, boolean value){
        sharedPreferences = context.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key){
        sharedPreferences = context.getSharedPreferences(Pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }



}
