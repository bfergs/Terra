package bfergus.Terra_Wallpapers;

import android.app.Application;

public class TerraApplication extends Application {

    private static TerraApplication sInstance;

    public TerraApplication() {
        sInstance = this;
    }
    public static synchronized  TerraApplication getInstance() {
        return sInstance;
    }
}
