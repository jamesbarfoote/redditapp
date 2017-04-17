package com.app.appydinos.redditscraper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by james on 17-Apr-17.
 */

public class MainActivityViewLogic {
    private ArrayList<RedditItemDTO> myDataset;
    private Context mContext;
    private MainActivity mCaller;

    protected void uiInitilised(@NonNull Context context, @NonNull MainActivity caller) {
        myDataset = new ArrayList<>();
        mContext = context;
        mCaller = caller;

        getData();
    }

    private void getData() {
        RedditItemDTO item = new RedditItemDTO();
        item.titleText = "Item Title";
        item.contentText = "Content Text blah lah blah";

        new DownloadImageTask(item).execute("https://api.learn2crack.com/android/images/donut.png");

        RedditItemDTO item2 = new RedditItemDTO();
        item2.titleText = "Second Item Title";
        item2.contentText = "Second Content Text You know this actually works quite well hahahahahahaha";

        new DownloadImageTask(item2).execute("https://pbs.twimg.com/profile_images/606585229034135553/2NqZJYQI.png");

        RedditItemDTO item3 = new RedditItemDTO();
        item3.titleText = "Third Item Title";
        item3.contentText = "Third Content Text ";

        new DownloadImageTask(item3).execute("https://lh3.googleusercontent.com/pzfbnn4blHagLueyDMs9jY7hGOQpWqxR8suYsgKhuZpDBd2f9S8ZxYyGvmE7LwdbO_9jBZXMnq5ya6zDjdq1hpRviYpr8nMJ83vcIpgAmTc5pCJdA0_65GcZUCLX61wIILJ0fT0I7NYsKd1Jpl6pf7H691Y6JtXNq9C_K-h5UiMWTyCC7dZRFVRlgoXX2_de61slcfMjX8v9GgJzCKmoB4ky6yDThE9FBRgxFrkkbAI_26P5_yleg-0MeeAkcLQqJ_ABk-IHP44BuNTrG5-L6MNSDWmkZSeT1OFGDWySKwZino5OO1RM-YmrhzlWm6IvqueO4O0SY5wwcNH0dXonXNk3WV3qfEyCusbZpJCLZKqxMzmcmiBdgVHOMSAtqOcL8I9nIS3lojmHs9FGIxq028SvliWokvsIDn9DmOjux1-TbJfOQOFB2hDgE2PsqXshNdwJqpsLMxTtXldECNMWgAxVUfqNcicv8BNLNJvTbUqQhLguXTIBDaNYSsnOGl7bJ3YPr4JXmxGIu1Rt6uVakwGQ4Zyfa7oDJheOpwBL6FJuwxA7-dMrv18OqPlPGWoTvMhWcLm08OvKoGKG2KyzFtQLQK6TKfb6TvnSEy5MyVpsC2TNPA=w1334-h275-no");

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        RedditItemDTO dto;

        public DownloadImageTask(@NonNull RedditItemDTO currentDTO) {
            dto = currentDTO;
        }

        @Override
        protected @Nullable Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap postImage = null;
            try {
                postImage =  BitmapFactory.decodeStream((InputStream)new URL(urldisplay).getContent());

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                Toast.makeText(mContext, "Error Loading Image !", Toast.LENGTH_SHORT).show();
            }

            return postImage;
        }

        protected void onPostExecute(@Nullable Bitmap result) {
            RedditItemDTO item = dto;
            if(result != null) {
                item.imageBitmap = result;
            }

            myDataset.add(item);
            mCaller.populateScreen(myDataset);
        }
    }
}
