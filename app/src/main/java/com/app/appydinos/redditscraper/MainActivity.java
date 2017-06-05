package com.app.appydinos.redditscraper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.app.appydinos.redditscraper.Components.MyAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainActivityViewLogic mViewLogic;
    private String sortBy = "hot";
    private String currentSub = "Askreddit";
    private NavigationView navigationView;
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewLogic = new MainActivityViewLogic();
        mViewLogic.uiInitilised(getApplicationContext(), this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mViewLogic.getJSONData("askreddit", "hot");

        EditText tv = new EditText(this);
        tv.setHint("Goto subreddit");
        tv.setWidth(this.getWindow().getWindowManager().getDefaultDisplay().getWidth());
        tv.setTextColor(Color.WHITE);
        tv.setHintTextColor(Color.GRAY);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(14);
        tv.setSingleLine();

        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENDCALL || actionId == KeyEvent.KEYCODE_CALL) {
                    currentSub = v.getText().toString();
                    mViewLogic.getJSONData(currentSub, sortBy);
                    v.setText("");
                    drawer.closeDrawers();
                }
                return false;
            }
        });

        navigationView.getMenu().add("").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        ArrayList<String> subs = mViewLogic.loadSavedSubs();
        for (String s : subs) {
            navigationView.getMenu().add(s);
        }

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                // do something
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getApplicationWindowToken(), 0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    protected void populateScreen(@NonNull ArrayList<RedditItemDTO> myDataset) {
        mAdapter = new MyAdapter(myDataset, this, getWindow().getWindowManager().getDefaultDisplay().getWidth(), getWindow().getWindowManager().getDefaultDisplay().getHeight());
        mRecyclerView.setAdapter(mAdapter);

        updateOverflowMenu();
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

    //The action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        if (mViewLogic.loadSavedSubs().contains(currentSub)) {
            menu.add("Remove Sub");
        }

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        optionsMenu = menu;

        return true;
    }


    //The action bar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().toString().equalsIgnoreCase("save")) {
            //Clicked the save button
            String newSub = mViewLogic.saveSub(currentSub);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            if (!newSub.isEmpty()) {
                navigationView.getMenu().add(newSub);
            }
        } else if (item.getTitle().toString().equalsIgnoreCase("Sort")) {
            //Creating the instance of PopupMenu
            View menuItemView = findViewById(R.id.sortButton);
            PopupMenu popup = new PopupMenu(this, menuItemView);

            //Inflating the Popup using xml file
            popup.inflate(R.menu.sort_menu);

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    sortBy = item.getTitle().toString().toLowerCase();
                    mViewLogic.getJSONData(currentSub.toLowerCase(), sortBy.toLowerCase());
                    return true;
                }
            });

            popup.show();//showing popup menu
        } else {
            //User clicked remove sub
            mViewLogic.removeSavedSub(currentSub);
        }

        return super.onOptionsItemSelected(item);
    }

    //The side bar navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        if (!item.getTitle().toString().equals("Save")) {
            currentSub = item.getTitle().toString();
            mViewLogic.getJSONData(currentSub.toLowerCase(), sortBy.toLowerCase());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void updateOverflowMenu() {
        if (optionsMenu != null) {
            boolean containsRemoveItem = false;
            int index = -1;
            int saveIconIndex = -1;
            for (int i = 0; i < optionsMenu.size(); i++) {
                if (optionsMenu.getItem(i).getTitle().equals("Remove Sub")) {
                    containsRemoveItem = true;
                    index = i;
                }

                if (optionsMenu.getItem(i).getTitle().equals("Save")) {
                    saveIconIndex = i;
                }
            }

            if (mViewLogic.loadSavedSubs().contains(currentSub) && !containsRemoveItem) {
                onPrepareOptionsMenu(optionsMenu);
                optionsMenu.add("Remove Sub");
            } else if (optionsMenu != null && containsRemoveItem) {
                onPrepareOptionsMenu(optionsMenu);
                optionsMenu.removeItem(index);
            }

            if (saveIconIndex >= 0 && mViewLogic.loadSavedSubs().contains(currentSub)) {
                optionsMenu.getItem(saveIconIndex).setEnabled(false);
                optionsMenu.getItem(saveIconIndex).setVisible(false);
            } else if (saveIconIndex > -1 && !mViewLogic.loadSavedSubs().contains(currentSub)) {
                optionsMenu.getItem(saveIconIndex).setEnabled(true);
                optionsMenu.getItem(saveIconIndex).setVisible(true);

            }
        }
    }
}
