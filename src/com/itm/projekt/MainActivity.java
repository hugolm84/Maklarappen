/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt;

import android.app.SearchManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.itm.projekt.Drawer.DrawerMenu;
import com.itm.projekt.Fragments.SavedPlacesFragment;
import com.itm.projekt.Hemnet.MapFragment;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerMenu mDrawerMenu;
    private SearchView.OnQueryTextListener mQueryListener;
    private static final String TAG = MainActivity.class.getName();
    private List<WeakReference<Fragment>> mFragmentList = new ArrayList<WeakReference<Fragment>>();
    private Fragment mMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /**
         * Main view is the Fragment, but we dont want to add it
         * in the view directly. So we make a transaction here instead.
         */
        if (savedInstanceState == null) {

            mMapFragment = Fragment.instantiate(this, getResources().getString(R.string.mapFragment));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_frame, mMapFragment)
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
                    .commit();

        }

        mDrawerMenu = new DrawerMenu(this);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        try {
            File httpCacheDir = new File(getExternalCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:" + e.getMessage());
        }

        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if(cache != null) {
            cache.flush();
        }


    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int backStackCount = manager.getBackStackEntryCount();
        if(backStackCount > 0) {
            FragmentManager.BackStackEntry backEntry = manager.getBackStackEntryAt(backStackCount-1);
            if(backEntry != null) {
                String str = backEntry.getName();
                Log.d(TAG, "Got backentry " + str);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(str);
                if(fragment != null) {
                    getActionBar().setTitle(fragment.toString());
                }

            }
        }
        super.onBackPressed();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        addFragment(fragment);
    }

    /**
     * This will add a fragment so that we can keep an reference to it.
     * But it stays hidden
     * @param fragmentId
     * @return
     */
    public Fragment addFragment(final String fragmentId) {

        Fragment fragment = getFragment(fragmentId);

        if(fragment == null) {
            fragment = Fragment.instantiate(this, fragmentId);
        }

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        if(!fragment.isAdded()) {
            tx.add(R.id.content_frame, fragment).addToBackStack(fragmentId);
        }

        tx.hide(fragment);
        tx.commit();

        return fragment;
    }

    public void addFragment(final Fragment fragment){
        mFragmentList.add(new WeakReference(fragment));
    }

    public void swapFragments(final Fragment fragment1, final Fragment fragment2) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.hide(fragment1);

        if(!currentFragment.isHidden())
            tx.hide(currentFragment);

        tx.show(fragment2);
        tx.addToBackStack(fragment1.getTag());
        getActionBar().setTitle(fragment2.toString());
        tx.commit();

    }

    public Fragment getFragment(final String fragmentId) {
        for(WeakReference<Fragment> ref : mFragmentList) {
            Fragment f = ref.get();
            if(f != null && fragmentId.contains(f.getClass().getName()) ) {
                Log.d(TAG, "Hittade " + f.getClass().getName() + " == " + fragmentId);
                return f;
            }
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Add SearchWidget.
        SearchManager searchManager = (SearchManager) getSystemService( this.SEARCH_SERVICE );
        SearchView searchView = (SearchView) menu.findItem( R.id.action_mapsearch ).getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );

        mQueryListener = new SearchView.OnQueryTextListener( ) {

            private String prevQuery;

            @Override
            public boolean onQueryTextChange( String newText ) { Log.d(TAG, "newText " + newText); return true; }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(prevQuery != null && prevQuery.equals(query))
                    return false;

                Log.d(TAG, "Query! " + query);

                prevQuery = query;
                Fragment mapFragment = getFragment(getResources().getString(R.string.mapFragment));
                if(mapFragment != null) {
                    ((MapFragment)mapFragment).mapQuery(query);
                }
                return true;
            }
        };

        searchView.setOnQueryTextListener(mQueryListener);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerMenu.isDrawerOpen();
        menu.findItem(R.id.action_mapsearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerMenu.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_mapsearch:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateNotificationItem(final String fragmentId, int count) {
        mDrawerMenu.updateNotificationItem(fragmentId, count);
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerMenu.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerMenu.onConfigChanged(newConfig);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment cFrag = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(cFrag != null)
            getSupportFragmentManager().putFragment(outState, getResources().getString(R.string.currentFragment), cFrag);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSharedPreferences(getResources().getString(R.string.savedPrefs),0).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getSharedPreferences(getResources().getString(R.string.savedPrefs),0).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Fragment fragment = addFragment(getResources().getString(R.string.savedPlacesFragment));

        if(sharedPreferences.getStringSet(key, null) == null)
            return;

        if(fragment != null) {
            ((SavedPlacesFragment)fragment).updateListFromPreferences(sharedPreferences);
        }
    }
}