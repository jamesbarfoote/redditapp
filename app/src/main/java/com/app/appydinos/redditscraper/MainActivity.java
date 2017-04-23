package com.app.appydinos.redditscraper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.appydinos.redditscraper.Components.MyAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainActivityViewLogic mViewLogic;
    private String sortBy = "Hot";
    private String currentSub = "Askreddit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewLogic = new MainActivityViewLogic();
        mViewLogic.uiInitilised(getApplicationContext(), this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mViewLogic.getJSONData("askreddit", "hot");

        navigationView.getMenu().add("android");
        navigationView.getMenu().add("askreddit");
        navigationView.getMenu().add("newzealand");
        navigationView.getMenu().add("raspberry_pi");
        navigationView.getMenu().add("earthporn");
        navigationView.getMenu().add("redditdev");

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Post clicked!", Toast.LENGTH_LONG).show();
            }
        });

//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        mBinding.setViewLogic(mViewLogic);
//        mBinding.postImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Post clicked!", Toast.LENGTH_LONG);
//            }
//        });
    }

    protected void populateScreen(@NonNull ArrayList<RedditItemDTO> myDataset) {
        mAdapter = new MyAdapter(myDataset, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

        sortBy = item.getTitle().toString();
        mViewLogic.getJSONData(currentSub.toLowerCase(), sortBy.toLowerCase());

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        currentSub = item.getTitle().toString();
        mViewLogic.getJSONData(currentSub.toLowerCase(), sortBy.toLowerCase());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
