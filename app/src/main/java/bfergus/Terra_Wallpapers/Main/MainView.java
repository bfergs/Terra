package bfergus.Terra_Wallpapers.Main;

import android.graphics.Bitmap;


public interface MainView{

     void showProgressViews(String msg);

     void setImageView(Bitmap bitmap);

     void removeProgressViews();

     void showButtons();

     void showToast(String msg);

     void displayAlertDialog(String title, String message, String positiveText, String negativeText);

}
