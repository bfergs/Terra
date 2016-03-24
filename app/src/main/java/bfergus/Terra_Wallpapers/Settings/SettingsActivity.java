package bfergus.Terra_Wallpapers.Settings;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import bfergus.Terra_Wallpapers.R;


public class SettingsActivity extends AppCompatActivity implements SettingsView {

    SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        presenter = new SettingsPresenterImpl(this,getApplicationContext());
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
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
