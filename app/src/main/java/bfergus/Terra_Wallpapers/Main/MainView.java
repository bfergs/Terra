package bfergus.Terra_Wallpapers.Main;

import android.graphics.Bitmap;

import bfergus.Terra_Wallpapers.Model.RedditApiModel;


public interface MainView{

     void displayProgressViews();

     void setImageView();

     boolean hasWallpaperBitmap();

     void showButtons();

     void showShortToast(String msg);

     void displayWallpaperDownloadFailedDialog();

     void getWallpaperBitmap(final RedditApiModel redditUrls, final int position);

     void saveImageToGallery();
}
