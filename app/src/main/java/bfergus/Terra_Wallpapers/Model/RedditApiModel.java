package bfergus.Terra_Wallpapers.Model;

import java.io.Serializable;

public class RedditApiModel implements Serializable {
    public RedditData data;

    public RedditApiModel(RedditData data) {
       this.data = data;
    }

    public String getUrl(int position) {
        return data.children[position].data.url;
    }

    public int getUrlListLength() {
        return data.children.length;
    }

}
