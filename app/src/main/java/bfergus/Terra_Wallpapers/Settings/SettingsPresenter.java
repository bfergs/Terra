package bfergus.Terra_Wallpapers.Settings;

public interface SettingsPresenter {

    void onResume();

    void onPause();

    boolean getAutomaticModeStatus();

    void setAutomaticModeStatus(boolean status);

    void onDestroy();
}
