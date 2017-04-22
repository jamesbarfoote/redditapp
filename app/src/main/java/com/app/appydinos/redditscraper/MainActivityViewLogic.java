package com.app.appydinos.redditscraper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by james on 17-Apr-17.
 */

public class MainActivityViewLogic {
    private ArrayList<RedditItemDTO> myDataset;
    private Context mContext;
    private MainActivity mCaller;
    private ProgressDialog mProgressDialog;
    private String mJSONString;

    protected void uiInitilised(@NonNull Context context, @NonNull MainActivity caller) {
        myDataset = new ArrayList<>();
        mContext = context;
        mCaller = caller;

    }

    public void getJSONData(String sub, String order) {
        mCaller.setTitle(sub);
        String url = String.format("https://www.reddit.com/r/%s/%s/.json", sub, order);
        new JsonTask().execute(url);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(mCaller);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mJSONString = result;
            parseJSONFile();
        }
    }

    private void parseJSONFile() {
        ArrayList<RedditItemDTO> redditPosts = new ArrayList<>();
        if (mJSONString != null) {
            try {
                JSONObject jsonObj = new JSONObject(mJSONString);

                // Getting JSON Array node
                JSONObject object = jsonObj.getJSONObject("data");
                JSONArray children = object.getJSONArray("children");

                // looping through All Contacts
                for (int i = 0; i < children.length(); i++) {
                    JSONObject p = children.getJSONObject(i);
                    Iterator<?> keys = p.keys();

                    while( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if ( p.get(key) instanceof JSONObject && key.equals("data")) {
                            JSONObject obj = (JSONObject) p.get(key);
                            String permalink = obj.getString("permalink");
                            String url = obj.getString("url");
                            String title = obj.getString("title");
                            String thumbnail = obj.getString("thumbnail");
                            String score = obj.getString("score");
                            String author = obj.getString("author");
                            String comments = obj.getString("num_comments");
                            String created = obj.getString("created_utc");

                            String imgUrl = "";
                            //Get preview
                            Iterator<?> datakeys = obj.keys();
                            while (datakeys.hasNext()) {
                                String dataKey = (String) datakeys.next();
                                if (obj.get(dataKey) instanceof JSONObject && dataKey.equals("preview")) {
                                    //Get images
                                    JSONObject images = (JSONObject) obj.get(dataKey);

                                    JSONArray arrayOfThings = images.getJSONArray("images");
                                    JSONObject k = (JSONObject) arrayOfThings.get(0);

                                    Iterator<?> imagekeys = k.keys();
                                    while (imagekeys.hasNext()) {
                                        String imageKey = (String) imagekeys.next();
                                        if (imageKey.equals("source")) {
                                            //Get source
                                            JSONObject source = (JSONObject) k.get(imageKey);
                                            imgUrl = source.getString("url");
                                        }
                                    }
                                }
                            }

                            RedditItemDTO thisPost = new RedditItemDTO();
                            thisPost.postURL = permalink;
                            thisPost.titleText = title;
                            thisPost.contentText = url;
                            thisPost.points = score;
                            thisPost.user = author;
                            thisPost.comments = comments;
                            thisPost.imageURL = imgUrl;

//                            if(!thumbnail.isEmpty()) {
////                                new DownloadImageTask(thisPost, postCount).execute(thumbnail);
//                                new DownloadImageTask(thisPost, postCount).execute(imgUrl);
//                            }

                            redditPosts.add(thisPost);
                        }
                    }


                }
            } catch (final JSONException e) {
                Log.e("JSON PARSER", "Json parsing error: " + e.getMessage());
                mCaller.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mCaller, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        } else {
            Log.e("JSON PARSER", "Couldn't get json from server.");
            mCaller.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mCaller, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                }
            });

        }

        mCaller.populateScreen(redditPosts);

        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        RedditItemDTO dto;
//        int index;
//
//        private DownloadImageTask(@NonNull RedditItemDTO currentDTO, int postIndex) {
//            dto = currentDTO;
//            index = postIndex;
//        }
//
//        @Override protected @Nullable Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap postImage = null;
//            try {
//                postImage = BitmapFactory.decodeStream((InputStream) new URL(urldisplay).getContent());
//
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
////                Toast.makeText(mContext, "Error Loading Image !", Toast.LENGTH_SHORT).show();
//            }
//
//            return postImage;
//        }
//
//        protected void onPostExecute(@Nullable Bitmap result) {
//            RedditItemDTO item = dto;
//            if (result != null) {
//                item.imageBitmap = result;
//            }
//            mCaller.replaceItem(dto, index);
//        }
//    }
}
