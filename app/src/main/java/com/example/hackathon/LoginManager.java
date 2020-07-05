package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Pref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_CONTACT_NUMBER = "contactnumber";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BLOOD_GROUP = "blood";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public LoginManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String email, String password, String gender, String contactnumber, String name, String blood,String type) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_CONTACT_NUMBER, contactnumber);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_BLOOD_GROUP, blood);
        editor.putString(KEY_TYPE, type);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        } else {
            Intent i = new Intent(_context, NavnBottom.class);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_CONTACT_NUMBER, pref.getString(KEY_CONTACT_NUMBER, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_BLOOD_GROUP, pref.getString(KEY_BLOOD_GROUP, null));
        user.put(KEY_TYPE, pref.getString(KEY_TYPE, null));

        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
