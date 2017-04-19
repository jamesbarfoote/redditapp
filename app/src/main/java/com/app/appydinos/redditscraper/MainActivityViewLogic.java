package com.app.appydinos.redditscraper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.R.attr.category;
import static android.R.attr.name;

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

        getJSONData();
    }

    private void getJSONData() {
        new JsonTask().execute("https://www.reddit.com/r/android/new/.json");

//        URL url = null;
//        try {
//            url = new URL("https://www.reddit.com/r/newzealand/.rss");
//
//            URLConnection conn = url.openConnection();
//
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(conn.getInputStream());
//
//            NodeList nodeList = doc.getElementsByTagName("entry");
//
//            ArrayList<String> authorNames = new ArrayList<>();
//            ArrayList<String> content = new ArrayList<>();
//            ArrayList<String> link = new ArrayList<>();
//            ArrayList<String> title = new ArrayList<>();
//
//            for (int i = 0; i < nodeList.getLength(); i++) {
//
//                Node node = nodeList.item(i);
//
//                Element authorList = (Element) node;
//                NodeList nameList = authorList.getElementsByTagName("name");
//                Element nameElement = (Element) nameList.item(0);
//                nameList = nameElement.getChildNodes();
//                authorNames.add(i, ((Node) nameList.item(0)).getNodeValue());
//
//                Element contentList = (Element) node;
//                NodeList contList = contentList.getElementsByTagName("content");
//                Element contentElement = (Element) contList.item(0);
//                contList = contentElement.getChildNodes();
//                content.add(i, ((Node) contList.item(0)).getNodeValue());
//
//                Element linkList = (Element) node;
//                NodeList linksList = linkList.getElementsByTagName("link");
//                Element linkElement = (Element) linksList.item(0);
//                linksList = linkElement.getChildNodes();
//                link.add(i, ((Node) linksList.item(0)).getNodeValue());
//
//                Element titleList = (Element) node;
//                NodeList titList = titleList.getElementsByTagName("title");
//                Element titleElement = (Element) titList.item(0);
//                titList = titleElement.getChildNodes();
//                title.add(i, ((Node) titList.item(0)).getNodeValue());
//            }
//
//            for (int i = 0; i < authorNames.size(); i++) {
//                RedditItemDTO newItem = new RedditItemDTO();
//                newItem.titleText = title.get(i);
//                newItem.contentText = content.get(i);
//                newItem.postURL = link.get(i);
//
//                myDataset.add(newItem);
//                mCaller.populateScreen(myDataset);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        RedditItemDTO item = new RedditItemDTO();
//        item.titleText = "Item Title";
//        item.contentText = "Content Text blah lah blah";
//
//        new DownloadImageTask(item).execute("https://api.learn2crack.com/android/images/donut.png");
//
//        RedditItemDTO item2 = new RedditItemDTO();
//        item2.titleText = "Second Item Title";
//        item2.contentText = "Second Content Text You know this actually works quite well hahahahahahaha";
//
//        new DownloadImageTask(item2).execute("https://pbs.twimg.com/profile_images/606585229034135553/2NqZJYQI.png");
//
//        RedditItemDTO item3 = new RedditItemDTO();
//        item3.titleText = "Third Item Title";
//        item3.contentText = "Third Content Text ";
//
//        new DownloadImageTask(item3).execute("https://lh3.googleusercontent.com/pzfbnn4blHagLueyDMs9jY7hGOQpWqxR8suYsgKhuZpDBd2f9S8ZxYyGvmE7LwdbO_9jBZXMnq5ya6zDjdq1hpRviYpr8nMJ83vcIpgAmTc5pCJdA0_65GcZUCLX61wIILJ0fT0I7NYsKd1Jpl6pf7H691Y6JtXNq9C_K-h5UiMWTyCC7dZRFVRlgoXX2_de61slcfMjX8v9GgJzCKmoB4ky6yDThE9FBRgxFrkkbAI_26P5_yleg-0MeeAkcLQqJ_ABk-IHP44BuNTrG5-L6MNSDWmkZSeT1OFGDWySKwZino5OO1RM-YmrhzlWm6IvqueO4O0SY5wwcNH0dXonXNk3WV3qfEyCusbZpJCLZKqxMzmcmiBdgVHOMSAtqOcL8I9nIS3lojmHs9FGIxq028SvliWokvsIDn9DmOjux1-TbJfOQOFB2hDgE2PsqXshNdwJqpsLMxTtXldECNMWgAxVUfqNcicv8BNLNJvTbUqQhLguXTIBDaNYSsnOGl7bJ3YPr4JXmxGIu1Rt6uVakwGQ4Zyfa7oDJheOpwBL6FJuwxA7-dMrv18OqPlPGWoTvMhWcLm08OvKoGKG2KyzFtQLQK6TKfb6TvnSEy5MyVpsC2TNPA=w1334-h275-no");

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(mCaller);
            mProgressDialog.setMessage("Please wait");
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
//                JSONObject post = children.getJSONObject("data");


//                JSONArray post = jsonObj.getJSONArray("data");
//                JSONObject obj = post.getJSONObject(0);
                // looping through All Contacts
                Toast.makeText(mContext, "SIze: " + children.length(), Toast.LENGTH_LONG);
                for (int i = 0; i < children.length(); i++) {
                    JSONObject p = children.getJSONObject(i);
//                    JSONObject p = obj.getJSONObject("data");
                    Iterator<?> keys = p.keys();

                    while( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if ( p.get(key) instanceof JSONObject && key.equals("data")) {
                            JSONObject obj = (JSONObject) p.get(key);
                            String permalink = obj.getString("permalink");
                            String url = obj.getString("url");
                            String title = obj.getString("title");
                            String thumbnail = obj.getString("thumbnail");
                            RedditItemDTO thisPost = new RedditItemDTO();
                            thisPost.postURL = permalink;
                            thisPost.titleText = title;
                            thisPost.contentText = url;
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        RedditItemDTO dto;

        public DownloadImageTask(@NonNull RedditItemDTO currentDTO) {
            dto = currentDTO;
        }

        @Override
        protected
        @Nullable
        Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap postImage = null;
            try {
                postImage = BitmapFactory.decodeStream((InputStream) new URL(urldisplay).getContent());

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                Toast.makeText(mContext, "Error Loading Image !", Toast.LENGTH_SHORT).show();
            }

            return postImage;
        }

        protected void onPostExecute(@Nullable Bitmap result) {
            RedditItemDTO item = dto;
            if (result != null) {
                item.imageBitmap = result;
            }

            myDataset.add(item);
            mCaller.populateScreen(myDataset);
        }
    }
}
