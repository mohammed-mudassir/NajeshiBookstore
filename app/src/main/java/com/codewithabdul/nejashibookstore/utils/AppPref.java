package com.codewithabdul.nejashibookstore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.codewithabdul.nejashibookstore.models.UserModel;


public class AppPref {
    private static final String KEY_PREF_NAME = "pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL_ID = "email_id";
    private static final String KEY_MOBILE_NO = "mobile_no";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_SELECTED_ADDRESS = "selected_address";

    private static SharedPreferences sharedPreferences;

    private static final String KEY_LOGIN = "isLoggedIn";
    private static final String KEY_FIRST_LAUNCH = "isFirstTimeLaunch";

    private static final String KEY_FCM_TOKEN = "fcm_token";


    private static SharedPreferences.Editor editSharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        return editor;
    }

    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putBoolean(KEY_LOGIN, isLoggedIn);

        editor.commit();

    }

    public static boolean isLoggedIn(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public static void setKeyFirstLaunch(Context context, boolean isLoggedIn) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putBoolean(KEY_FIRST_LAUNCH, isLoggedIn);

        editor.commit();

    }

    public static boolean isKeyFirstLaunch(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public static void setLogout(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putBoolean(KEY_LOGIN, false);
        editor.clear().apply();
        editor.commit();

    }

    public static String getUserId(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_USER_ID, "");
    }


    public static String getName(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_NAME, "");
    }

    public static String getEmailId(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_EMAIL_ID, "");
    }

    public static String getMobileNo(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_MOBILE_NO, "");
    }

    public static void setKeyFcmToken(Context context, String fcm_token) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_FCM_TOKEN, fcm_token);
        editor.commit();

    }

    public static void setKeyProfileImage(Context context, String profile) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_PROFILE_IMAGE, profile);
        editor.commit();

    }

    public static String getKeyProfileImage(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_PROFILE_IMAGE, "");
    }

    public static void setKeySelectedAddress(Context context, String address_id) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_SELECTED_ADDRESS, address_id);
        editor.commit();

    }

    public static String getKeySelectedAddress(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_SELECTED_ADDRESS, "0");
    }

    public static void setKeyLanguage(Context context, String language) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = editSharedPref(context);
        editor.putString(KEY_LANGUAGE, language);
        editor.commit();

    }

    public static String getKeyLanguage(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_LANGUAGE, "en");
    }

    public static void saveUserInfo(Context context, UserModel userModel) {

        SharedPreferences.Editor editor = editSharedPref(context);

        if (userModel.getId() != null)
            if (!userModel.getId().contentEquals(""))
                editor.putString(KEY_USER_ID, userModel.getId());

        if (userModel.getName() != null)
            if (!userModel.getName().contentEquals(""))
                editor.putString(KEY_NAME, userModel.getName());

        if (userModel.getMobile() != null)
            if (!userModel.getMobile().contentEquals(""))
                editor.putString(KEY_MOBILE_NO, userModel.getMobile());

        if (userModel.getEmail() != null)
            if (!userModel.getEmail().contentEquals(""))
                editor.putString(KEY_EMAIL_ID, userModel.getEmail());

        editor.commit();
    }

}
