package bfergus.Terra_Wallpapers.Services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;


import bfergus.Terra_Wallpapers.Model.RedditApiModel;
import bfergus.Terra_Wallpapers.Utils.NetworkUtils;

import retrofit.Call;
import retrofit.Callback;

import retrofit.Response;


public class SetWallpaperService extends IntentService {


    RedditApiModel redditApiData;

    public SetWallpaperService() {
        super("SetWallpaperService()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        retrieveImageUrls();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Point getDeviceSize() {
        Point size = new Point();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        return size;
    }

    private void getWallPaperBitmap(final int position) {
        Point size = getDeviceSize();
        Glide.with(getApplicationContext())
                .load(redditApiData.data.children[position].data.url)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        getWallPaperBitmap(position + 1);
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
                        setBitmapToWallpaper(resource);

                    }
                });
    }

    private void setBitmapToWallpaper(final Bitmap bitmap) {
        WallpaperManager mWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            mWallpaperManager.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Retrieves a list of 25 image urls from Reddit.com
    private void retrieveImageUrls() {
        Call<RedditApiModel> call = NetworkUtils.getApiCall();
        call.enqueue(new Callback<RedditApiModel>() {
            @Override
            public void onResponse(Response<RedditApiModel> response) {
                redditApiData = response.body();
                getWallPaperBitmap(0);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


}
