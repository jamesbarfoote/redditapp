package com.app.appydinos.redditscraper;

import android.graphics.Bitmap;

import com.app.appydinos.redditscraper.PostWrapperActivity.Comment;

import java.util.ArrayList;

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
    public ArrayList<Comment> commentsList;
}
