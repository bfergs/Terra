package bfergus.Terra_Wallpapers.Main;



import android.content.Context;


import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.text.DateFormat;

import java.util.Date;

import bfergus.Terra_Wallpapers.Model.RedditApiModel;
import bfergus.Terra_Wallpapers.R;
import bfergus.Terra_Wallpapers.Reddit_API_Interface;
import bfergus.Terra_Wallpapers.TerraApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class  MainPresenterImpl implements MainPresenter {
    private MainView view;

    RedditApiModel redditApiData;



    public MainPresenterImpl(MainView view) {
        this.view = view;
    }

    public void onResume() {
        //prevents useless network calls if wallpaper bitmap isn't null.
        if (view.hasWallpaperBitmap()) {
            view.setImageView();
            view.showButtons();
        } else {
            retrieveImageUrls();
        }
    }

    public void onDestroy() {
        view = null;
    }

    //Retrieves a list of 25 image urls from Reddit.com/r/earthporn
    private void retrieveImageUrls() {
        view.displayProgressViews();
        Call<RedditApiModel> call = getApiCall();
        call.enqueue(new Callback<RedditApiModel>() {
            @Override
            public void onResponse(Response<RedditApiModel> response) {
                redditApiData = response.body();
                view.getWallpaperBitmap(redditApiData,0);
            }

            @Override
            public void onFailure(Throwable t) {
                view.displayWallpaperDownloadFailedDialog();
            }
        });
    }
    public void saveImageToGallery() {
        view.saveImageToGallery();
    }

    public String getFileNameForTodaysImage() {
        DateFormat df = DateFormat.getDateTimeInstance();
        String timeStamp = df.format(new Date().getTime());
        return "Terra_Wallpaper_" + timeStamp;
    }

    private  Call<RedditApiModel> getApiCall() {
        Reddit_API_Interface redditAPI = getRetrofit().create(Reddit_API_Interface.class);
        return redditAPI.getEarth();
    }
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TerraApplication.getInstance().getString(R.string.reddit_base_url))
                .client(getCache())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient getCache() {
        Context context = TerraApplication.getInstance();
        OkHttpClient client = new OkHttpClient();
        Cache cache = new Cache(context.getCacheDir(), 1024 * 1024 * 10);
        client.setCache(cache);
        return client;
    }
}



