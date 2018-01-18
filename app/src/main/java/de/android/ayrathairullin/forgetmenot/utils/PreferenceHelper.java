package de.android.ayrathairullin.forgetmenot.utils;


import android.content.Context;
import android.content.SharedPreferences;

import de.android.ayrathairullin.forgetmenot.R;

public class PreferenceHelper {
    private static final String PREFERENCES_FILE_NAME = "preferences";
    public static final String SPLASH_IS_INVISIBLE = "splash_is_invisible";

    private static PreferenceHelper instance;
    private Context context;
    private SharedPreferences preferences;

    private PreferenceHelper() {
    }

    public static PreferenceHelper getInstance() {
        if (instance == null) {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, context.getResources().getBoolean(R.bool.default_value));
    }
}
