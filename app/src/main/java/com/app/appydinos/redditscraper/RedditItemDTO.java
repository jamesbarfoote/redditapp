package com.app.appydinos.redditscraper;

import android.graphics.Bitmap;

/**
 * Created by james on 17-Apr-17.
 * Object to store an items data
 */

public class RedditItemDTO {
    public Bitmap imageBitmap;
    public String imageURL;
    public String titleText;
    public String contentText;
    public String postURL;
    public String timeSincePost;
    public String points;
    public String user;
    public String comments;
}
