package bfergus.Terra_Wallpapers.Main;

import android.app.AlertDialog;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import bfergus.Terra_Wallpapers.Faq.FaqActivity;
import bfergus.Terra_Wallpapers.Loaders.WallpaperLoader;
import bfergus.Terra_Wallpapers.Model.RedditApiModel;
import bfergus.Terra_Wallpapers. R;
import bfergus.Terra_Wallpapers.Settings.SettingsActivity;
import bfergus.Terra_Wallpapers.TerraApplication;
import bfergus.Terra_Wallpapers.Utils.DeviceUtils;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainView, ImageButton.OnClickListener{

    MainPresenter presenter;

    Bitmap wallPaperBitmap;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.progressTextView)
    TextView progressTV;

    @Bind(R.id.saveWallPaperIBT)
    ImageButton saveWallPaperIBT;

    @Bind(R.id.setWallPaperIBT)
    ImageButton setWallPaperIBT;

    @Bind(R.id.settingsIBT)
    ImageButton settingsIBT;

    @Bind(R.id.helpIBT)
    ImageButton helpIBT;

    @Bind(R.id.Actual_Image_View)
    ImageView actualImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenterImpl(this);
        saveWallPaperIBT.setOnClickListener(this);
        setWallPaperIBT.setOnClickListener(this);
        settingsIBT.setOnClickListener(this);
        helpIBT.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void setImageView() {
        actualImageView.setImageBitmap(wallPaperBitmap);
    }

    public void displayProgressViews() {
        progressBar.setVisibility(View.VISIBLE);
        progressTV.setText(getString(R.string.fetching_images));
        progressTV.setVisibility(View.VISIBLE);
    }

    public void removeProgressViews() {
        progressBar.setVisibility(View.INVISIBLE);
        progressTV.setVisibility(View.INVISIBLE);
    }

    public void showButtons() {
        saveWallPaperIBT.setVisibility(View.VISIBLE);
        settingsIBT.setVisibility(View.VISIBLE);
        setWallPaperIBT.setVisibility(View.VISIBLE);
        helpIBT.setVisibility(View.VISIBLE);
    }

    public void showShortToast(String msg) {
        Toast toast  = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void showLongToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    private void navigateToSettingsScreen() {
       startActivity(new Intent(this, SettingsActivity.class));
    }

    private void navigateToFaqScreen() {
        startActivity(new Intent(this, FaqActivity.class));
    }

    public boolean hasWallpaperBitmap() {
        return wallPaperBitmap != null;
    }

    public void displayWallpaperDownloadFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.alert_dialog_title));
        builder.setMessage(getString(R.string.alert_dialog_message));
        builder.setPositiveButton(getString(R.string.alert_dialog_positive_bt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.onResume();
            }
        })
                .setNegativeButton(getString(R.string.alert_dialog_negative_bt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }


    public void getWallpaperBitmap(final RedditApiModel redditUrls, final int position) {
        Point size = DeviceUtils.getDeviceSize();
        Glide.with(this)
                .load(redditUrls.getUrl(position))
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        //normally there are 25 URLs to go through
                        if (position == redditUrls.getUrlListLength()) showShortToast(getString(R.string.Out_Of_Images));
                        else getWallpaperBitmap(redditUrls, position + 1);
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
                        removeProgressViews();
                        showButtons();
                    }
                });
    }

    public void startWallpaperLoader() {
        getLoaderManager().initLoader(0, null,
                new LoaderManager.LoaderCallbacks<Boolean>() {
                    @Override
                    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                        showShortToast(TerraApplication.getInstance().getString(R.string.setting_wallpaper));
                        return new WallpaperLoader(getApplicationContext(), wallPaperBitmap);
                    }

                    @Override
                    public void onLoadFinished(Loader<Boolean> loader, Boolean loadSuccessful) {
                        if (loadSuccessful)
                            showShortToast(TerraApplication.getInstance().getString(R.string.Wallpaper_Set));
                        else
                            showShortToast(TerraApplication.getInstance().getString(R.string.Wallpaper_Error));
                    }

                    @Override
                    public void onLoaderReset(Loader<Boolean> loader) {

                    }
                }).forceLoad();
    }

    public void saveImageToGallery() {
        ContentResolver cr = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, presenter.getFileNameForTodaysImage());
        values.put(MediaStore.Images.Media.DISPLAY_NAME, presenter.getFileNameForTodaysImage());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Terra Wallpaper");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (hasWallpaperBitmap()) {
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
            showShortToast(getString(R.string.Wallpaper_Saved));
        }
        catch(SecurityException se) {
            if(url != null) cr.delete(url,null,null);
            showLongToast(getString(R.string.Wallpaper_Error_No_Permissions));
        }
        catch (Exception e) {
            if (url != null) cr.delete(url, null, null);

            showShortToast(getString(R.string.Wallpaper_save_error));
        }
    }

    private static Bitmap storeThumbnail(
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
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
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




    @Override
    public void onClick(View v ) {
        switch(v.getId()) {
            case R.id.saveWallPaperIBT:
                presenter.saveImageToGallery();
                break;
            case R.id.setWallPaperIBT:
                startWallpaperLoader();
                break;
            case R.id.settingsIBT:
                navigateToSettingsScreen();
                break;
            case R.id.helpIBT:
                navigateToFaqScreen();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
