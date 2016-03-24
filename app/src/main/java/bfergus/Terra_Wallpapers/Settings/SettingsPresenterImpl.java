package bfergus.Terra_Wallpapers.Settings;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import bfergus.Terra_Wallpapers.Services.SetWallpaperService;
import bfergus.Terra_Wallpapers.Utils.PreferencesUtils;

public class SettingsPresenterImpl implements SettingsPresenter {

    SettingsView view;

    Context appContext;

    public static final String PREFS_NAME = "MyPrefsFile";

    boolean automaticMode = false;

    boolean alarmStatusChanged = false;

    SharedPreferences preferences;

    public SettingsPresenterImpl(SettingsView view, Context context) {
        this.view = view;
        this.appContext = context;
    }

    public void onResume() {
       automaticMode = PreferencesUtils.getAutomaticMode();
    }

    public void onStop() {
        PreferencesUtils.setAutomaticMode(automaticMode);
       if(alarmStatusChanged) handleAutomaticMode();
    }

    private void handleAutomaticMode() {
        AlarmManager alarmMngr = (AlarmManager)appContext.getSystemService(appContext.ALARM_SERVICE);
        Intent intent = new Intent(appContext, SetWallpaperService.class);
        PendingIntent  alarmIntent = PendingIntent.getService(appContext, 0, intent, 0);
        alarmMngr.cancel(alarmIntent);
        if(automaticMode) alarmMngr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void onDestroy() {
        view = null;
    }

    public boolean getAutomaticModeStatus() {
        return automaticMode;
    }

    public void setAutomaticModeStatus(boolean status) {
        this.automaticMode = status;
        alarmStatusChanged = (alarmStatusChanged == false) ? true : false;
    }
}
