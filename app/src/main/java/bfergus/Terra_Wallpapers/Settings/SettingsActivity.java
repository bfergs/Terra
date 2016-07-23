package bfergus.Terra_Wallpapers.Settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import bfergus.Terra_Wallpapers.R;
import bfergus.Terra_Wallpapers.Services.SetWallpaperService;


public class SettingsActivity extends AppCompatActivity implements SettingsView {

    SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        presenter = new SettingsPresenterImpl(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause(){
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void handleAutomaticWallpaperService(Boolean activateService) {
        AlarmManager alarmMngr = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, SetWallpaperService.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmMngr.cancel(alarmIntent);
        if(activateService) alarmMngr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        MenuItem menuItem = menu.findItem(R.id.myswitch);
        View view = MenuItemCompat.getActionView(menuItem);
        SwitchCompat mswitch = (SwitchCompat) view.findViewById(R.id.switchForActionBar);
        mswitch.setChecked(presenter.getAutomaticModeStatus());
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setAutomaticModeStatus(isChecked);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.myswitch:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
