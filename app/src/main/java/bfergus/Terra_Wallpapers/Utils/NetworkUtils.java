package bfergus.Terra_Wallpapers.Utils;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import bfergus.Terra_Wallpapers.Model.RedditApiModel;
import bfergus.Terra_Wallpapers.R;
import bfergus.Terra_Wallpapers.Reddit_API_Interface;
import bfergus.Terra_Wallpapers.TerraApplication;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class NetworkUtils {
    
    public static Call<RedditApiModel> getApiCall() {
        Reddit_API_Interface redditAPI = getRetrofit().create(Reddit_API_Interface.class);
        Call<RedditApiModel> Call = redditAPI.getEarth();
        return Call;
    }
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TerraApplication.getInstance().getString(R.string.reddit_base_url))
                .client(getCache())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient getCache() {
        Context context = TerraApplication.getInstance();
        OkHttpClient client = new OkHttpClient();
        Cache cache = new Cache(context.getCacheDir(), 1024 * 1024 * 10);
        client.setCache(cache);
        return client;
    }
}
