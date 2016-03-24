package bfergus.Terra_Wallpapers;

import bfergus.Terra_Wallpapers.Model.RedditApiModel;
import retrofit.Call;
import retrofit.http.GET;


public interface Reddit_API_Interface {
    @GET("/r/earthporn/top/.json?sort=top&t=day")
    Call<RedditApiModel> getEarth();
}
