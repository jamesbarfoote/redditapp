package com.app.appydinos.redditscraper.PostWrapperActivity;

/**
 * Created by james on 24-Apr-17.
 * Details about a comment
 */
public class Comment {
    public String htmlText;
    public String author;
    public String points;
    public String postedOn;

    // The 'level' field indicates how deep in the hierarchy
    // this comment is. A top-level comment has a level of 0
    // where as a reply has level 1, and reply of a reply has
    // level 2 and so on...
    public int level;
}
