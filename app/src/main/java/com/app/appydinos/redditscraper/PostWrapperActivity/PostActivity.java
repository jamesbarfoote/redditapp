package com.app.appydinos.redditscraper.PostWrapperActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.appydinos.redditscraper.Components.CommentsAdapter;
import com.app.appydinos.redditscraper.R;
import com.app.appydinos.redditscraper.RedditItemDTO;
import com.app.appydinos.redditscraper.databinding.PostLayoutBinding;

/**
 * Created by james on 23-Apr-17.
 */

public class PostActivity extends AppCompatActivity {
    PostActivityViewLogic mViewLogic;
    private CommentsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PostLayoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getLayoutInflater().inflate(R.layout.post_layout, null);
        setContentView(R.layout.post_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewLogic = new PostActivityViewLogic();
        mViewLogic.uiInitilised(getApplicationContext(), this);

        String postURL = getIntent().getExtras().getString("THE_POST_PERMALINK_TPP");

        mBinding = DataBindingUtil.setContentView(this, R.layout.post_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.commentList);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mViewLogic.getJSONData(postURL, "new");
    }

    public void populateScreen(RedditItemDTO dto) {
        mBinding.postContent.setText(dto.contentText);
        mBinding.postTitle.setText(dto.titleText);

        mBinding.user.setText(dto.user + " \u2022 " + dto.timeSincePost);
        mBinding.postPoints.setText(dto.points + "pts \u2022 " + dto.comments + " comments");

        mAdapter = new CommentsAdapter(dto.commentsList, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
//        mBinding.postImage.setImageResource(android.R.drawable.alert_light_frame);
    }


//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        PostLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.post_layout, container, false);
//        return binding.getRoot();
//    }

//    @Override
//    public void onBackPressed() {
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("Hot");
        menu.add("New");
        menu.add("Top");
        menu.add("Rising");
        menu.add("Controversial");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sortBy = item.getTitle().toString();
        return super.onOptionsItemSelected(item);
    }

}

