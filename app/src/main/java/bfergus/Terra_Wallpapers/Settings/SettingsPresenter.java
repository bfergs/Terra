package bfergus.Terra_Wallpapers.Settings;

public interface SettingsPresenter {

    void onResume();

    void onStop();

    boolean getAutomaticModeStatus();

    void setAutomaticModeStatus(boolean status);

    void onDestroy();
}
