package bfergus.Terra_Wallpapers.Main;



import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;

import java.util.Date;

import bfergus.Terra_Wallpapers.Model.RedditApiModel;
import bfergus.Terra_Wallpapers.R;
import bfergus.Terra_Wallpapers.Utils.NetworkUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class MainPresenterImpl implements MainPresenter {
    private MainView view;

    RedditApiModel redditApiData;

    Bitmap wallPaperBitmap;

    Context appContext;

    public MainPresenterImpl(MainView view, Context context) {
        this.view = view;
        this.appContext = context;
    }

    public void onResume() {
        if(wallPaperBitmap != null) {
            view.setImageView(wallPaperBitmap);
            view.showButtons();
        }
        else {
            retrieveImageUrls();
        }
    }

    public void onDestroy() {
        view = null;
    }

    //Retrieves a list of 25 image urls from Reddit.com
    private void retrieveImageUrls() {
        view.showProgressViews(appContext.getString(R.string.fetching_images));
        Call<RedditApiModel> call = NetworkUtils.getApiCall();
        call.enqueue(new Callback<RedditApiModel>() {
            @Override
            public void onResponse(Response<RedditApiModel> response) {
                redditApiData = response.body();
                getWallpaperBitmap(0);
            }

            @Override
            public void onFailure(Throwable t) {
                view.displayAlertDialog(appContext.getString(R.string.alert_dialog_title),
                        appContext.getString(R.string.alert_dialog_message),
                        appContext.getString(R.string.alert_dialog_positive_bt),
                        appContext.getString(R.string.alert_dialog_negative_bt));
            }
        });
    }

    private Point getDeviceSize() {
        Point size = new Point();
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        return size;
    }

    private void getWallpaperBitmap(final int position) {
        Point size = getDeviceSize();
        Glide.with(appContext)
                .load(redditApiData.getUrl(position))
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if(position == redditApiData.getUrlListLength()) view.showMessage(appContext.getString(R.string.Out_Of_Images));
                        else getWallpaperBitmap(position + 1);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(size.x, size.y) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        wallPaperBitmap = resource;
                        setImageView();
                        showButtons();
                    }
                });
    }
    public void showButtons() {
        view.removeProgressViews();
        view.showButtons();
    }

    public void setImageView() {
        view.setImageView(wallPaperBitmap);
    }

    public  void addImageToGallery(){
        ContentResolver cr = appContext.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, getFileNameForTodaysImage());
        values.put(MediaStore.Images.Media.DISPLAY_NAME, getFileNameForTodaysImage());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Terra Wallpaper");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (wallPaperBitmap != null) {
                OutputStream imageOut;
                imageOut = cr.openOutputStream(url);
                try {
                    wallPaperBitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
            view.showMessage(appContext.getString(R.string.Wallpaper_Saved));
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
            }
            view.showMessage(appContext.getString(R.string.Wallpaper_save_error));
        }
    }

    private static  Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }


    private static String getFileNameForTodaysImage() {
        DateFormat df = DateFormat.getDateTimeInstance();
        String timeStamp = df.format(new Date().getTime());
        return "Terra_Wallpaper_" + timeStamp;
    }

    public void startWallpaperLoader() {
        view.startWallpaperLoader(wallPaperBitmap);
    }
}
