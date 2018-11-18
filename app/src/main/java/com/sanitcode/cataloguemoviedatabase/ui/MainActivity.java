package com.sanitcode.cataloguemoviedatabase.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sanitcode.cataloguemoviedatabase.R;
import com.sanitcode.cataloguemoviedatabase.adapter.ViewPagerAdapter;
import com.sanitcode.cataloguemoviedatabase.data.local.DatabaseContract;
import com.sanitcode.cataloguemoviedatabase.data.model.MovieFavorite;
import com.sanitcode.cataloguemoviedatabase.fragment.NowPlayingFragment;
import com.sanitcode.cataloguemoviedatabase.fragment.PopularFragment;
import com.sanitcode.cataloguemoviedatabase.fragment.UpcomingFragment;
import com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn;
import com.sanitcode.cataloguemoviedatabase.utils.TabPager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TabLayout.OnTabSelectedListener {

    private static final String INTENT_SEARCH = "intent_search";
    private static final String INTENT_TAG = "tag";
    private static final String INTENT_DETAIL = "detail";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpViewpager(viewPager);

        setupTab();
        selectDrawer();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.alerttext))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.keywords));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(INTENT_SEARCH, query);
                intent.putExtra(INTENT_TAG, "search");
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent mIntent = new Intent(this, SettingActivity.class);
            startActivity(mIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_popular) {
            selectTab(0);
        } else if (id == R.id.nav_now_playing) {
            selectTab(1);
        } else if (id == R.id.nav_upcoming) {
            selectTab(2);
        } else if (id == R.id.nav_favorite) {
            favoriteList();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
        } else if (id == R.id.nav_languange) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void favoriteList() {
        ArrayList<MovieFavorite> movieFavoriteArrayList = new ArrayList<>();
        Cursor cursor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(DatabaseContract.CONTENT_URI, null,
                    null, null, null, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(cursor).moveToFirst();
        }
        MovieFavorite favorite;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.requireNonNull(cursor).getCount() > 0) {
                do {
                    favorite = new MovieFavorite(cursor.getString(cursor.getColumnIndexOrThrow(
                            FavoriteColumn.FAVORITE_ID)));
                    movieFavoriteArrayList.add(favorite);
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }
        }
        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
        intent.putExtra(INTENT_DETAIL, movieFavoriteArrayList);
        intent.putExtra(INTENT_TAG, "detail");
        startActivity(intent);
    }

    private void selectDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    void setUpViewpager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.populateFragment(new PopularFragment(), getString(R.string.popular));
        adapter.populateFragment(new NowPlayingFragment(), getString(R.string.now_playing));
        adapter.populateFragment(new UpcomingFragment(), getString(R.string.upcoming));

        viewPager.setAdapter(adapter);
    }

    private void setupTab() {
        viewPager.setAdapter(new TabPager(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.popular);
        tabLayout.getTabAt(1).setText(R.string.now_playing);
        tabLayout.getTabAt(2).setText(R.string.upcoming);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void selectNav(int navNumber) {
        navigationView.getMenu().getItem(navNumber).setChecked(true);
    }

    private void selectTab(int tabNumber) {
        tabLayout.getTabAt(tabNumber).select();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selectNav(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
