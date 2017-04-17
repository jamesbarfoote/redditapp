package com.app.appydinos.redditscraper.Components;

import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.appydinos.redditscraper.databinding.RedditListviewBinding;

import java.net.URI;


/**
 * Created by james on 16-Apr-17.
 */

public class RedditListView extends RecyclerView.ViewHolder {
    private RedditListviewBinding binding;
    public final ObservableField<String> TitleText = new ObservableField<String>();
    public final ObservableField<String> ContentText = new ObservableField<String>();

    public RedditListView(View itemView) {
        super(itemView);
    }

//    public RedditListView(RedditListviewBinding binding) {
//        super(binding.getRoot());
//        this.binding = binding;
//    }

    public void setTitle(String tileText) {
        TitleText.set(tileText);
    }

    public void setContentText(String content) {
        ContentText.set(content);
    }

    public void setImage(Uri image) {
        binding.image.setImageURI(image);
    }
}
