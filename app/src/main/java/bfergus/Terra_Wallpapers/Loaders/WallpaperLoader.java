package bfergus.Terra_Wallpapers.Loaders;

import android.app.WallpaperManager;
import android.content.AsyncTaskLoader;
import android.content.Context;

import android.graphics.Bitmap;

public class WallpaperLoader extends AsyncTaskLoader<Boolean> {

        Bitmap wallPaperBitmap;

        public WallpaperLoader(Context context, Bitmap bitmap) {
            super(context);
            this.wallPaperBitmap = bitmap;
        }

    @Override
    public Boolean loadInBackground() {
        WallpaperManager mWallpaperManager = WallpaperManager.getInstance(getContext());
        try {
            mWallpaperManager.setBitmap(wallPaperBitmap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

