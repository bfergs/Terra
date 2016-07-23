package bfergus.Terra_Wallpapers.Settings;

import bfergus.Terra_Wallpapers.Utils.PreferencesUtils;

public class SettingsPresenterImpl implements SettingsPresenter {

    SettingsView view;


    boolean automaticModeEnabled = false;

    //boolean that's true only if automatic mode's status is changed when the activity is stopped.
    boolean alarmStatusChanged = false;

    public SettingsPresenterImpl(SettingsView view) {
        this.view = view;
    }

    public void onResume() {
       automaticModeEnabled = PreferencesUtils.getAutomaticMode();
    }

    public void onPause() {
        PreferencesUtils.setAutomaticMode(automaticModeEnabled);
       if(alarmStatusChanged) handleAutomaticMode();
    }

    private void handleAutomaticMode() {
       view.handleAutomaticWallpaperService(automaticModeEnabled);
    }

    public void onDestroy() {
        view = null;
    }

    public boolean getAutomaticModeStatus() {
        return automaticModeEnabled;
    }

    public void setAutomaticModeStatus(boolean status) {
        this.automaticModeEnabled = status;
        alarmStatusChanged = (alarmStatusChanged == false) ? true : false;
    }
}
