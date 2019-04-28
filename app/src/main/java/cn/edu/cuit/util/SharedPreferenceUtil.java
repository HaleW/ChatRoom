package cn.edu.cuit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private SharedPreferences sharedPreference;
    private final String fileName = "settings";

    public SharedPreferenceUtil(Context context) {
        sharedPreference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void put(ContentValue... contentValues) {
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = sharedPreference.edit();

        for (ContentValue contentValue : contentValues) {
            if (contentValue.value instanceof String) {
                editor.putString(contentValue.key, contentValue.value.toString()).apply();
            } else if (contentValue.value instanceof Integer) {
                editor.putInt(contentValue.key, (Integer) contentValue.value).apply();
            } else if (contentValue.value instanceof Boolean) {
                editor.putBoolean(contentValue.key, (Boolean) contentValue.value).apply();
            }
        }
    }

    public String getString(SPType key, String defValue) {
        return sharedPreference.getString(key.toString(), defValue);
    }

    public int getInt(SPType key, int defValue) {
        return sharedPreference.getInt(key.toString(), defValue);
    }

    public boolean getBoolean(SPType key, boolean defValue) {
        return sharedPreference.getBoolean(key.toString(), defValue);
    }

    public static class ContentValue {
        String key;
        Object value;

        public ContentValue(SPType key, Object value) {
            this.key = key.toString();
            this.value = value;
        }
    }
}
