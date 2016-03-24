package bfergus.Terra_Wallpapers.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import bfergus.Terra_Wallpapers.Services.SetWallpaperService;
import bfergus.Terra_Wallpapers.Utils.PreferencesUtils;


public class StartSetWallpaperServiceOnBootReceiver extends BroadcastReceiver {
    //Resets the set wallpaper service on Boot-up if enabled.
    @Override
    public void onReceive(Context context, Intent intent) {
        if(PreferencesUtils.getAutomaticMode()) startAutomaticMode(context);

    }

    private void startAutomaticMode(Context context) {
        AlarmManager alarmMngr = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, SetWallpaperService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmMngr.cancel(alarmIntent);
        alarmMngr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
