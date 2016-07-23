package bfergus.Terra_Wallpapers.Model;

import java.io.Serializable;

public class RedditData implements Serializable {
    public RedditChildren children [];

    public RedditData(RedditChildren children[]) {
        this.children = children;
    }
}
