package bfergus.Terra_Wallpapers.Model;

import java.io.Serializable;

/**
 * Created by Bob on 2/25/2016.
 */
public class RedditData implements Serializable {
    public RedditChildren children [];

    public RedditData(RedditChildren children[]) {
        this.children = children;
    }
}
