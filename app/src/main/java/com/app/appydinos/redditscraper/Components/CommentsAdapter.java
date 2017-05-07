package com.app.appydinos.redditscraper.Components;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.app.appydinos.redditscraper.PostWrapperActivity.Comment;
import com.app.appydinos.redditscraper.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

/**
 * Created by james on 24-Apr-17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private ArrayList<Comment> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mCommentInfo;
        private HtmlTextView htmlTextView;

        private ViewHolder(View v) {
            super(v);
            mCommentInfo = (TextView) v.findViewById(R.id.commentInfo);
            htmlTextView = (HtmlTextView) v.findViewById(R.id.commentsContent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentsAdapter(@NonNull ArrayList<Comment> myDataset, @NonNull Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    public ArrayList<Comment> getDataset() {
        return mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_layout, parent, false);

        return new CommentsAdapter.ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Comment currentItem = mDataset.get(position);
        holder.mCommentInfo.setText(currentItem.author + " \u2022 " + currentItem.points + " \u2022 " + currentItem.postedOn);
        holder.htmlTextView.setHtml(Html.fromHtml(currentItem.htmlText).toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
