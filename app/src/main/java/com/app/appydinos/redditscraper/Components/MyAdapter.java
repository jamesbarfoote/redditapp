package com.app.appydinos.redditscraper.Components;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.appydinos.redditscraper.PostWrapperActivity.PostActivity;
import com.app.appydinos.redditscraper.R;
import com.app.appydinos.redditscraper.RedditItemDTO;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

/**
 * Created by james on 17-Apr-17.
 * Adapter that deals with displaying the cards
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<RedditItemDTO> mDataset;
    private Context mContext;
    private final int mWidth;
    private final int mHeight;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTitleText;
        private TextView mContentText;
        private final PhotoView mImage;
        private TextView mPostInfo;
        private CardView mCardView;

        private ViewHolder(View v) {
            super(v);
            mTitleText = (TextView) v.findViewById(R.id.title);
            mContentText = (TextView) v.findViewById(R.id.contentText);
            mImage = (PhotoView) v.findViewById(R.id.post_image);
            mPostInfo = (TextView) v.findViewById(R.id.postInfo);
            mCardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(@NonNull ArrayList<RedditItemDTO> myDataset, @NonNull Context context, int width, int height) {
        mDataset = myDataset;
        mContext = context;
        mWidth = width;
        mHeight = height;
    }

    public ArrayList<RedditItemDTO> getDataset() {
        return mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reddit_card, parent, false);

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final RedditItemDTO currentItem = mDataset.get(position);
        holder.mTitleText.setText(currentItem.titleText);
        holder.mContentText.setText(currentItem.contentText);
        if (!currentItem.imageURL.isEmpty()) {

            final ObjectAnimator anim = ObjectAnimator.ofInt(holder.mImage, "ImageLevel", 0, 100);
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.start();

//            holder.mImage.setMaxHeight(1000);
//            holder.mImage.setMinimumHeight(200);
            Glide.with(mContext)
                    .load(currentItem.imageURL)
                    .placeholder(R.drawable.ic_ring)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
//                    .centerCrop()
                    .into(holder.mImage);

            final ImagePopup imagePopup = new ImagePopup(mContext);
            imagePopup.setBackgroundColor(Color.BLACK);
            imagePopup.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            imagePopup.setWindowWidth(mWidth);
            imagePopup.setWindowHeight(mHeight);
            imagePopup.setHideCloseIcon(true);
            imagePopup.setImageOnClickClose(true);

            holder.mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Initiate Popup view **/
                    imagePopup.initiatePopup(holder.mImage.getDrawable());
                }
            });
        } else {
            holder.mImage.getLayoutParams().height = 0;
            holder.mImage.requestLayout();
        }
        holder.mPostInfo.setText(currentItem.user + " \u2022 " + currentItem.points + "pts \u2022 " + currentItem.comments + " comments");

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Post clicked! " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("THE_POST_PERMALINK_TPP", currentItem.postURL);
                mContext.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
