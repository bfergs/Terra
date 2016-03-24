package bfergus.Terra_Wallpapers.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


public interface MainView{

     void showProgressViews(String msg);

     void setImageView(Bitmap bitmap);

     void removeProgressViews();

     void showButtons();

     void showMessage(String msg);

     void startWallpaperLoader(Bitmap bitmap);

     void displayAlertDialog(String title, String message, String positiveText, String negativeText);

}
