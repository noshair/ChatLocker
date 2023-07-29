package com.locker.whatschatlocker.utills;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class PreferenceUtils {
    public static String PREFERENCE_NAME = "whatsapp_locker_2021_pro_secure";

    private PreferenceUtils() {
        throw new AssertionError();
    }

    public static void putObject(Context context, String str, Object obj) {
        putString(context, str, new Gson().toJson(obj));
    }

    public static <T> T getObject(Context context, String str, Class<T> cls) {
        return new Gson().fromJson(getString(context, str), cls);
    }

    public static void putString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getString(Context context, String str) {
        return getString(context, str, (String) null);
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString(str, str2);
    }

    public static boolean putBoolean(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putBoolean(str, z);
        return edit.commit();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getBoolean(str, z);
    }

    public static boolean getBoolean(Context context, String str) {
        return getBoolean(context, str, false);
    }

    public static boolean putLong(Context context, String str, long j) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putLong(str, j);
        return edit.commit();
    }

    public static long getLong(Context context, String str, long j) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getLong(str, j);
    }

    public static long getLong(Context context, String str) {
        return getLong(context, str, -1);
    }

    public static boolean putInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putInt(str, i);
        return edit.commit();
    }

    public static int getInt(Context context, String str) {
        return getInt(context, str, -1);
    }

    public static int getInt(Context context, String str, int i) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getInt(str, i);
    }

    public static void saveToPref(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("code", str);
        edit.apply();
    }

    public static String getCode(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("code", "");
    }

    public static void addNew(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("is_add", str);
        edit.apply();
    }

    public static String getNewAdd(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("is_add", "0");
    }

    public static void setUnlock(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("unlock", str);
        edit.apply();
    }

    public static String getUnLock(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("unlock", "0");
    }

    public static void setFulllock(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("full_lock", str);
        edit.apply();
    }

    public static String getFullLock(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("full_lock", "0");
    }

    public static void setFullUnlock(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("full_unlock", str);
        edit.apply();
    }

    public static String getFullUnLock(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("full_unlock", "0");
    }

    public static void setPassCode(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("email", str);
        edit.apply();
    }

    public static String getPassCode(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("email", (String) null);
    }

    public static void setRating(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString("is_rating", str);
        edit.apply();
    }

    public static String getRating(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString("is_rating", (String) null);
    }

    public static void delete(Context context) {
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().clear().apply();
    }
}
