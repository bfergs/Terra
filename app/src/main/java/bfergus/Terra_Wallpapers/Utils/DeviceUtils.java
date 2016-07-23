package bfergus.Terra_Wallpapers.Utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import bfergus.Terra_Wallpapers.TerraApplication;


public class DeviceUtils {

    public static Point getDeviceSize() {
        android.graphics.Point size = new Point();
        WindowManager wm = (WindowManager) TerraApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        return size;
    }
}
