package bfergus.Terra_Wallpapers.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import bfergus.Terra_Wallpapers.R;
import bfergus.Terra_Wallpapers.TerraApplication;

public class PreferencesUtils {

    public static boolean getAutomaticMode(){
        Context context = TerraApplication.getInstance();

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_name), 0);
        return preferences.getBoolean(context.getString(R.string.preference_automatic_mode), false);
    }

    public static void setAutomaticMode(Boolean automaticMode) {
        Context context = TerraApplication.getInstance();

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_name), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.preference_automatic_mode), automaticMode);
        editor.apply();
    }
}
