package bfergus.Terra_Wallpapers.Settings;

import bfergus.Terra_Wallpapers.Utils.PreferencesUtils;

public class SettingsPresenterImpl implements SettingsPresenter {

    SettingsView view;


    boolean automaticModeEnabled = false;

    public SettingsPresenterImpl(SettingsView view) {
        this.view = view;
    }

    public void onResume() {
       automaticModeEnabled = PreferencesUtils.getAutomaticMode();
        System.out.println(automaticModeEnabled);
    }

    public void onPause() {
        PreferencesUtils.setAutomaticMode(automaticModeEnabled);
       if(automaticModeEnabled) handleAutomaticMode();
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
    }
}
